package com.innowave.mahaulb.reports.manager;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innowave.mahaulb.reports.util.ConfUtil;
import com.innowave.mahaulb.reports.util.ReportException;
import com.innowave.mahaulb.reports.util.Templates;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReportsContext;
@Service
public class ReportFromDb {
	
	static org.slf4j.Logger logger = LoggerFactory.getLogger(ReportFromDb.class);
	@Autowired
	DataSource dataSource;
	
	@Autowired
	ConfUtil confUtil;
	
	static {
		JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
//		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.pdfEmbedded", "true");
//		jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.encoding", "Identity-H");
	}
	
	public void generateReport(String query) {
		build(query);
	}
	
	public void generateReport(String query, List<Column> columns, String reportName) {
		build(query, columns, reportName, null, null);
	}
	
	public void generateReportBetweenDateRange(String query, List<Column> columns, String reportName, String fromDate, String toDate) {
		build(query, columns, reportName, fromDate, toDate);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void build(String query, List<Column> columns, String repName, String fromDate, String toDate) {
		Connection connection = null;
		try {
			JasperReportBuilder report = report();
			for (Column column : columns) {
				report.addColumn(col.column(column.getTitle(), column.getField(), (DRIDataType) type.detectType(column.getDataType())));
			}
			
			
			connection = dataSource.getConnection();
			
			
			JasperReportBuilder builder = report
			  .setTemplate(Templates.reportTemplate)
			  .title(Templates.createTitleComponent("From Innowave Data Warehouse"));
			 if(fromDate != null && toDate != null) {
				 builder = builder.pageHeader(cmp.text("From Date: "+fromDate+"           To Date: "+toDate));
			 }
			  
			
			builder.pageFooter(Templates.footerComponent, cmp.line())
			 // .setDataSource(createDataSource())
			  .setDataSource(query, connection)
			  .highlightDetailOddRows()
			  .toPdf(new FileOutputStream(confUtil.getDocumentsLocation()+File.separator+repName+".pdf"));
			 // .show();
			connection.close();
		} catch (DRException e) {
			e.printStackTrace();
			logger.error("problem in report generation", e);
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("problem in report generation", e);
			}
			throw new ReportException("problem..", e);
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("problem in report generation", e);
			}
			logger.error("problem in report generation", e);
			throw new ReportException("problem..", e);
		}
	}
	
	private String getName() {
		Calendar calendar = Calendar.getInstance();
		int hr = calendar.get(Calendar.HOUR);
		int sec = calendar.get(Calendar.SECOND);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int year = calendar.get(Calendar.YEAR);
		return ""+day+"-"+month+"-"+year+"-"+hr+"-"+sec;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void build(String query) {
		Connection connection = null;
		try {
			JasperReportBuilder report = report();
			for (Column column : createColumns()) {
				report.addColumn(col.column(column.getTitle(), column.getField(), (DRIDataType) type.detectType(column.getDataType())));
			}
			connection = dataSource.getConnection();
			report
			  .setTemplate(Templates.reportTemplate)
			  .title(Templates.createTitleComponent("From Print Reciept Table"))
			  .pageFooter(Templates.footerComponent)
			 // .setDataSource(createDataSource())
			  .setDataSource(query, connection)
			  .show();
			connection.close();
		} catch (DRException e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("problem in report generation", e);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("problem in report generation", e);
			}
		}
	}
	
	

	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("item", "orderdate", "quantity", "unitprice");
		dataSource.add("Notebook", new Date(), 1, new BigDecimal(500));
		return dataSource;
	}

	private List<Column> createColumns() {
		List<Column> columns = new ArrayList<Column>();
		columns.add(new Column("ULB Id",        "ulb_id",      "integer"));//dataType = "String", "STRING", "java.lang.String", "text"
		columns.add(new Column("Reciept Amount",    "receipt_amount",  "integer"));//dataType = "Integer", "INTEGER", "java.lang.Integer"
		columns.add(new Column("Name of Owner",  "name_owner", "string"));//dataType = "bigdecimal", "BIGDECIMAL", "java.math.BigDecimal"
		
		return columns;
	}
	
	public String validate(String qry) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.createStatement().executeQuery(qry);
			connection.close();
			return "Success";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Invalid Query", e);
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("problem in report generation", e);
			}
			return "Failure "+e.getMessage();
		}
	}

	

	public static void main(String[] args) {
		new ColumnDetectDataTypeReport();
	}
}

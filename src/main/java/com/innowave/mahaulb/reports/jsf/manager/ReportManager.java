package com.innowave.mahaulb.reports.jsf.manager;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;

import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowave.mahaulb.reports.data.Report;
import com.innowave.mahaulb.reports.data.Table;
import com.innowave.mahaulb.reports.data.ULBLogoMapper;
import com.innowave.mahaulb.reports.manager.Column;
import com.innowave.mahaulb.reports.manager.Manager;
import com.innowave.mahaulb.reports.manager.ReportFromDb;
import com.innowave.mahaulb.reports.services.ReportService;
import com.innowave.mahaulb.reports.services.ULBLogoMapperService;
import com.innowave.mahaulb.reports.util.ConfUtil;
import com.innowave.mahaulb.reports.util.ReportException;
import com.test.domain.UserManager;

@ManagedBean(name = "reportManager", eager = true)
@SessionScoped
@Service
public class ReportManager {

	List<Table> tables = new ArrayList<Table>();
	
	List<String> selectedTables = new ArrayList<String>();
	
	List<Table> selectedTabs = new ArrayList<Table>();
	
	Table calculationColumnsTable = new Table("Table_For_Adding_Calculation_Columns");
	
	private Map<String, Table> map = new HashMap<String, Table>();
	
	Report report = new Report();
	@Transient
	Manager manager;
	
	@Transient
	ReportFromDb reportService;
	
	@Transient
	ReportService service;
	
	String queryValidation = "";
	
	List<Report> reports = new ArrayList<Report>();
	
	ConfUtil confUtil;
	
	ObjectMapper mapper = new ObjectMapper();
	
	Boolean editReportName = true;
	
	 @ManagedProperty(value="#{userManager}") 
	 UserManager userManager;
	 
	 List<String> ulbnames;
	 
	 transient ULBLogoMapperService logoMapperService;
	 
	 String dataTypes[] = {"integer", "string", "date", "dateYearToFraction", "bigDecimal"};
	@PostConstruct
	public void init() throws SQLException {
//		if(!getUserManager().getLogin()) {
//			throw new ReportException("Can not access page without Authentication");
//		}
		manager = SpringUtil.getService(Manager.class);
		reportService = SpringUtil.getService(ReportFromDb.class);
		service = SpringUtil.getService(ReportService.class);
		confUtil = SpringUtil.getService(ConfUtil.class);
		logoMapperService = SpringUtil.getService(ULBLogoMapperService.class);
		tables = manager.getTablesForSchema("receipt");
		reports = service.findAll();
		for(Table tab:tables) {
			map.put(tab.getTableName(), tab);
		}
		
		 ulbnames = manager.getColumnData( "receipt", "ulb_names", "ulb_name");
	}
	
	public void reload() {
//		if(!getUserManager().getLogin()) {
//			throw new ReportException("Can not access page without Authentication");
//		}
		reports = service.findAll();
		
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	
	
	public List<String> getSelectedTables() {
		return selectedTables;
	}

	public void setSelectedTables(List<String> selectedTables) {
		this.selectedTables = selectedTables;
	}

	public void populateDetailsInSelectedTables() throws SQLException {
		
		process();
		RequestContext.getCurrentInstance().update("reportForm:selected");
	}
	
	private void process() throws SQLException {
		selectedTabs.clear();
		for(String table : selectedTables) {
			Table t = map.get(table);
			t.setColumns(manager.getColumns2(table, "receipt"));
			selectedTabs.add(t);
		}
	}

	public List<Table> getSelectedTabs() {
		return selectedTabs;
	}

	public void setSelectedTabs(List<Table> selectedTabs) {
		this.selectedTabs = selectedTabs;
	}
	
	public void gotoQueryBuilder() throws SQLException {
		getReport().getSelectedTablesAndColumns().clear();
		for(Table tab : getSelectedTabs()) {
			List<Column> selectedForQry = new ArrayList<Column>();
			Table t = new Table();
			t.setTableName(tab.getTableName());
				for(Column column : tab.getColumns()) {
					if(column.getWantedInReport()) {
						selectedForQry.add(column);
					}
				}
			t.setColumns(selectedForQry);
			getReport().getSelectedTablesAndColumns().add(t);
		}
		
		Iterator<Column> itr = calculationColumnsTable.getColumns().iterator();
			while(itr.hasNext()) {
				Column calculated = itr.next();
					if(!calculated.getWantedInReport()) {
						itr.remove();
					}
			}
			/**
			 * Make sure for calc columns field and title names are one and same.
			 */
			setFieldNameForCalcColumns();
			getReport().getSelectedTablesAndColumns().add(calculationColumnsTable);
		RequestContext.getCurrentInstance().update("reportForm:selected");
	}
	
	private void setFieldNameForCalcColumns() {
		for(Column column : calculationColumnsTable.getColumns()) {
			column.setField(column.getTitle());
		}
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
	
	public void generateReport(String ext) {
		try {
				if(getReport().getReportName() == null || getReport().getReportName().trim().length() == 0) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Name can not be blank");
					RequestContext.getCurrentInstance().showMessageInDialog(msg);
					return;
				}
				
				if(getReport().getQuery() == null || getReport().getQuery().trim().length() == 0) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Query can not be blank");
					RequestContext.getCurrentInstance().showMessageInDialog(msg);
					return;
				}
				
				if(!validate()) {
					return;
				}
				gotoQueryBuilder();
				
			
			List<Column> aggregatedColumns = new ArrayList<Column>();
			/**
			 * Add columns of all tables in report
			 */
			for(Table table:getReport().getSelectedTablesAndColumns()) {
				aggregatedColumns.addAll(table.getColumns());
			}
			if(report.getFromDate() != null && report.getToDate() != null && report.getDateRangeParam() != null) {
				if(report.getFromDate().trim().length() != 0 && report.getToDate().trim().length()!=0) {
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						Date d = formatter.parse(report.getFromDate());
						d = formatter.parse(report.getToDate());
						ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
						reportService.generateReportBetweenDateRange(getReport().getQuery(), aggregatedColumns, getReport().getReportName(), report.getFromDate(), report.getToDate(), ext, logoMapper);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failure", "Improper Date Releated Data");
						RequestContext.getCurrentInstance().showMessageInDialog(msg);
						setQueryValidation("");
						return;
					}
					
				}
				else {
					ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
					reportService.generateReport(getReport().getQuery(), aggregatedColumns, getReport().getReportName(), ext, logoMapper );
				}
				
				
			}
			else {
				ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
				reportService.generateReport(getReport().getQuery(), aggregatedColumns, getReport().getReportName(), ext, logoMapper );
			}
			
			getReport().setReportLink(confUtil.getDocumentsServerBaseUrl()+"/"+getReport().getReportName()+"."+ext);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Go to "+getReport().getReportLink()+" to download the report");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			setQueryValidation("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failure - "+e.getMessage(), "Report Generation failed");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
		}
	}
	
	/**
	 * This method is called from Reports.xhtml page. This has to be a stateless method and no state variable has to be impacted by this.
	 * @param report
	 */
	public void generateReportFromOutside(Report report, String ext) {
		try {
			/**
			 * Neccessary because we store columns and tables json in report obj. so it is neccessary to marshal it back to object structure.
			 */
			List<Table> selTablesAndCols = mapper.readValue(report.getSelectedTablesAndColumnsJson(), mapper.getTypeFactory().constructCollectionType(List.class, Table.class));
			report.setSelectedTablesAndColumns(selTablesAndCols);
			
			List<String> selTables = mapper.readValue(report.getSelectedTablesJson(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
			report.setSelectedTables(selTables);
			
			List<Column> aggregatedColumns = new ArrayList<Column>();
			/**
			 * Add columns of all tables in report
			 */
			for(Table table:report.getSelectedTablesAndColumns()) {
				aggregatedColumns.addAll(table.getColumns());
			}
			ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
			reportService.generateReport(report.getQuery(), aggregatedColumns, report.getReportName(), ext, logoMapper );
			report.setReportLink(confUtil.getDocumentsServerBaseUrl()+"/"+report.getReportName()+"."+ext);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Go to "+report.getReportLink()+" to download the report");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failure - "+e.getMessage(), "Report Generation  - "+report.getReportName()+" failed");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
		}
	}
	
	
	
	public boolean validate() {
		if(getReport().getQuery() == null || getReport().getQuery().trim().length() == 0) {
			setQueryValidation("Query Validation Failure");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Query can not be blank");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			return false;
		}
		
		String val = reportService.validate(getReport().getQuery());
		if(val.equalsIgnoreCase("Success")) {
			setQueryValidation("Query Validation Success");
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			return true;
		}
		else {
			setQueryValidation("Query Validation Failed -"+val);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", val);
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			return false;
		}
		
	}

	public String getQueryValidation() {
		return queryValidation;
	}

	public void setQueryValidation(String queryValidation) {
		this.queryValidation = queryValidation;
	}
	
	public String saveReport(String ext) throws JsonProcessingException, SQLException{
		String val = reportService.validate(getReport().getQuery());
		if(val.equalsIgnoreCase("Success")) {
			setQueryValidation("Query Validation Success");
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			getReport().setSelectedTables(getSelectedTables());
			gotoQueryBuilder();
			String jsonTablesAndColumns = mapper.writeValueAsString(getReport().getSelectedTablesAndColumns());
			getReport().setSelectedTablesAndColumnsJson(jsonTablesAndColumns);
			
			String jsonTables = mapper.writeValueAsString(getReport().getSelectedTables());
			getReport().setSelectedTablesJson(jsonTables);
			generateReport(ext);
			service.saveOrUpdate(getReport());
			setQueryValidation("");
			return "Reports.xhtml?faces-redirect=false";
		}
		else {
			setQueryValidation("Query Validation Failed -"+val);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", val);
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			return "";
		}
		
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
	
	public String edit(Report report) throws SQLException, JsonParseException, JsonMappingException, IOException {
		this.report = report;
		setEditReportName(false);
		List<Table> selTablesAndCols = mapper.readValue(report.getSelectedTablesAndColumnsJson(), mapper.getTypeFactory().constructCollectionType(List.class, Table.class));
		report.setSelectedTablesAndColumns(selTablesAndCols);
		
		List<String> selTables = mapper.readValue(report.getSelectedTablesJson(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
		report.setSelectedTables(selTables);
		List<String> l1 = new ArrayList<String>();
		l1.addAll(report.getSelectedTables());
		setSelectedTables(l1);
		
		List<Table> l2 = new ArrayList<Table>();
		Table calcTable = getCalculationTable(selTablesAndCols);
			if(calcTable != null) {
				selTablesAndCols.remove(calcTable);
				setCalculationColumnsTable(calcTable);
			}
		l2.addAll(selTablesAndCols);
	/**
	 * Why having new list l2 - so that data at Reportmanager level and report level are distinct.
	 */
		setSelectedTabs(l2);
		//process();
		return "Main.xhtml?faces-redirect=false";
	}
	
	private Table getCalculationTable(List<Table> tables) {
		for(Table table : tables) {
			if(table.getTableName().equals("Table_For_Adding_Calculation_Columns")) {
				return table;
			}
		}
		return null;
	}
	
	public void delete(Report report) {
		service.delete(report.getId());
	
	}
	
	public String createNew() {
		setReport(new Report());
		setEditReportName(true);
		setSelectedTabs(new ArrayList<Table>());
		setSelectedTables(new ArrayList<String>());
		setCalculationColumnsTable(new Table("Table_For_Adding_Calculation_Columns"));
		return "Main.xhtml?faces-redirect=true";
	}

	public Boolean getEditReportName() {
		return editReportName;
	}

	public void setEditReportName(Boolean editReportName) {
		this.editReportName = editReportName;
	}
	
	public String goback() {
		setQueryValidation("");
		return "Reports.xhtml?faces-redirect=false";
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	
	public void addColumnToReport() {
		
		Column col = new Column("Edit Title", "Edit Field Name", "string");
		col.setWantedInReport(true);
		calculationColumnsTable.getColumns().add(col);
		RequestContext.getCurrentInstance().update("reportForm:CalculatedTable");
	}
	
	public void removeColumn(Column column) {
		calculationColumnsTable.getColumns().remove(column);
		RequestContext.getCurrentInstance().update("reportForm:CalculatedTable");
	}

	public Table getCalculationColumnsTable() {
		return calculationColumnsTable;
	}

	public void setCalculationColumnsTable(Table calculationColumnsTable) {
		this.calculationColumnsTable = calculationColumnsTable;
	}

	public String[] getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(String[] dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	public void updateQuery() {
		if(report.getFromDate()!= null && report.getToDate()!= null && report.getDateRangeParam() != null  ) {
			if(report.getDateRangeParam().trim().length() !=0 ) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				try {
					Date d = formatter.parse(report.getFromDate());
					d = formatter.parse(report.getToDate());
					String fromDt = "TO_DATE('"+report.getFromDate()+"','dd-MM-yyyy')";
					String toDt = "TO_DATE('"+report.getToDate()+"','dd-MM-yyyy')";
					
					String qry = report.getQuery();
					if( qry.toLowerCase().contains("group by")) {
						if(qry.toLowerCase().contains("where")) {
							qry = qry.replace("group by", " AND "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"   group by");
						}
						else {
							qry = qry.replace("group by", " where "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"   group by");
						}
						
					}
					else {
						if(qry.toLowerCase().contains("where")) {
							qry +=  " AND "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"";
						}
						else {
							qry +=  " where "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"";
						}
						
					}
				report.setQuery(qry);
				RequestContext.getCurrentInstance().update("reportForm:queryBox");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status", "Invalid Date Formats");
					RequestContext.getCurrentInstance().showMessageInDialog(msg);
					
				}
				
				
			}
			
		}
		else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status", "In Complete Details. Enter from/to dates and date parameter");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
		}
	}
	
	public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

	public List<String> getUlbnames() {
		return ulbnames;
	}

	public void setUlbnames(List<String> ulbnames) {
		this.ulbnames = ulbnames;
	}
	
	
}

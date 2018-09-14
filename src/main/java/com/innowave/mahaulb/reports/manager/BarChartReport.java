package com.innowave.mahaulb.reports.manager;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;

import com.innowave.mahaulb.reports.util.Templates;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class BarChartReport {

	public BarChartReport() {
		build();
	}

	private void build() {
		FontBuilder boldFont = stl.fontArialBold().setFontSize(12);

		TextColumnBuilder<String> itemColumn = col.column("Date Range", "date", type.stringType());
		TextColumnBuilder<Integer> ulb1 = col.column("Collection ULB 1", "ulb1", type.integerType());
		TextColumnBuilder<BigDecimal> ulb2 = col.column("Collection ULB 2", "ulb2", type.bigDecimalType());

		try {
			report()
				.setTemplate(Templates.reportTemplate)
				//.columns(itemColumn, quantityColumn, unitPriceColumn)
				.title(Templates.createTitleComponent("BarChart"))
				.summary(
					cht.barChart()
						.setTitle("Bar chart - ULB1 Wise ULB2")
						.setTitleFont(boldFont)
						.setCategory(itemColumn)
						.series(
							cht.serie(ulb1), cht.serie(ulb2))
						.setCategoryAxisFormat(
							cht.axisFormat().setLabel("Item")))
				.pageFooter(Templates.footerComponent)
				.setDataSource(createDataSource())
				.show();
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("date", "ulb1", "ulb2");
		dataSource.add("2017", 350, 700);
		dataSource.add("2016", 300, 500);
		dataSource.add("2015", 450, 600);
		dataSource.add("2014", 350, 50);
		dataSource.add("2013", 300, 900);
		dataSource.add("2012", 450, 800);
		return dataSource;
	}

	public static void main(String[] args) {
		new BarChartReport();
	}
}

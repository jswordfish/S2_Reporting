package com.innowave.mahaulb.reports.util;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class Test {
	
	public static  ReportTemplateBuilder reportTemplate;
	public static final StyleBuilder rootStyle;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder bold12CenteredStyle;
	public static final StyleBuilder bold18CenteredStyle;
	public static final StyleBuilder bold22CenteredStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;
	static {
		rootStyle           = stl.style().setPadding(2);
		boldStyle           = stl.style(rootStyle).bold();
		italicStyle         = stl.style(rootStyle).italic();
		boldCenteredStyle   = stl.style(boldStyle)
		                         .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
		bold12CenteredStyle = stl.style(boldCenteredStyle)
		                         .setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle)
		                         .setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle)
                             .setFontSize(22);
		columnStyle         = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
		columnTitleStyle    = stl.style(columnStyle)
		                         .setBorder(stl.pen1Point())
		                         .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
		                         .setBackgroundColor(Color.LIGHT_GRAY)
		                         .bold();
		groupStyle          = stl.style(boldStyle)
		                         .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
		subtotalStyle       = stl.style(boldStyle)
		                         .setTopBorder(stl.pen1Point());
		reportTemplate = template()
				  .setLocale(Locale.ENGLISH)
                  //.setColumnStyle(columnStyle)
                  .setColumnTitleStyle(columnTitleStyle);
	}
	
	public static void main(String args[]) {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item());
		items.add(new Item());
		items.add(new Item());
		items.add(new Item());
		items.add(new Item());
		items.add(new Item());
		JasperReportBuilder report = DynamicReports.report();
		StyleBuilder styleBuilder = stl.style().setRightBorder((stl.pen1Point().setLineColor(Color.BLACK)));
		
		report
		.columns(
				 Columns.column("Customer Name", "name", DataTypes.stringType()),
	
			      Columns.column("Customer Name", "name", DataTypes.stringType())
		)
		.setColumnFooterStyle(styleBuilder)
		.title(Components.text("SimpleReportExample1"))
		.highlightDetailEvenRows() 
		//.pageFooter(cmp.line())
		//.pageHeader(cmp.line())
		.setDataSource(items);
	
		stl.style().setRightBorder((stl.pen1Point().setLineColor(Color.BLACK))).build();
	
		
		JasperReportBuilder report2 = DynamicReports.report();
		List<Item> items2 = new ArrayList<Item>();
		items2.addAll(items);
		//report.sette
		report2
		.columns(
				 Columns.column("Customer Name2", "name", DataTypes.stringType()),
			      Columns.column("Category2", "category", DataTypes.stringType())
			 
			     
		)
		.setColumnFooterStyle(styleBuilder)
		.title(Components.text("SimpleReportExample2") )
		.highlightDetailEvenRows() 
		//.pageFooter(cmp.line())
		//.pageHeader(cmp.line())
		.setDataSource(items2);
		
		JasperReportBuilder report3 = DynamicReports.report();
				report3
				.title(cmp.horizontalList(Components.subreport(report), Components.subreport(report2)));
		
		try {
            //show the report
			//report.show();
		
		            //export the report to a pdf file
			report3.toPdf(new FileOutputStream("report.pdf"));
			
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

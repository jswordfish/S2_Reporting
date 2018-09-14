/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2016 Ricardo Mariaca
 * http://www.dynamicreports.org
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

package com.innowave.mahaulb.reports.util;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.innowave.mahaulb.reports.data.ULBLogoMapper;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Templates {
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

	public static final ReportTemplateBuilder reportTemplate;
	public static final CurrencyType currencyType;
	public static final ComponentBuilder<?, ?> dynamicReportsComponent;
	public static final ComponentBuilder<?, ?> footerComponent;

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
		columnStyle         = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE).setLeftPadding(5).setRightPadding(5).setFontName("gnu-free").setBorder(stl.pen1Point());
		//columnStyle         = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE).setLeftPadding(5).setRightPadding(5).setBorder(stl.pen1Point());
		columnTitleStyle    = stl.style(columnStyle)
		                         .setBorder(stl.pen1Point())
		                         .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
		                         .setBackgroundColor(Color.LIGHT_GRAY)
		                         .bold();
		groupStyle          = stl.style(boldStyle)
		                         .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
		subtotalStyle       = stl.style(boldStyle)
		                         .setTopBorder(stl.pen1Point());

		StyleBuilder crosstabGroupStyle      = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
		                                          .setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
		                                          .setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle       = stl.style(columnStyle)
		                                          .setBorder(stl.pen1Point());

		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
			.setHeadingStyle(0, stl.style(rootStyle).bold());

		reportTemplate = template()
		                   .setLocale(Locale.ENGLISH)
		                   .setColumnStyle(columnStyle)
		                   .setColumnTitleStyle(columnTitleStyle)
		                   .setGroupStyle(groupStyle)
		                   .setGroupTitleStyle(groupStyle)
		                   .setSubtotalStyle(subtotalStyle)
		                   .highlightDetailEvenRows()
		                   .crosstabHighlightEvenRows()
		                   .setCrosstabGroupStyle(crosstabGroupStyle)
		                   .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
		                   .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
		                   .setCrosstabCellStyle(crosstabCellStyle)
		                   .setTableOfContentsCustomizer(tableOfContentsCustomizer);

		currencyType = new CurrencyType();

		HyperLinkBuilder link = hyperLink("http://www.innowave.com");
		dynamicReportsComponent =
		  cmp.horizontalList(
				 // cmp.image("images/image_left.jpeg").setHorizontalImageAlignment(HorizontalImageAlignment.LEFT),
		  	cmp.verticalList(
		  		//cmp.text("Innowave Reports").setStyle(bold22CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
		  		//cmp.text("http://www.innowave.com").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(300);
		  		
		  		//cmp.text("ULB Name - Test ULB"),
		  		//cmp.text("Report Name - Test Report"))).setFixedWidth(300);
		//	cmp.image("images/image_right.jpeg").setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT));
		  	));
//		footerComponent = cmp.pageXofY()
//		                     .setStyle(
//		                     	stl.style(boldCenteredStyle)
//		                     	   .setTopBorder(stl.pen1Point()));
		footerComponent = cmp.verticalList().add(cmp.pageXofY()
                .setStyle(
                    	stl.style(boldCenteredStyle)
                    	   .setTopBorder(stl.pen1Point())), cmp.text("Report Generated from Adhoc – Tool").setStyle(italicStyle));
				
				
				
	}

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(String label) {
		return cmp.horizontalList()
		        .add(
		        	dynamicReportsComponent,
		        	//cmp.image("images/ULB LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.LEFT).setFixedDimension(80, 80),
		        	cmp.image("images/ULB LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.LEFT).setFixedDimension(80, 80),
			  		cmp.text("ULB Name - Test ULB <br> Report Name - Test Report").setStyle(stl.style().bold().setFontSize(16).setFontName("gnu-free").setMarkup(Markup.HTML)).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
			  		cmp.image("images/MAH LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT).setFixedDimension(50, 50))
		        .newRow()
		        .add(cmp.line())
		        .newRow()
		        .add(cmp.verticalGap(5));
	}
	
	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleLabels(ULBLogoMapper logoMapper, String reportName) {
		return cmp.horizontalList()
		        .add(
		        	dynamicReportsComponent,
		        	//cmp.image("images/ULB LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.LEFT).setFixedDimension(75, 75),
		        	cmp.image(logoMapper != null ? logoMapper.getLeftLogoPath() : "images/ULB LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.LEFT).setFixedDimension(75, 75),
			  		cmp.text("ULB Name - "+(logoMapper != null? logoMapper.getUlbName():"ALL")+" <br> Report Name - "+reportName).setStyle(stl.style().bold().setFontSize(16).setFontName("gnu-free").setMarkup(Markup.HTML)).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
			  		cmp.image(logoMapper !=null?logoMapper.getRightLogoPath():"images/MAH LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT).setFixedDimension(50, 50))
		        .newRow()
		        .add(cmp.line())
		        .newRow()
		        .add(cmp.verticalGap(5));
	}
	
	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleLabels(ULBLogoMapper logoMapper, String reportName, String user) {
		return cmp.horizontalList()
		        .add(
		        	dynamicReportsComponent,
		        	//cmp.image("images/ULB LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.LEFT).setFixedDimension(75, 75),
		        	cmp.image(logoMapper != null ? logoMapper.getLeftLogoPath() : "images/ULB LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.LEFT).setFixedDimension(75, 75),
		        	cmp.text((logoMapper != null? logoMapper.getUlbName():"ALL")+" <br> Report Name - "+reportName).setStyle(stl.style().bold().setFontSize(16).setFontName("gnu-free").setMarkup(Markup.HTML)).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
		        	//	cmp.text("ULB Name - "+(logoMapper != null? logoMapper.getUlbName():"ALL")+" <br> Report Name - "+reportName).setStyle(stl.style().bold().setFontSize(16).setFontName("gnu-free").setMarkup(Markup.HTML)).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
			  		cmp.image(logoMapper !=null?logoMapper.getRightLogoPath():"images/MAH LOGO.png").setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT).setFixedDimension(50, 50))
		        .newRow()
		        .add(cmp.text("Generated by "+user+". Generated at "+getCurrentDateAndTime()))
		        .newRow()
		        .add(cmp.verticalGap(5));
	}
	
	private static String getCurrentDateAndTime() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = sdf.format(cal.getTime());
        return strDate;
	}

	public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
		return new CurrencyValueFormatter(label);
	}

	public static class CurrencyType extends BigDecimalType {
		private static final long serialVersionUID = 1L;

		@Override
		public String getPattern() {
			return "$ #,###.00";
		}
	}

	private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {
		private static final long serialVersionUID = 1L;

		private String label;

		public CurrencyValueFormatter(String label) {
			this.label = label;
		}

		
		public String format(Number value, ReportParameters reportParameters) {
			return label + currencyType.valueToString(value, reportParameters.getLocale());
		}
	}
}
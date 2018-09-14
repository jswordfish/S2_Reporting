package com.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.innowave.mahaulb.reports.manager.Column;
import com.innowave.mahaulb.reports.manager.Manager;
import com.innowave.mahaulb.reports.manager.ReportFromDb;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:reportContext.xml"})
public class TestMetaData {
	@Autowired
	Manager manager;
	
	@Autowired
	ReportFromDb repService;
	
	@Test
	public void getColumnsMetaData() throws Exception {
		manager.getColumnsMetaData("receipt_print");
	}
	
	@Test
	public void getTables() throws Exception {
		List<String> tables = manager.getAllTables("receipt");
			for(String tab : tables) {
				System.out.println(tab);
			}
	}
	
	@Test
	public void testReport() {
		Column column1 = new Column("ULB ID", "ulb_id", "integer");
		Column column2 = new Column("Reciept Amount", "receipt_amount", "integer");
		Column column3 = new Column("Balance Amount", "bal_amount", "integer");
		Column column4 = new Column("Name of Owner", "name_owner", "string");
		Column column5 = new Column("Address", "address", "string");
		Column column6 = new Column("Reciept Date", "receipt_date", "date");
		List<Column> columns = new ArrayList<Column>();
		columns.add(column1);
		columns.add(column2);
		columns.add(column3);
		columns.add(column4);
		columns.add(column5);
		columns.add(column6);
		repService.generateReport("select ulb_id, receipt_amount, bal_amount, name_owner, address, receipt_date  from receipt_print where bal_amount = 0", columns, "Report 1", "csv", null);
	}
	
	@Test
	public void testCollectionAmountReport() {
		Column column1 = new Column("Collection Amount", "Collection Amount", "integer");
		Column column2 = new Column("ULB ID", "ulb_id", "integer");
		
		List<Column> columns = new ArrayList<Column>();
		columns.add(column1);
		columns.add(column2);
	
		repService.generateReportBetweenDateRange("select ulb_id, sum(receipt_amount) as \"Collection Amount\"   from receipt_print group by ulb_id ", columns, "Collection Report", "01-01-2017", "20-12-2017", "pdf", null);
	}
	
	@Test
	public void testCountOfReceiptWithPartPaymentReport() {
		Column column1 = new Column("Count of Receipt With Part Payment", "Count of Receipt With Part Payment", "integer");
		Column column2 = new Column("ULB ID", "ulb_id", "integer");
		
		List<Column> columns = new ArrayList<Column>();
		columns.add(column1);
		columns.add(column2);
	
		repService.generateReport("select ulb_id, count(receipt_number) as \"Count of Receipt With Part Payment\" from receipt.receipt_print group by ulb_id", columns, "Count of Receipt With Part Payment", "pdf", null);
	}

}

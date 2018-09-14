package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.innowave.mahaulb.reports.data.Report;
import com.innowave.mahaulb.reports.services.ReportService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:reportContext.xml"})
public class TestDatabase {
	@Autowired
	ReportService   reportService;

	@Test
	@Rollback(value=false)
	public void testRep() {
		Report rep = new Report();
		rep.setReportName("del");
		rep.setQuery("rrr");
		reportService.saveOrUpdate(rep);
	}
}

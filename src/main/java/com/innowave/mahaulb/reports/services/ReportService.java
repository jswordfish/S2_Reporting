package com.innowave.mahaulb.reports.services;

import java.util.List;

import com.innowave.mahaulb.reports.data.Report;
import com.innowave.mahaulb.reports.util.ReportException;

public interface ReportService extends BaseService{

	public void saveOrUpdate(Report report) throws ReportException;
	
	
	 public Report getUniqueReportByName(String reportName) throws ReportException;
	
	 public void delete(Long id) throws ReportException;
	 
	 public List<Report> getReportsWithNewVersion() throws ReportException;
	 
	 public List<Report> getReportsByULB(String ulb) throws ReportException;
	
}

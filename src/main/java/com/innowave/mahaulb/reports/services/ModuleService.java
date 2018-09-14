package com.innowave.mahaulb.reports.services;

import java.util.List;

import com.fasterxml.jackson.databind.Module;
import com.innowave.mahaulb.reports.data.Report;
import com.innowave.mahaulb.reports.util.ReportException;

public interface ModuleService extends BaseService{

	public void saveOrUpdate(com.innowave.mahaulb.reports.data.Module module) throws ReportException;
	
	
	 public com.innowave.mahaulb.reports.data.Module getUniqueModuleByName(String reportName) throws ReportException;
	
	 public void delete(Long id) throws ReportException;
	 
	 public List<com.innowave.mahaulb.reports.data.Module> getModules(Integer ulbId) throws ReportException;
	
}
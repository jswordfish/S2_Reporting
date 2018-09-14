package com.innowave.mahaulb.reports.services;

import com.innowave.mahaulb.reports.data.ULBLogoMapper;
import com.innowave.mahaulb.reports.util.ReportException;

public interface ULBLogoMapperService extends BaseService{

	public void saveOrUpdate(ULBLogoMapper ulbLogoMapper) throws ReportException;
	
	
	 public ULBLogoMapper getUniqueULBLogoMapper(String ulbName) throws ReportException;
	
	 public void delete(Long id) throws ReportException;
	
}

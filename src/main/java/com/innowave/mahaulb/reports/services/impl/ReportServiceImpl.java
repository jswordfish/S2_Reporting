package com.innowave.mahaulb.reports.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import com.innowave.mahaulb.reports.dao.JPADAO;
import com.innowave.mahaulb.reports.dao.ReportDAO;
import com.innowave.mahaulb.reports.data.Report;
import com.innowave.mahaulb.reports.services.ReportService;
import com.innowave.mahaulb.reports.util.ConfUtil;
import com.innowave.mahaulb.reports.util.ReportException;

@Component("reportService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=ReportException.class)
public class ReportServiceImpl extends BaseServiceImpl<Long, Report> implements ReportService{
	@Autowired
    protected ReportDAO dao;
	
	
	@Autowired
	ConfUtil confService;
	
	@PostConstruct
    public void init() throws Exception {
	 super.setDAO((JPADAO) dao);
    }
    
    @PreDestroy
    public void destroy() {
    }
    
    @Override
    public void setEntityManagerOnDao(EntityManager entityManager){
    	dao.setEntityManager(entityManager);
    }

	public void saveOrUpdate(Report report) throws ReportException {
		// TODO Auto-generated method stub
		Report report2 = getUniqueReportByName(report.getReportName());
			if(report2 == null) {
				//create
				report.setCreatedDate(new Date());
				dao.persist(report);
			}
			else {
				//update
				
				DozerBeanMapper beanMapper = new DozerBeanMapper();
				report.setId(report2.getId());
				beanMapper.map(report, report2);
				report2.setLastModifiedDate(new Date());
				dao.merge(report2);
			}
	}
	
	public void delete(Long id) {
		super.delete(id);
	}

	public Report getUniqueReportByName(String reportName) throws ReportException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("reportName", reportName);
		List<Report> reports = findByNamedQueryAndNamedParams(
				"Report.getUniqueReport", queryParams);
		if(reports.size() > 1){
			throw new ReportException("TOO_MANY_Reports_BY_SAME_NAME");
		}
		if(reports.size() == 0){
			return null;
		}
		//MultipleBagFetchException
		Report ret = reports.get(0);
		
		return ret;
	}
    
    
	
	

}

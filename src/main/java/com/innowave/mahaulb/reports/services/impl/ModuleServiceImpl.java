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
import com.innowave.mahaulb.reports.dao.ModuleDAO;
import com.innowave.mahaulb.reports.data.Module;
import com.innowave.mahaulb.reports.services.ModuleService;
import com.innowave.mahaulb.reports.util.ConfUtil;
import com.innowave.mahaulb.reports.util.ReportException;

@Component("moduleService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=ReportException.class)
public class ModuleServiceImpl extends BaseServiceImpl<Long, com.innowave.mahaulb.reports.data.Module> implements ModuleService{
	@Autowired
    protected ModuleDAO dao;
	
	
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

	public void saveOrUpdate(Module module) throws ReportException {
		// TODO Auto-generated method stub
		Module module2 = getUniqueModuleByName(module.getModuleName());
			if(module2 == null) {
				//create
				module.setCreatedDate(new Date());
				dao.persist(module);
			}
			else {
				//update
				
				DozerBeanMapper beanMapper = new DozerBeanMapper();
				module.setId(module2.getId());
				beanMapper.map(module, module2);
				module2.setTableNames(module.getTableNames());
				module2.setLastModifiedDate(new Date());
				dao.merge(module2);
			}
	}
	
	public void delete(Long id) {
		super.delete(id);
	}

	public Module getUniqueModuleByName(String moduleName) throws ReportException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("moduleName", moduleName);
		List<Module> modules = findByNamedQueryAndNamedParams(
				"Module.getUniqueModule", queryParams);
		if(modules.size() > 1){
			throw new ReportException("TOO_MANY_modules_BY_SAME_NAME");
		}
		if(modules.size() == 0){
			return null;
		}
		//MultipleBagFetchException
		Module ret = modules.get(0);
		
		return ret;
	}

	public List<com.innowave.mahaulb.reports.data.Module> getModules(Integer ulbId){
		Map<String, Integer> queryParams = new HashMap<String, Integer>();
		queryParams.put("ulbId", ulbId);
		List<com.innowave.mahaulb.reports.data.Module> modules = findByNamedQueryAndNamedParams(
				"Module.getModulesByUlbId", queryParams);
		return modules;
	}

	
    
    
	
	

}

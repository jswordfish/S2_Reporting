package com.innowave.mahaulb.reports.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import com.innowave.mahaulb.reports.dao.JPADAO;
import com.innowave.mahaulb.reports.dao.ULBLogoMapperDAO;
import com.innowave.mahaulb.reports.data.ULBLogoMapper;
import com.innowave.mahaulb.reports.services.ULBLogoMapperService;
import com.innowave.mahaulb.reports.util.ConfUtil;
import com.innowave.mahaulb.reports.util.ReportException;

@Component("ulbLogoMapperService")
@org.springframework.transaction.annotation.Transactional(propagation= Propagation.REQUIRED, rollbackFor=ReportException.class)
public class ULBLogoMapperServiceImpl extends BaseServiceImpl<Long, ULBLogoMapper> implements ULBLogoMapperService{
	@Autowired
    protected ULBLogoMapperDAO dao;
	
	
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

	public void saveOrUpdate(ULBLogoMapper ulbLogoMapper) throws ReportException {
		// TODO Auto-generated method stub
		ULBLogoMapper ulbLogoMapper2 = getUniqueULBLogoMapper(ulbLogoMapper.getUlbName());
			if(ulbLogoMapper2 == null) {
				//create
				ulbLogoMapper.setCreatedDate(new Date());
				ulbLogoMapper.setLastModifiedDate(new Date());
				ulbLogoMapper = saveAndGenerateLogoPath(ulbLogoMapper);
				dao.persist(ulbLogoMapper);
				
			}
			else {
				//update
				ulbLogoMapper = saveAndGenerateLogoPath(ulbLogoMapper);
				DozerBeanMapper beanMapper = new DozerBeanMapper();
				ulbLogoMapper.setId(ulbLogoMapper2.getId());
				beanMapper.map(ulbLogoMapper, ulbLogoMapper2);
				ulbLogoMapper2.setLastModifiedDate(new Date());
				dao.merge(ulbLogoMapper2);
			}
	}
	
	private ULBLogoMapper saveAndGenerateLogoPath(ULBLogoMapper mapper){
    	try {
			if(mapper.getLeftLogoFileExtension() != null && mapper.getLeftLogoFileExtension().trim().length() != 0 && mapper.getLeftLogo() != null){
				FileUtils.writeByteArrayToFile(new File(confService.getLogoPathToSave()+File.separator+mapper.getUlbName()+"_left."+mapper.getLeftLogoFileExtension()), mapper.getLeftLogo());
				mapper.setLeftLogoPath(confService.getLogoPathToSave()+File.separator+mapper.getUlbName()+"_left."+mapper.getLeftLogoFileExtension());
			}
			else {
				if(mapper.getLeftLogoPath() == null) {
					throw new ReportException("Insufficient Logo Data - Either file name or extension is invalid");
				}
				
			}
			
			if(mapper.getRightLogoFileExtension() != null && mapper.getRightLogoFileExtension().trim().length() != 0 && mapper.getRightLogo() != null){
				FileUtils.writeByteArrayToFile(new File(confService.getLogoPathToSave()+File.separator+mapper.getUlbName()+"_right."+mapper.getRightLogoFileExtension()), mapper.getRightLogo());
				mapper.setRightLogoPath(confService.getLogoPathToSave()+File.separator+mapper.getUlbName()+"_right."+mapper.getRightLogoFileExtension());
			}
			else {
				if(mapper.getRightLogoPath() == null) {
					throw new ReportException("Insufficient Logo Data - Either file name or extension is invalid");
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ReportException("CAN_NOT_CREATE_LOGO_FILE", e);
		}
    	return mapper;
    }
	
	public void delete(Long id) {
		super.delete(id);
	}

	public ULBLogoMapper getUniqueULBLogoMapper(String ulbName) throws ReportException {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ulbName", ulbName);
		List<ULBLogoMapper> logoMappers = findByNamedQueryAndNamedParams(
				"ULBLogoMapper.getUniqueULBLogoMapper", queryParams);
		if(logoMappers.size() > 1){
			throw new ReportException("TOO_MANY_ulbName_mapping_BY_SAME_NAME");
		}
		if(logoMappers.size() == 0){
			return null;
		}
		//MultipleBagFetchException
		ULBLogoMapper ret = logoMappers.get(0);
		
		return ret;
	}
    
    
	
	

}

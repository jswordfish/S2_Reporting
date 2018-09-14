package com.innowave.mahaulb.reports.jsf.manager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Service;

import com.innowave.mahaulb.reports.data.Module;
import com.innowave.mahaulb.reports.data.ULBLogoMapper;
import com.innowave.mahaulb.reports.manager.Manager;
import com.innowave.mahaulb.reports.services.ModuleService;
import com.innowave.mahaulb.reports.services.ULBLogoMapperService;
import com.innowave.mahaulb.reports.util.ConfUtil;
import com.innowave.mahaulb.reports.util.ReportException;
import com.test.domain.UserManager;

@ManagedBean(name = "ulbManager", eager = true)
@SessionScoped
@Service
public class ULBMapperManager {
	
	
	private List<ULBLogoMapper> list = new ArrayList<ULBLogoMapper>();
	
	
	transient ULBLogoMapperService  mapperService;
	
	ULBLogoMapper uLBLogoMapper;
	
	 @ManagedProperty(value="#{userManagerNew}") 
	 UserManagerNew userManager;
	 
	 
	 transient UploadedFile leftLogo;
	 
	 
	 transient UploadedFile rightLogo;
	 
	
	transient Manager manager;
	
	
	 
	List<String> ulbnames;
	 
	
	 transient ConfUtil confUtil;
	 
	
	
	
	@PostConstruct
	public void init() throws SQLException {
		if(!getUserManager().getLogin()) {
			throw new ReportException("Can not access Admin page without Authentication");
		}
		mapperService = SpringUtil.getService(ULBLogoMapperService.class);
		list = mapperService.findAll();
		 manager = SpringUtil.getService(Manager.class);
		// 
		 ulbnames = manager.getColumnData("receipt", "tm_ulb", "ulb_name_en");
		 confUtil = SpringUtil.getService(ConfUtil.class);
		
	}
	
	public void editMapping(ULBLogoMapper logoMapper) {
		this.uLBLogoMapper = logoMapper;
		showDialog();
	}
	
	
	
	public void delete(Long id) {
		mapperService.delete(id);
		list = mapperService.findAll();
	}
	
	private void showDialog() {
		Map<String,Object> options = new HashMap<String, Object>();
		 options.put("draggable", false);
     options.put("modal", true);
     options.put("position", "top"); // <--- not work
     options.put("width", "90%");
     options.put("contentWidth", "90%");
     options.put("height", "500");
     options.put("contentheight", "90%");
     options.put("size", "auto");

   
    Map<String, List<String>> params = new HashMap<String, List<String>>();
 
    RequestContext.getCurrentInstance().openDialog("mapper",options, params);
	}
	
	public void createNewMapping() {
		uLBLogoMapper = new ULBLogoMapper();
		showDialog();
	}
	
	public String logout() {
		 ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
		     return "loginNew.xhtml?faces-redirect=true";
	}

	public List<ULBLogoMapper> getList() {
		return list;
	}

	public void setList(List<ULBLogoMapper> list) {
		this.list = list;
	}

	public ULBLogoMapper getuLBLogoMapper() {
		return uLBLogoMapper;
	}

	public void setuLBLogoMapper(ULBLogoMapper uLBLogoMapper) {
		this.uLBLogoMapper = uLBLogoMapper;
	}

//	public UserManager getUserManager() {
//		return userManager;
//	}
//
//	public void setUserManager(UserManager userManager) {
//		this.userManager = userManager;
//	}
	
	

	public UploadedFile getLeftLogo() {
		return leftLogo;
	}

	public UserManagerNew getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManagerNew userManager) {
		this.userManager = userManager;
	}

	public void setLeftLogo(UploadedFile leftLogo) {
		this.leftLogo = leftLogo;
	}

	public UploadedFile getRightLogo() {
		return rightLogo;
	}

	public void setRightLogo(UploadedFile rightLogo) {
		this.rightLogo = rightLogo;
	}
	
	public void uploadLeft() {
		if(leftLogo != null){
			String fileName = leftLogo.getFileName();
		    String contentType = leftLogo.getContentType();
		    byte[] contents = leftLogo.getContents(); // Or getInputStream()
		    this.uLBLogoMapper.setLeftLogo(contents);
		    this.uLBLogoMapper.setLeftLogoFileExtension(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
		    
		}
	    
	    // ... Save it, now!
	}
	
	public void uploadRight() {
		if(rightLogo != null){
			String fileName = rightLogo.getFileName();
		    String contentType = rightLogo.getContentType();
		    byte[] contents = rightLogo.getContents(); // Or getInputStream()
		    this.uLBLogoMapper.setRightLogo(contents);
		    this.uLBLogoMapper.setRightLogoFileExtension(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
		    
		}
	    
	    // ... Save it, now!
	}
	
	public void  save() {
		mapperService.saveOrUpdate(uLBLogoMapper);
		//list = mapperService.findAll();
		RequestContext.getCurrentInstance().closeDialog(null);
		//RequestContext.getCurrentInstance().update("ulbsForm:ulbs");
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status", "Update Successfull. Please referesh the page to see updated values.");
		RequestContext.getCurrentInstance().showMessageInDialog(msg);
	}

	public List<String> getUlbnames() {
		return ulbnames;
	}

	public void setUlbnames(List<String> ulbnames) {
		this.ulbnames = ulbnames;
	}

	public ConfUtil getConfUtil() {
		return confUtil;
	}

	public void setConfUtil(ConfUtil confUtil) {
		this.confUtil = confUtil;
	}
	
	
}

package com.innowave.mahaulb.reports.jsf.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.stereotype.Service;

import com.innowave.mahaulb.reports.data.Ulb;
import com.innowave.mahaulb.reports.manager.Manager;

@ManagedBean(name = "userManagerNew", eager = true)
@SessionScoped
@Service
public class UserManagerNew {
	
	private String user;
	
	private String password;

	private Integer ulbId;
	
	String ulbName;
	
	private List<Ulb> ulbs = new ArrayList<>();
	
	String errorTitle;
	
	String errorDetails;
	
	Boolean login = false;
	
	Manager manager;
	
//	@ManagedProperty("#{reportManagerNew}")
//	ReportManagerNew reportManagerNew;
	
	@PostConstruct
    private void init() throws SQLException {
		
		manager = SpringUtil.getService(Manager.class);
		ulbs = manager.getAllULBs();
    }
	
	public String login() throws SQLException {
		if(user == null || password == null) {
			setErrorTitle("User or Password not entered");
			setErrorDetails("Enter All Details");
			login = false;
			return "Error.xhtml?faces-redirect=true";
		}
		
		if(getUlbId() == null || getUlbId() == 0) {
			setErrorTitle("ULB Not selected");
			setErrorDetails("Please select ULB");
			login = false;
			return "Error.xhtml?faces-redirect=true";
		}
		
		if(user.equals("demo.user") && password.equals("secret") ) {
			String ulbname = manager.getUlbName(getUlbId());
				if(ulbname.equalsIgnoreCase("ADMIN")) {
					login = true;
					return "allUlbs.xhtml?faces-redirect=true";
				}
			
		}
		
		if(!(password.equals("secret") && user.equals("demo.user"))) {
			setErrorTitle("Invalid Credentials");
			setErrorDetails("Try again");
			login = false;
			return "Error.xhtml?faces-redirect=true";
		}
		login = true;
		return "ReportsNew.xhtml?faces-redirect=true";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public Boolean getLogin() {
		return login;
	}

	public void setLogin(Boolean login) {
		this.login = login;
	}

	public Integer getUlbId() {
		return ulbId;
	}

	public void setUlbId(Integer ulbId) {
		this.ulbId = ulbId;
	}

	public List<Ulb> getUlbs() {
		return ulbs;
	}

	public void setUlbs(List<Ulb> ulbs) {
		this.ulbs = ulbs;
	}

	

	public String getUlbName() {
		return ulbName;
	}

	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}

	
	
	

}

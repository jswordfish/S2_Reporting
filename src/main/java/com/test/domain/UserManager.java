package com.test.domain;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.stereotype.Service;

@ManagedBean(name = "userManager", eager = true)
@SessionScoped
@Service
public class UserManager {
	
	private String user;
	
	private String password;

	
	String errorTitle;
	
	String errorDetails;
	
	Boolean login = false;
	
	@PostConstruct
    private void init() {
		
		
    }
	
	public String login() {
		if(user == null || password == null) {
			setErrorTitle("User or Password not entered");
			setErrorDetails("Enter All Details");
			login = false;
			return "Error.xhtml?faces-redirect=true";
		}
		
		if(!(password.equals("secret") && user.equals("demo.user"))) {
			setErrorTitle("Invalid Credentials");
			setErrorDetails("Try again");
			login = false;
			return "Error.xhtml?faces-redirect=true";
		}
		login = true;
		return "Reports.xhtml?faces-redirect=true";
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

	
	
	

}

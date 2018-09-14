package com.innowave.mahaulb.reports.util;

public class ConfUtil {
	/**
	 * Default location
	 */
	String documentsLocation ="";
	
	/**
	 * Default location
	 */
	String documentsServerBaseUrl = "";
	
	String logoPathToSave;
	
	String logoImageServerBaseUrl = "";
	
	String databaseSchema = "";
	
	String localDocumentsServerBaseUrl = "";

	public String getDocumentsLocation() {
		return documentsLocation;
	}

	public void setDocumentsLocation(String documentsLocation) {
		this.documentsLocation = documentsLocation;
	}

	public String getDocumentsServerBaseUrl() {
		return documentsServerBaseUrl;
	}

	public void setDocumentsServerBaseUrl(String documentsServerBaseUrl) {
		this.documentsServerBaseUrl = documentsServerBaseUrl;
	}

	public String getLogoPathToSave() {
		return logoPathToSave;
	}

	public void setLogoPathToSave(String logoPathToSave) {
		this.logoPathToSave = logoPathToSave;
	}

	public String getLogoImageServerBaseUrl() {
		return logoImageServerBaseUrl;
	}

	public void setLogoImageServerBaseUrl(String logoImageServerBaseUrl) {
		this.logoImageServerBaseUrl = logoImageServerBaseUrl;
	}

	public String getDatabaseSchema() {
		return databaseSchema;
	}

	public void setDatabaseSchema(String databaseSchema) {
		this.databaseSchema = databaseSchema;
	}

	public String getLocalDocumentsServerBaseUrl() {
		return localDocumentsServerBaseUrl;
	}

	public void setLocalDocumentsServerBaseUrl(String localDocumentsServerBaseUrl) {
		this.localDocumentsServerBaseUrl = localDocumentsServerBaseUrl;
	}


	
	
	
}

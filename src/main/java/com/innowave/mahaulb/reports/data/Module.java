package com.innowave.mahaulb.reports.data;

import java.util.ArrayList;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="Module.getUniqueModule", 
			query="SELECT r FROM Module r WHERE r.moduleName=:moduleName"),
	
	@NamedQuery(name="Module.getModulesByUlbId", 
	query="SELECT r FROM Module r WHERE r.ulbId=:ulbId")
})
public class Module extends Base{
	
	String moduleName;
	
	Integer ulbId;
	
	String ulbName;
	
	@ElementCollection(fetch=FetchType.EAGER)
	java.util.List<String> tableNames = new ArrayList<String>();
	
	String schemaName;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public java.util.List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(java.util.List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public Integer getUlbId() {
		return ulbId;
	}

	public void setUlbId(Integer ulbId) {
		this.ulbId = ulbId;
	}

	public String getUlbName() {
		return ulbName;
	}

	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}
	
	public String getTablesInString() {
		String ret = "";
		for(String tb : getTableNames()) {
			ret += tb+",";
		}
		
		if(ret.trim().length() != 0) {
			ret = ret.substring(0, ret.length()-1);
		}
	return ret;
	}

	public String getSchemaName() {
			if(this.schemaName == null || this.schemaName.trim().length() == 0) {
				return "receipt";
			}
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	
	
}

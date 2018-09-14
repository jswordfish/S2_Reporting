package com.innowave.mahaulb.reports.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
@Entity
@NamedQueries({
	@NamedQuery(name="Report.getUniqueReport", 
			query="SELECT r FROM Report r WHERE r.reportName=:reportName"),
	@NamedQuery(name="Report.getReportsCreatedWithUIBuilder", 
	query="SELECT r FROM Report r WHERE r.newVersion=:newVersion"),
	@NamedQuery(name="Report.getReportsByUlbName", 
	query="SELECT r FROM Report r WHERE r.ulb=:ulb")
})
public class Report extends Base{

	@Column(nullable = false)
	String reportName;
	
	@Column(nullable = false, columnDefinition="text")
	String query;
	
	@Transient
	List<Table> selectedTablesAndColumns = new ArrayList<Table>();
	
	@Lob
	@Type(type = "text")
	String selectedTablesAndColumnsJson;
	
	String reportLink = "";
	
	@Transient
	String reportExtension;
	
	@Transient
	List<String> selectedTables = new ArrayList<String>();
	
	String fromDate;
	
	String toDate;
	
	String dateRangeParam;
	
	@Lob
	@Type(type = "text")
	String selectedTablesJson;
	
	String ulbNameUsedForLogos;
	
	String ulb;
	
	String moduleName;
	
	@Transient
	String user;
	
		
	@Transient
	WhereClause whereClause;
	
	@Lob
	@Type(type = "text")
	String whereClauseJson;
	
	@Transient
	FromClause fromClause;
	
	@Lob
	@Type(type = "text")
	String fromClauseJson;
	
	Boolean newVersion = false;
	String schemaName;

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Table> getSelectedTablesAndColumns() {
		return selectedTablesAndColumns;
	}

	public void setSelectedTablesAndColumns(List<Table> selectedTablesAndColumns) {
		this.selectedTablesAndColumns = selectedTablesAndColumns;
	}

	public String getReportLink() {
		return reportLink;
	}

	public void setReportLink(String reportLink) {
		this.reportLink = reportLink;
	}

	public List<String> getSelectedTables() {
		return selectedTables;
	}

	public void setSelectedTables(List<String> selectedTables) {
		this.selectedTables = selectedTables;
	}

	public String getReportExtension() {
		return reportExtension;
	}

	public void setReportExtension(String reportExtension) {
		this.reportExtension = reportExtension;
	}

	public String getSelectedTablesAndColumnsJson() {
		return selectedTablesAndColumnsJson;
	}

	public void setSelectedTablesAndColumnsJson(String selectedTablesAndColumnsJson) {
		this.selectedTablesAndColumnsJson = selectedTablesAndColumnsJson;
	}

	public String getSelectedTablesJson() {
		return selectedTablesJson;
	}

	public void setSelectedTablesJson(String selectedTablesJson) {
		this.selectedTablesJson = selectedTablesJson;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getDateRangeParam() {
		return dateRangeParam;
	}

	public void setDateRangeParam(String dateRangeParam) {
		this.dateRangeParam = dateRangeParam;
	}

	public String getUlbNameUsedForLogos() {
		return ulbNameUsedForLogos;
	}

	public void setUlbNameUsedForLogos(String ulbNameUsedForLogos) {
		this.ulbNameUsedForLogos = ulbNameUsedForLogos;
	}

	public String getUlb() {
		return ulb;
	}

	public void setUlb(String ulb) {
		this.ulb = ulb;
	}

	public WhereClause getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(WhereClause whereClause) {
		this.whereClause = whereClause;
	}

	public FromClause getFromClause() {
		return fromClause;
	}

	public void setFromClause(FromClause fromClause) {
		this.fromClause = fromClause;
	}

	public String getWhereClauseJson() {
		return whereClauseJson;
	}

	public void setWhereClauseJson(String whereClauseJson) {
		this.whereClauseJson = whereClauseJson;
	}

	public String getFromClauseJson() {
		return fromClauseJson;
	}

	public void setFromClauseJson(String fromClauseJson) {
		this.fromClauseJson = fromClauseJson;
	}

	public Boolean getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(Boolean newVersion) {
		this.newVersion = newVersion;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	
}

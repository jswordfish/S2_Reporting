package com.innowave.mahaulb.reports.jsf.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.persistence.Transient;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowave.mahaulb.reports.data.FromClause;
import com.innowave.mahaulb.reports.data.Module;
import com.innowave.mahaulb.reports.data.Report;
import com.innowave.mahaulb.reports.data.Table;
import com.innowave.mahaulb.reports.data.ULBLogoMapper;
import com.innowave.mahaulb.reports.data.WhereClause;
import com.innowave.mahaulb.reports.manager.Column;
import com.innowave.mahaulb.reports.manager.Manager;
import com.innowave.mahaulb.reports.manager.ReportFromDbNew;
import com.innowave.mahaulb.reports.services.ModuleService;
import com.innowave.mahaulb.reports.services.ReportService;
import com.innowave.mahaulb.reports.services.ULBLogoMapperService;
import com.innowave.mahaulb.reports.util.ConfUtil;

@ManagedBean(name = "reportManagerNew", eager = true)
@SessionScoped
@Service
public class ReportManagerNew {
List<Table> tables = new ArrayList<Table>();
	
	List<String> selectedTables = new ArrayList<String>();
	
	List<Table> selectedTabs = new ArrayList<Table>();
	
	
	
	private Map<String, Table> map = new HashMap<String, Table>();
	
	Report report = new Report();
	@Transient
	Manager manager;
	
	@Transient
	ReportFromDbNew reportService;
	
	@Transient
	ReportService service;
	
	String queryValidation = "";
	
	List<Report> reports = new ArrayList<Report>();
	
	ConfUtil confUtil;
	
	ObjectMapper mapper = new ObjectMapper();
	
	Boolean editReportName = true;
	
	 @ManagedProperty("#{userManagerNew}")
	 UserManagerNew userManager;
	 
	 List<String> ulbnames;
	 
	 transient ULBLogoMapperService logoMapperService;
	 
	 QueryBuilder queryBuilder;
	 
	 Integer ulbId;
	 
	 String moduleName;
	 
	 Module module;
	 
	 public Module getModule() {
		return moduleService.getUniqueModuleByName(getModuleName());
	}

	List<Module> moduleNames = new ArrayList<>();
	 
	 ModuleService moduleService;
	 
	 String dataTypes[] = {"integer", "string", "date", "dateYearToFraction", "bigDecimal"};
	@PostConstruct
	public void init() throws SQLException {
//		if(!getUserManager().getLogin()) {
//			throw new ReportException("Can not access page without Authentication");
//		}
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		manager = SpringUtil.getService(Manager.class);
		reportService = SpringUtil.getService(ReportFromDbNew.class);
		service = SpringUtil.getService(ReportService.class);
		confUtil = SpringUtil.getService(ConfUtil.class);
		logoMapperService = SpringUtil.getService(ULBLogoMapperService.class);
		moduleService = SpringUtil.getService(ModuleService.class);
		//tables = manager.getTablesForSchema("receipt");
		//reports = service.findAll();
		//reports = service.getReportsWithNewVersion();
		reload();
		
//		for(Table tab:tables) {
//			map.put(tab.getTableName(), tab);
//		}
		
		// ulbnames = manager.getColumnData("ulb_names", "ulb_name");
		ulbnames = manager.getColumnData("receipt", "tm_ulb", "ulb_name_en");//remove hardcoding later
		
	}
	
	public void loadAllModules() {
		this.moduleNames = moduleService.getModules(getUlbId());
	}
	
	public void loadTablesByModule() {
		
		Module mod = moduleService.getUniqueModuleByName(getModuleName());
		setModule(mod);
	
		String schema = "";
			if(mod.getSchemaName() == null || mod.getSchemaName().trim().length() == 0) {
				schema = "receipt";
			}
			else {
				schema = mod.getSchemaName();
			}
		//tables =  manager.getTablesForSchema(schema, mod.getTableNames());
			/**
			 * Just making sure new tables added are reflected at run time.
			 */
		tables = manager.getTablesForSchema(schema);
		mod.getTableNames().clear();
		for(Table tab : tables) {
			mod.getTableNames().add(tab.getTableName());
		}
		moduleService.saveOrUpdate(mod);
		/**
		 * End - Just making sure new tables added are reflected at run time.
		 */
		for(Table tab:tables) {
			map.put(tab.getTableName(), tab);
		}
			
		RequestContext.getCurrentInstance().update("reportForm:pType");
	}
	
	
	
	public void reload() throws SQLException {
	 FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ValueBinding binding = application.createValueBinding("#{userManagerNew}");
        UserManagerNew n = (UserManagerNew)binding.getValue(context);
        	if(n != null) {
        	String ulnbname = manager.getUlbName(n.getUlbId());
        	reports = service.getReportsByULB(ulnbname);
        	}
		//reports = service.getReportsWithNewVersion();
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	
	
	public List<String> getSelectedTables() {
		return selectedTables;
	}

	public void setSelectedTables(List<String> selectedTables) {
		this.selectedTables = selectedTables;
	}

	public void populateDetailsInSelectedTables() throws SQLException {
		
		process();
		RequestContext.getCurrentInstance().update("reportForm:selected");
	}
	
	private void process() throws SQLException {
		selectedTabs.clear();
		String schemaName = getModule().getSchemaName();
		getReport().setSchemaName(schemaName);
		for(String table : selectedTables) {
			Table t = map.get(table);
			t.setColumns(manager.getColumns2(table, schemaName));
			selectedTabs.add(t);
		}
	}

	public List<Table> getSelectedTabs() {
		return selectedTabs;
	}

	public void setSelectedTabs(List<Table> selectedTabs) {
		this.selectedTabs = selectedTabs;
	}
	
	
	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
	
	public void generateReport(String ext) {
		try {
				if(getReport().getReportName() == null || getReport().getReportName().trim().length() == 0) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Name can not be blank");
					RequestContext.getCurrentInstance().showMessageInDialog(msg);
					return;
				}
				
				if(getReport().getQuery() == null || getReport().getQuery().trim().length() == 0) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Query can not be blank");
					RequestContext.getCurrentInstance().showMessageInDialog(msg);
					return;
				}
				
				if(!validate()) {
					return;
				}
				generateReportFromOutside(getReport(), ext);
				
			
//			List<Column> aggregatedColumns = new ArrayList<Column>();
//			/**
//			 * Add columns of all tables in report
//			 */
//			for(Table table:getReport().getSelectedTablesAndColumns()) {
//				for(Column col : table.getColumns()) {
//					if(col.getWantedInReport()) {
//						aggregatedColumns.add(col);
//					}
//				}
//			}
//			
//			
//			if(report.getFromDate() != null && report.getToDate() != null && report.getDateRangeParam() != null) {
//				if(report.getFromDate().trim().length() != 0 && report.getToDate().trim().length()!=0) {
//					try {
//						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//						Date d = formatter.parse(report.getFromDate());
//						d = formatter.parse(report.getToDate());
//						ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
//						reportService.generateReportBetweenDateRange(getReport().getQuery(), aggregatedColumns, getReport().getReportName(), report.getFromDate(), report.getToDate(), ext, logoMapper, report.getUser());
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failure", "Improper Date Releated Data");
//						RequestContext.getCurrentInstance().showMessageInDialog(msg);
//						setQueryValidation("");
//						return;
//					}
//					
//				}
//				else {
//					ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
//					reportService.generateReport(getReport().getQuery(), aggregatedColumns, getReport().getReportName(), ext, logoMapper, report.getUser() );
//				}
//				
//				
//			}
//			else {
//				ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
//				reportService.generateReport(getReport().getQuery(), aggregatedColumns, getReport().getReportName(), ext, logoMapper, report.getUser() );
//			}
//			
//			getReport().setReportLink(confUtil.getDocumentsServerBaseUrl()+"/"+getReport().getReportName()+"."+ext);
//			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Go to "+getReport().getReportLink()+" to download the report");
//			RequestContext.getCurrentInstance().showMessageInDialog(msg);
//			setQueryValidation("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failure - "+e.getMessage(), "Report Generation failed");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
		}
	}
	
	/**
	 * This method is called from Reports.xhtml page. This has to be a stateless method and no state variable has to be impacted by this.
	 * @param report
	 */
	public void generateReportFromOutside(Report report, String ext) {
		//InputStream inputStream = null;
		 HttpServletResponse response = null;
		try {
			
			String user = getUserName();
			report.setUser(user);
			/**
			 * Neccessary because we store columns and tables json in report obj. so it is neccessary to marshal it back to object structure.
			 */
			if(report.getSelectedTablesAndColumnsJson() == null) {
				String jsonTablesAndColumns = mapper.writeValueAsString(getReport().getSelectedTablesAndColumns());
				getReport().setSelectedTablesAndColumnsJson(jsonTablesAndColumns);
				
			}
			if(report.getSelectedTablesJson() == null) {
				String jsonTables = mapper.writeValueAsString(getReport().getSelectedTables());
				getReport().setSelectedTablesJson(jsonTables);
			}
			List<Table> selTablesAndCols = mapper.readValue(report.getSelectedTablesAndColumnsJson(), mapper.getTypeFactory().constructCollectionType(List.class, Table.class));
			report.setSelectedTablesAndColumns(selTablesAndCols);
			
			List<String> selTables = mapper.readValue(report.getSelectedTablesJson(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
			report.setSelectedTables(selTables);
			
			List<Column> aggregatedColumns = new ArrayList<Column>();
			/**
			 * Add columns of all tables in report
			 */
			for(Table table:report.getSelectedTablesAndColumns()) {
			//	aggregatedColumns.addAll(table.getColumns());
				for(Column col : table.getColumns()) {
					if(col.getWantedInReport()) {
						aggregatedColumns.add(col);
					}
				}
			}
			ULBLogoMapper logoMapper = logoMapperService.getUniqueULBLogoMapper(report.getUlb());
			reportService.generateReport(report.getQuery(), aggregatedColumns, report.getReportName(), ext, logoMapper, report.getUser() );
			report.setReportLink(confUtil.getLocalDocumentsServerBaseUrl()+"/"+report.getReportName()+"."+ext);
//			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Go to "+report.getReportLink()+" to download the report");
//			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			
			String str = confUtil.getLocalDocumentsServerBaseUrl()+"/"+java.net.URLEncoder.encode(report.getReportName()+"."+ext,"UTF-8").replace("+", "%20");
			URL url = new URL(str);
			File file = new File(report.getReportName()+"."+ext);
			System.out.println("url is "+url);
			FileUtils.copyURLToFile(url, file);
			
			FileInputStream input = new FileInputStream(file);  
			System.out.println(input.available());
			response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setHeader("Content-Disposition", "attachment;filename="+report.getReportName()+"."+ext);
			response.setContentLength((int) input.available());
	        byte[] buffer = new byte[1024];  
	        ServletOutputStream out = null;  
	        out = response.getOutputStream(); 
	        int i = 0;  
	        while ((i = input.read(buffer)) != -1) {  
	        	out.write(buffer);  
	        	out.flush();  
	        }  
	        input.close();
	        out.close();
	        facesContext.responseComplete();
	        facesContext.renderResponse();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failure - "+e.getMessage(), "Report Generation  - "+report.getReportName()+" failed");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
		}
		
	}
	
	
	
	public boolean validate() {
		if(getReport().getReportName() == null || getReport().getReportName().trim().length() == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Name can not be blank");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			return false;
		}
		
		if(getReport().getQuery() == null || getReport().getQuery().trim().length() == 0) {
			setQueryValidation("Query Validation Failure");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Query can not be blank");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			return false;
		}
		
		String val = reportService.validate(getReport().getQuery());
		if(val.equalsIgnoreCase("Success")) {
			setQueryValidation("Query Validation Success");
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			return true;
		}
		else {
			setQueryValidation("Query Validation Failed -"+val);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", val);
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			return false;
		}
		
	}

	public String getQueryValidation() {
		return queryValidation;
	}

	public void setQueryValidation(String queryValidation) {
		this.queryValidation = queryValidation;
	}
	
	public String saveReport(String ext) throws JsonProcessingException, SQLException{
		if(getReport().getReportName() == null || getReport().getReportName().trim().length() == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Name can not be blank");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			return "";
		}
		
		String val = reportService.validate(getReport().getQuery());
		if(val.equalsIgnoreCase("Success")) {
			setQueryValidation("Query Validation Success");
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			getReport().setSelectedTables(getSelectedTables());
			cleanUpUnWantedColumns();
			String jsonTablesAndColumns = mapper.writeValueAsString(getReport().getSelectedTablesAndColumns());
			getReport().setSelectedTablesAndColumnsJson(jsonTablesAndColumns);
			
			String jsonTables = mapper.writeValueAsString(getReport().getSelectedTables());
			getReport().setSelectedTablesJson(jsonTables);
			
			String fromClauseJson = mapper.writeValueAsString(getReport().getFromClause());
			getReport().setFromClauseJson(fromClauseJson);
			String whereClauseJson = mapper.writeValueAsString(getReport().getWhereClause());
			getReport().setWhereClauseJson(whereClauseJson);
			
			//generateReport(ext);
			service.saveOrUpdate(getReport());
			setQueryValidation("");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Report Save Status", "Successful");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			
			
			return goback();
		}
		else {
			setQueryValidation("Query Validation Failed -"+val);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Report Save Status", "Failure - Possible reason: "+val);
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			RequestContext.getCurrentInstance().update("reportForm:valStatus");
			return "";
		}
		
	}
	
	private void cleanUpUnWantedColumns() {
		Iterator<Table> itrTable = report.getSelectedTablesAndColumns().iterator();
		while(itrTable.hasNext()) {
			Table table = itrTable.next();
			Iterator<Column> itrCol = table.getColumns().iterator();
				while(itrCol.hasNext()) {
					Column column = itrCol.next();
					if(!column.getWantedInReport()) {
						itrCol.remove();
					}
				}
		}
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
	
	private List<Table> clone(List<Table> selectedTables){
		Mapper mapper = new DozerBeanMapper();
		List<Table> selectedTables2 = new ArrayList<Table>();
		mapper.map(selectedTables, selectedTables2);
		return selectedTables2;
	}
	
	public String edit(Report report) throws SQLException, JsonParseException, JsonMappingException, IOException {
		
		
		this.report = report;
		fetchModules();
		String ulnbname = manager.getUlbName(getUlbId());
		getReport().setUlb(ulnbname);
		setEditReportName(false);
		List<Table> selTablesAndCols = mapper.readValue(report.getSelectedTablesAndColumnsJson(), mapper.getTypeFactory().constructCollectionType(List.class, Table.class));
		report.setSelectedTablesAndColumns(selTablesAndCols);
		
		List<String> selTables = mapper.readValue(report.getSelectedTablesJson(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
		report.setSelectedTables(selTables);
		List<String> l1 = new ArrayList<String>();
		l1.addAll(report.getSelectedTables());
		setSelectedTables(l1);
		
		List<Table> l2 = new ArrayList<Table>();
		
		l2.addAll(selTablesAndCols);
	/**
	 * Why having new list l2 - so that data at Reportmanager level and report level are distinct.
	 */
		//setSelectedTabs(l2);
		setSelectedTabs(clone(l2));
		FromClause fromClause = mapper.readValue(report.getFromClauseJson(), FromClause.class);
		report.setFromClause(fromClause);
		WhereClause whereClause = mapper.readValue(report.getWhereClauseJson(), WhereClause.class);
		report.setWhereClause(whereClause);
		
		
		//process();
		return "MainNew.xhtml?faces-redirect=false";
	}
	
	private Report fetchReport(String name) throws JsonParseException, JsonMappingException, IOException {
		Report rep = service.getUniqueReportByName(name);
		List<Table> selTablesAndCols = mapper.readValue(report.getSelectedTablesAndColumnsJson(), mapper.getTypeFactory().constructCollectionType(List.class, Table.class));
		rep.setSelectedTablesAndColumns(selTablesAndCols);
		
		List<String> selTables = mapper.readValue(report.getSelectedTablesJson(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
		rep.setSelectedTables(selTables);
		return rep;
	}
	
	public void delete(Report report) {
		service.delete(report.getId());
	
	}
	
	private void fetchModules() throws SQLException {
		 FacesContext context = FacesContext.getCurrentInstance();
	        Application application = context.getApplication();
	        ValueBinding binding = application.createValueBinding("#{userManagerNew}");
	        UserManagerNew n = (UserManagerNew)binding.getValue(context);
	        	if(n != null) {
	        		System.out.println(n.getUlbId());
	        		setUlbId(n.getUlbId());
	        		String ulnbname = manager.getUlbName(n.getUlbId());
	        		getReport().setUlb(ulnbname);
	        		getReport().setUser(n.getUser());
	        		loadAllModules();
	        	}
	}
	
	private String getUserName() throws SQLException {
		 FacesContext context = FacesContext.getCurrentInstance();
	        Application application = context.getApplication();
	        ValueBinding binding = application.createValueBinding("#{userManagerNew}");
	        UserManagerNew n = (UserManagerNew)binding.getValue(context);
	        	if(n != null) {
	        	  return n.getUser();
	        	}
	        return "Anonymous";
	}
	
	public String createNew() throws SQLException {
		setReport(new Report());
		fetchModules();
		
		getReport().setNewVersion(true);
		String ulnbname = manager.getUlbName(getUlbId());
		getReport().setUlb(ulnbname);
		setEditReportName(true);
		setSelectedTabs(new ArrayList<Table>());
		setSelectedTables(new ArrayList<String>());
		
		return "MainNew.xhtml?faces-redirect=true";
	}

	public Boolean getEditReportName() {
		return editReportName;
	}

	public void setEditReportName(Boolean editReportName) {
		this.editReportName = editReportName;
	}
	
	public String goback() {
		setQueryValidation("");
		return "ReportsNew.xhtml?faces-redirect=false";
	}

	
	
	

	public UserManagerNew getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManagerNew userManager) {
		this.userManager = userManager;
		setUlbId(userManager.getUlbId());
		moduleNames = moduleService.getModules(ulbId);
	}

	public String[] getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(String[] dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	public void updateQuery() {
		if(report.getFromDate()!= null && report.getToDate()!= null && report.getDateRangeParam() != null  ) {
			if(report.getDateRangeParam().trim().length() !=0 ) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				try {
					Date d = formatter.parse(report.getFromDate());
					d = formatter.parse(report.getToDate());
					String fromDt = "TO_DATE('"+report.getFromDate()+"','dd-MM-yyyy')";
					String toDt = "TO_DATE('"+report.getToDate()+"','dd-MM-yyyy')";
					
					String qry = report.getQuery();
					if( qry.toLowerCase().contains("group by")) {
						if(qry.toLowerCase().contains("where")) {
							qry = qry.replace("group by", " AND "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"   group by");
						}
						else {
							qry = qry.replace("group by", " where "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"   group by");
						}
						
					}
					else {
						if(qry.toLowerCase().contains("where")) {
							qry +=  " AND "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"";
						}
						else {
							qry +=  " where "+report.getDateRangeParam()+" between "+fromDt+" AND "+toDt+"";
						}
						
					}
				report.setQuery(qry);
				RequestContext.getCurrentInstance().update("reportForm:queryBox");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status", "Invalid Date Formats");
					RequestContext.getCurrentInstance().showMessageInDialog(msg);
					
				}
				
				
			}
			
		}
		else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Status", "In Complete Details. Enter from/to dates and date parameter");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
		}
	}
	
	public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/loginNew.xhtml?faces-redirect=true";
    }

	public List<String> getUlbnames() {
		return ulbnames;
	}

	public void setUlbnames(List<String> ulbnames) {
		this.ulbnames = ulbnames;
	}
	
	
	
	public void selectAllColumnsForTable(String tableName) {
		for(Table tab : getSelectedTabs()) {
			
			if(tab.getTableName().equalsIgnoreCase(tableName)) {
				for(Column column : tab.getColumns()) {
					column.setWantedInReport(true);
				}
			}
				
			
		}
		RequestContext.getCurrentInstance().update("reportForm:selected");
	}
	
	public void deselectAllColumnsForTable(String tableName) {
		for(Table tab : getSelectedTabs()) {
			if(tab.getTableName().equalsIgnoreCase(tableName)) {
				for(Column column : tab.getColumns()) {
					column.setWantedInReport(false);
				}
			}
			
		
		}
		RequestContext.getCurrentInstance().update("reportForm:selected");
	}
	
	public void gotoQueryBuilder() throws SQLException {
		getReport().getSelectedTablesAndColumns().clear();
		for(Table tab : getSelectedTabs()) {
			List<Column> selectedForQry = new ArrayList<Column>();
			Table t = new Table();
			t.setTableName(tab.getTableName());
				for(Column column : tab.getColumns()) {
					if(column.getWantedInReport()) {
						selectedForQry.add(column);
					}
				}
			t.setColumns(selectedForQry);
			getReport().getSelectedTablesAndColumns().add(t);
		}
		
		
		RequestContext.getCurrentInstance().update("reportForm:selected");
	}
	
	public String showReportBuilder() throws SQLException {
//		Map<String,Object> options = new HashMap<String, Object>();
//		 options.put("draggable", false);
//         options.put("modal", true);
//         options.put("position", "top"); // <--- not work
//         options.put("width", "90%");
//         options.put("contentWidth", "90%");
//         options.put("height", "500");
//         options.put("contentheight", "90%");
//         options.put("size", "auto");
//         getReport().setSelectedTablesAndColumns(selectedTabs);
//         queryBuilder = new QueryBuilder(getReport());
//        Map<String, List<String>> params = new HashMap<String, List<String>>();
//
//        RequestContext.getCurrentInstance().openDialog("queryBuilder",options, params);
			if(getReport().getId() == null ) {
				getReport().setSelectedTablesAndColumns(selectedTabs);
				getReport().setFromClause(null);
				getReport().setWhereClause(null);
				queryBuilder = new QueryBuilder(getReport());
				return "/queryBuilder.xhtml?faces-redirect=true";
			}
		
			if(canUpdate()) {
				getReport().setSelectedTablesAndColumns(selectedTabs);
				//getReport().setFromClause(null);
				//getReport().setWhereClause(null);
				queryBuilder = new QueryBuilder(getReport());
				return "/queryBuilder.xhtml?faces-redirect=true";
			}
		
      return null;
	}
	
	private boolean canUpdate() {
		Report temp = null;
		try {
			temp = fetchReport(getReport().getReportName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Table tab : selectedTabs) {
			if( getReport().getId() > 0) {
				//if(!getReport().getSelectedTablesAndColumns().contains(tab)) {
				if(!temp.getSelectedTablesAndColumns().contains(tab)) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong Update", "Can not change the query structure for the report already created."+tab.getTableName() +" was not a part of saved report");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
					return false;
				}
				//report saved earlier.
				
				//for(Table t1 : getReport().getSelectedTablesAndColumns()) {
				for(Table t1 : temp.getSelectedTablesAndColumns()) {
					if(tab.getTableName().equalsIgnoreCase(t1.getTableName())) {
						for(Column col : tab.getColumns()) {
							for(Column c : t1.getColumns()) {
								if(col.getField().equalsIgnoreCase(c.getField()) ) {
									if( col.getWantedInReport() != c.getWantedInReport()) {
									FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong Update", "Can not change the query structure for the report already created. Problem column "+c.getField());
									RequestContext.getCurrentInstance().showMessageInDialog(message);
									return false;
									}
								}
							}
						}
					}
					
					
				}
				
				
			}
		}
		return true;
	}

	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public void setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getUlbId() {
		return ulbId;
	}

	public void setUlbId(Integer ulbId) {
		this.ulbId = ulbId;
	}

	public List<Module> getModuleNames() {
		return moduleNames;
	}

	public void setModuleNames(List<Module> moduleNames) {
		this.moduleNames = moduleNames;
	}

	public void setModule(Module module) {
		this.module = module;
	}
	
	
	
}

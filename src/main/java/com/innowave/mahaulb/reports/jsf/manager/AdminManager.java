package com.innowave.mahaulb.reports.jsf.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Service;

import com.innowave.mahaulb.reports.data.Module;
import com.innowave.mahaulb.reports.data.Table;
import com.innowave.mahaulb.reports.data.Ulb;
import com.innowave.mahaulb.reports.manager.Manager;
import com.innowave.mahaulb.reports.services.ModuleService;
import com.innowave.mahaulb.reports.util.ReportException;

@ManagedBean(name = "adminManager", eager = true)
@SessionScoped
@Service
public class AdminManager {
	transient ModuleService moduleService;

	 List<Module> modules ;
	 
	 Module module = new Module();
	 Integer ulbId;
	 
	 Manager  manager;
	 
	 List<Ulb> ulbs;
	 
	 List<String> tables = new ArrayList<String>();
	 
	 List<String> schemas = new ArrayList<String>();
	 
	 @PostConstruct
	 public void init() throws SQLException {
		 moduleService = SpringUtil.getService(ModuleService.class);
		 manager = SpringUtil.getService(Manager.class);
		 ulbs = manager.getAllULBs();
		 
		 List<Table> tbs = manager.getTablesForSchema("receipt");
		 for(Table t : tbs) {
			 tables.add(t.getTableName());
		 }
		 
		 schemas = manager.getAllSchemas();
	 }
	 
	 public void refresh() {
		 schemas = manager.getAllSchemas();
		 RequestContext.getCurrentInstance().update("responseForm:schemaid");
	 }
	 
	 public String showAllModules() {
		 modules = moduleService.findAll();
		// Collections.sort(modules, (s1, s2) -> s1.UlbName.compareTo(s2.UlbName);
		 Collections.sort(modules, (p1, p2) -> p1.getUlbName().compareTo(p2.getUlbName()));
		 return "modules.xhtml?redirect=true";
	 }
	 
	 public String showModules(Integer ulbId) {
		 modules = moduleService.getModules(ulbId);
		 setUlbId(ulbId);
			return "modules.xhtml?redirect=true";
		}
	 
	 public String createModule() {
		 this.module = new Module();
		 this.module.setUlbId(getUlbId());
		 return "moduleSchema.xhtml?redirect=true";
	 }
	 
	 public String editModule(Module module) {
		 this.module = new Module();
		 Mapper mapper = new DozerBeanMapper();
		 mapper.map(module, this.module);
		 return "moduleSchema.xhtml?redirect=true";
	 }
	 
	 public String deleteModule(Module module) {
		 moduleService.delete(module.getId());
//		 this.modules = moduleService.getModules(ulbId);
//		 return "modules.xhtml?redirect=true";
		 return showAllModules();
		 
	 }
	 
	 public String save() {
		this.module.setUlbName(getUlbName(this.module.getUlbId())); 
		List<Table> tables = manager.getTablesForSchema(this.module.getSchemaName());
			for(Table tab : tables) {
				this.module.getTableNames().add(tab.getTableName());
			}
		 moduleService.saveOrUpdate(this.module);
//		 this.modules = moduleService.getModules(ulbId);
//		 return "modules.xhtml?redirect=true";
		 return showAllModules();
	 }
	 
	 String getUlbName(int id) {
		 for(Ulb u : ulbs) {
			 if(u.getUlbId() == id) {
				 return u.getUlbName();
			 }
		 }
		 throw new ReportException("Report Name not Found");
	 }

	public ModuleService getModuleService() {
		return moduleService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
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

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	 
	 
}

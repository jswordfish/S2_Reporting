package com.innowave.mahaulb.reports.jsf.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Transient;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Service;

import com.innowave.mahaulb.reports.data.ColumnValue;
import com.innowave.mahaulb.reports.data.FromClause;
import com.innowave.mahaulb.reports.data.FromFieldFunction;
import com.innowave.mahaulb.reports.data.Function;
import com.innowave.mahaulb.reports.data.Group;
import com.innowave.mahaulb.reports.data.JoinType;
import com.innowave.mahaulb.reports.data.LogicalOperator;
import com.innowave.mahaulb.reports.data.Operator;
import com.innowave.mahaulb.reports.data.Report;
import com.innowave.mahaulb.reports.data.Rule;
import com.innowave.mahaulb.reports.data.Table;
import com.innowave.mahaulb.reports.data.WhereClause;
import com.innowave.mahaulb.reports.manager.Column;
import com.innowave.mahaulb.reports.manager.Manager;
import com.innowave.mahaulb.reports.manager.ReportFromDb;
import com.innowave.mahaulb.reports.util.ConfUtil;

public class QueryBuilder {
	
	
	
	Report report;
	
	String query = "";
	
	WhereClause whereClause = null;
	
	FromClause fromClause = new FromClause();
	
	
	
	List<Column> toFields = new ArrayList<Column>();
	
	
	transient String toFieldAdded ;
	
	
	 
	transient Manager manager;
	
	transient Map<String, Column> map = new HashMap<String, Column>();
	
	@Transient
	ReportFromDb reportService;
	
	transient List<Table> selectedTableAndColumns  = new ArrayList<Table>();
	
	transient ConfUtil confUtil;
	
	public QueryBuilder( Report report) throws SQLException {
		
		this.report = report;
		reportService = SpringUtil.getService(ReportFromDb.class);
		confUtil = SpringUtil.getService(ConfUtil.class);
		init();
	}
	
	public void processJoinType(String joinType) {
		getFromClause().setJoinType(JoinType.valueOf(joinType));
		generateAllQuery();
	}
	
	public void generateFromClauseQuery() {
		List<FromFieldFunction> fieldFunctions = fromClause.getFieldFunctions();
		String from = "select ";
		for(FromFieldFunction fieldFunction :  fieldFunctions) {
			if(fieldFunction.getFunction().getFunction().equalsIgnoreCase("NONE")) {
				from += fieldFunction.getColumn().getField()+" AS \""+fieldFunction.getColumn().getTitle()+"\", ";
			}
			else {
				if(fieldFunction.getFunction().getFunction().equals(Function.LPAD.getFunction()) || fieldFunction.getFunction().getFunction().equals(Function.RPAD.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					String v[] = val.split(",");
						if(v.length == 2) {
							val = v[0] +", '"+v[1]+"'";
						}
					
					from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+"::text, "+val +") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.MOD.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					from += ""+"("+fieldFunction.getColumn().getField()+" % "+val +") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.ROUND.getFunction()) || fieldFunction.getFunction().getFunction().equals(Function.TRUNC.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					String value = "";
					if(val != null && val.trim().length() != 0 ) {
						from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+"::numeric, "+val +") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
					}
					else {
						from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
					}
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.TO_CHAR.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					String value = "";
					from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+", '"+val +"') AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.TO_NUMBER.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					String value = "";
					from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+"::text, '"+val +"') AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.POSITION.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					String value = "";
					from += ""+fieldFunction.getFunction().getFunction()+"('"+val +"' IN "+fieldFunction.getColumn().getField()+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.SUBSTRING.getFunction()) || fieldFunction.getFunction().getFunction().equals(Function.COALESCE.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					
					from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+", "+val+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.ADDITION.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					
					from += "("+fieldFunction.getColumn().getField()+" + "+val+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.SUBSTRACTION.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					
					from += "("+fieldFunction.getColumn().getField()+" - "+val+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.MULTIPLICATION.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					
					from += "("+fieldFunction.getColumn().getField()+" * "+val+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.DIVISION.getFunction())) {
					String val = fieldFunction.getFunctionValue();
					
					from += "("+fieldFunction.getColumn().getField()+" / "+val+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else if(fieldFunction.getFunction().getFunction().equals(Function.CHR.getFunction())) {
					//need to change the datatype of column from integer to string as this function changes the datatype after being applied.
					for(Table table : getReport().getSelectedTablesAndColumns()) {
						for(Column column : table.getColumns()) {
							if(column.getWantedInReport() && fieldFunction.getColumn().getField().contains(column.getField())) {
								column.setDataType("string");
							}
						}
					}
					from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				else {
					from += ""+fieldFunction.getFunction().getFunction()+"("+fieldFunction.getColumn().getField()+") AS \""+fieldFunction.getColumn().getTitle()+"\", ";
				}
				
			}
			
		}
		from = from.substring(0, from.lastIndexOf(","));
		from += " from ";
		int count = 0;
		Boolean join = true;
		for(Table table : selectedTableAndColumns)	{
			
			String split[] = table.getTableName().split("\\.");
			split[1] = split[1].substring(0, split[1].indexOf(" "));
			String name="\""+split[0]+"\".\""+split[1]+"\"";
			String alias = table.getTableName().substring(table.getTableName().indexOf(" ")+1, table.getTableName().length());
			//from += table.getTableName() +", ";
			
			if(count < selectedTableAndColumns.size() - 1) {
				if(getFromClause().getJoinType().getType().equalsIgnoreCase(JoinType.NONE.getType())) {
					from += name +" "+alias+", ";
					join = false;
				}
				else if(getFromClause().getJoinType().getType().equalsIgnoreCase(JoinType.JOIN.getType())) {
					from += name +" "+alias+" JOIN ";
				}
				else if(getFromClause().getJoinType().getType().equalsIgnoreCase(JoinType.LEFT_JOIN.getType())) {
					from += name +" "+alias+" LEFT JOIN ";
				}
				else if(getFromClause().getJoinType().getType().equalsIgnoreCase(JoinType.RIGHT_JOIN.getType())) {
					from += name +" "+alias+" RIGHT JOIN ";
				}
				else if(getFromClause().getJoinType().getType().equalsIgnoreCase(JoinType.FULL_JOIN.getType())) {
					from += name +" "+alias+" FULL JOIN ";
				}
				else if(getFromClause().getJoinType().getType().equalsIgnoreCase(JoinType.CROSS_JOIN.getType())) {
					from += name +" "+alias+" CROSS JOIN ";
				}
			}
			else {
				if(!join) {
					from += name +" "+alias;
					query = from +" where ";
				}
				else {
					if(selectedTableAndColumns.size() > 1) {
						from += name +" "+alias;
						query = from +" ON ";
					}
					else {
						from += name +" "+alias;
						query = from +" where ";
					}
					
				}
			}
			count++;
		}
		//from  = from.substring(0, from.lastIndexOf(","));
		
		setAliasToReportFields();
	}
	
	private void setAliasToReportFields(){
		List<FromFieldFunction> fieldFunctions = fromClause.getFieldFunctions();
		List<Table> tables = report.getSelectedTablesAndColumns();
		Map<String, FromFieldFunction> mp = new HashMap<String, FromFieldFunction>();
		for(FromFieldFunction fieldFunction  : fieldFunctions) {
			mp.put(fieldFunction.getColumn().getField().substring(fieldFunction.getColumn().getField().indexOf(".")+1, fieldFunction.getColumn().getField().length()), fieldFunction);
		}
		
		for(Table table : tables) {
			for(Column column : table.getColumns()) {
					if(column.getWantedInReport()) {
						column.setTitle(mp.get(column.getField()).getColumn().getTitle());
					}
				
			}
		}
		
	}
	
	public void generateAllQuery() {
		generateFromClauseQuery();
		generateWhereQuery();
		RequestContext.getCurrentInstance().update("reportQueryForm:queryBox");
	}
	
	public void init() throws SQLException {
		
		manager = SpringUtil.getService(Manager.class);
		
		
		toFields = new ArrayList<Column>();
		selectedTableAndColumns = clone(report.getSelectedTablesAndColumns());
		if(report.getFromClause() != null && report.getFromClause().getFieldFunctions().size() != 0) {
			this.fromClause = report.getFromClause();
			setUpTableAliases();
			generateFromClauseQuery();
			
		}
		else {
			this.fromClause = new FromClause();
			this.report.setFromClause(fromClause);
			query = "select ";
			int count = 0;
			String fromLater = " from ";
			String fromEarlier = "select ";
			
			for(Table table : selectedTableAndColumns)	{
				String alias = "a"+count;
				//table.setTableName(confUtil.getDatabaseSchema()+"."+table.getTableName()+" "+alias);
				//table.setTableName("\""+getReport().getSchemaName()+"\".\""+table.getTableName()+"\" "+alias);
				table.setTableName(getReport().getSchemaName()+"."+table.getTableName()+" "+alias);
				//fromLater += table.getTableName() +", ";
				fromLater += "\""+getReport().getSchemaName()+"\".\""+table.getTableName()+"\" "+alias +", ";
				count++;
				for(Column column : table.getColumns()) {
					if(column.getWantedInReport()) {
						column.setField(alias+"."+column.getField());
						fromEarlier += column.getField() +" AS "+column.getTitle()+", ";
						
						FromFieldFunction fieldFunction = new FromFieldFunction(column, Function.NONE);
						fromClause.getFieldFunctions().add(fieldFunction);
					}
					
				}
			}
			fromLater = fromLater.substring(0, fromLater.lastIndexOf(","));
			fromEarlier = fromEarlier.substring(0,fromEarlier.lastIndexOf(","));
			query = fromEarlier + fromLater+" where ";
			
		}
		
		if(report.getWhereClause() != null && report.getWhereClause().getGroups().size() != 0) {
			this.whereClause = report.getWhereClause();
			generateWhereQuery();//when you try to open up an existing report.
		}
		else {
			
			if(report.getWhereClause() == null) {
				this.whereClause = new WhereClause();
				report.setWhereClause(this.whereClause);
			}
			else {
				//if there is  no group  added but just group clauses added then in report edit mode you may come here.
				this.whereClause = report.getWhereClause();
				generateWhereQuery();
			}
			
			
			
			
		}
		
		setUpToFields();	
		
	}
	
	private void setUpTableAliases() {
		int count = 0;
		for(Table table : selectedTableAndColumns)	{
			String alias = "a"+count;
			//table.setTableName(confUtil.getDatabaseSchema()+"."+table.getTableName()+" "+alias);
			table.setTableName(getReport().getSchemaName()+"."+table.getTableName()+" "+alias);
			count++;
			
		}
	}
	
	private void setUpToFields() throws SQLException {
		map.clear();
		
		for(Table table : selectedTableAndColumns)	{
			String split[] = table.getTableName().split("\\.");
			split[1] = split[1].substring(0, split[1].indexOf(" "));
			String name="\""+split[0]+"\".\""+split[1]+"\"";
			//String name=""+split[0]+"\".\""+split[1]+"\"";
			List<Column> columns = manager.getColumns2(name);
			table.setColumns(columns);
		
			String alias = table.getTableName().substring(table.getTableName().indexOf(" ")+1, table.getTableName().length());
			for(Column column : table.getColumns()) {
				column.setField(alias+"."+column.getField());
				toFields.add(column);
				map.put(column.getField(), column);
			}
		}
	}

	private Column getColumn(String columnName) {
		return map.get(columnName);
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
	
	public void setUpTablesWithAliases() {
		
	}
	
	
	
	private List<Table> clone(List<Table> selectedTables){
		Mapper mapper = new DozerBeanMapper();
		List<Table> selectedTables2 = new ArrayList<Table>();
		mapper.map(selectedTables, selectedTables2);
		return selectedTables2;
	}



	
	
	public String getToFieldAdded() {
		return toFieldAdded;
	}



	public void setToFieldAdded(String toFieldAdded) {
		this.toFieldAdded = toFieldAdded;
	}



	public void addRule(Group group, boolean mappingRule) {
		if(getToFieldAdded() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doesn't work without selecting a field", "Select field from the dropdown");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			
			return;
		}
		Rule rule = new Rule();
		ColumnValue columnValue = new ColumnValue();
		columnValue.setMapFromOtherTable(mappingRule);
		Column column = map.get(getToFieldAdded());
		columnValue.setColumn(column);
		rule.setColumnValue(columnValue);
		group.getRules().add(rule);
		setToFieldAdded("");
		RequestContext.getCurrentInstance().update("reportQueryForm:groups");
	}
	
	public void deleteRule(Group group, Rule rule) {
		group.getRules().remove(rule);
		RequestContext.getCurrentInstance().update("reportQueryForm:groups");
	}

	
	public void addGroup() {
		getWhereClause().getGroups().add(new Group());
		RequestContext.getCurrentInstance().update("reportQueryForm:groups");
	}
	
	public void deleteGroup(Group group) {
		getWhereClause().getGroups().remove(group);
		RequestContext.getCurrentInstance().update("reportQueryForm:groups");
	}


	public String getQuery() {
		return query;
	}



	public void setQuery(String query) {
		this.query = query;
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



	public List<Column> getToFields() {
		return toFields;
	}



	public void setToFields(List<Column> toFields) {
		this.toFields = toFields;
	}
	
	
	public Function[] getFunctions() {
        return Function.values();
    }
	
	public Operator[] getOperators() {
		return Operator.values();
	}
	
	public LogicalOperator[] getLogicalOperators() {
		return LogicalOperator.values();
	}
	
	public void generateWhereQuery() {
//			if(getFromClause().getJoinType().getType().equalsIgnoreCase(JoinType.NONE.getType())) {
//				this.query = this.query.substring(0, this.query.indexOf("where"));
//			}
//			else {
//				this.query = this.query.substring(0, this.query.indexOf("ON"));
//			}
//		
//		this.query += " where ";
		List<Group> groups = whereClause.getGroups();
		String query = "";
		for(Group group : groups) {
			query += "(";
			List<Rule> rules = group.getRules();
			for(Rule rule : rules) {
				query += "(";
					if(rule.getOperator().getOperator().equals(Operator.IS_FALSE.getOperator()) || rule.getOperator().getOperator().equals(Operator.IS_TRUE.getOperator()) || rule.getOperator().getOperator().equals(Operator.IS_NOT_NULL.getOperator()) || rule.getOperator().getOperator().equals(Operator.IS_NULL.getOperator())) {
						query += rule.getColumnValue().getColumn().getField()+" "+rule.getOperator().getOperator();
						query += ")";
					}
					else {
						query += rule.getColumnValue().getColumn().getField()+" "+rule.getOperator().getOperator()+" "+rule.getColumnValue().getValue();
						query += ")";
					}
				
				if(rule.getLogicalOperator().getOperator().equalsIgnoreCase(LogicalOperator.NOT_APPLICABLE.getOperator())) {
					
					break;
				}
				else {
					query += " "+rule.getLogicalOperator().getOperator() +" ";
				}
			}
			query += ")";
			if(group.getLogicalOperator().getOperator().equalsIgnoreCase(LogicalOperator.NOT_APPLICABLE.getOperator())) {
				break;
			}
			else {
				query += " "+group.getLogicalOperator().getOperator() +" ";
			}
		}
		this.query  = this.query +" "+query;
			if(whereClause.getGroupby() != null && !whereClause.getGroupby().equalsIgnoreCase("None")) {
				this.query  = this.query +" group by "+whereClause.getGroupby();
				
				if(whereClause.getGroupBy2() != null && !whereClause.getGroupBy2().equalsIgnoreCase("None")) {
					this.query  = this.query +", "+whereClause.getGroupBy2();
					
					if(whereClause.getGroupBy3() != null && !whereClause.getGroupBy3().equalsIgnoreCase("None")) {
						this.query  = this.query +", "+whereClause.getGroupBy3();
						
						if(whereClause.getGroupBy4() != null && !whereClause.getGroupBy4().equalsIgnoreCase("None")) {
							this.query  = this.query +", "+whereClause.getGroupBy4();
							
							if(whereClause.getGroupBy5() != null && !whereClause.getGroupBy5().equalsIgnoreCase("None")) {
								this.query  = this.query +", "+whereClause.getGroupBy5();
								
								
							}
							
						}
						
					}
				}
			}
			
			if(whereClause.getOrderby() != null && !whereClause.getOrderby().equalsIgnoreCase("None")) {
				this.query  = this.query +" order by "+whereClause.getOrderby();
				
				if(whereClause.getOrderby2() != null && !whereClause.getOrderby2().equalsIgnoreCase("None")) {
					this.query  = this.query +", "+whereClause.getOrderby2();
					
					if(whereClause.getOrderby3() != null && !whereClause.getOrderby3().equalsIgnoreCase("None")) {
						this.query  = this.query +", "+whereClause.getOrderby3();
						
						if(whereClause.getOrderby4() != null && !whereClause.getOrderby4().equalsIgnoreCase("None")) {
							this.query  = this.query +", "+whereClause.getOrderby4();
							
							if(whereClause.getOrderby5() != null && !whereClause.getOrderby5().equalsIgnoreCase("None")) {
								this.query  = this.query +", "+whereClause.getOrderby5();
								
								
							}
							
						}
						
					}
				}
			}
			
			if(whereClause.getHaving() != null && whereClause.getHaving().trim().length() != 0) {
				this.query  = this.query +" HAVING ("+whereClause.getHaving()+") ";
			}
			
			if(whereClause.getLimit() != null && whereClause.getLimit().trim().length() != 0) {
				this.query  = this.query +" LIMIT "+whereClause.getLimit();
			}
		
	}
	
	public boolean validate() {
		if(getQuery() == null || getQuery().trim().length() == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", "Report Query can not be blank");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			return false;
		}
		
		String val = reportService.validate(getQuery());
		if(val.equalsIgnoreCase("Success")) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Status", "Query Successfully Validated");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			this.report.setQuery(getQuery());
			return true;
		}
		else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation Failed", val);
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			return false;
		}
		
	}
	
	public String save() {
		validate();
		this.report.setWhereClause(whereClause);
		this.report.setFromClause(fromClause);
		this.report.setQuery(query);
		 return "/MainNew.xhtml?faces-redirect=true";
	}
}

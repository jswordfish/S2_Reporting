package com.innowave.mahaulb.reports.data;

import java.util.ArrayList;
import java.util.List;

public class FromClause {
	
	
	List<FromFieldFunction> fieldFunctions = new ArrayList<FromFieldFunction>();
	
	String fromQuery;

	JoinType joinType = JoinType.NONE;

	public String getFromQuery() {
		return fromQuery;
	}

	public void setFromQuery(String fromQuery) {
		this.fromQuery = fromQuery;
	}

	public List<FromFieldFunction> getFieldFunctions() {
		return fieldFunctions;
	}

	public void setFieldFunctions(List<FromFieldFunction> fieldFunctions) {
		this.fieldFunctions = fieldFunctions;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}
	
	

}

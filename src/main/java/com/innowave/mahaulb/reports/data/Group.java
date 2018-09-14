package com.innowave.mahaulb.reports.data;

import java.util.ArrayList;
import java.util.List;

public class Group {
	
	List<Rule> rules = new ArrayList<Rule>();
	
	LogicalOperator logicalOperator = LogicalOperator.NOT_APPLICABLE;

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(LogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	
	

}

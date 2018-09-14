package com.innowave.mahaulb.reports.data;

public enum LogicalOperator {

AND("AND"), OR("OR"), NOT_APPLICABLE("NOT_APPLICABLE");
	
	private String operator;
	
	private LogicalOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}
}

package com.innowave.mahaulb.reports.data;

public enum Operator {
	
	EQUALS("="), NOT_EQUALS("!="),  GREATER_THAN(">"), LESS_THAN("<"), 
	IS_NULL("IS NULL"), IS_NOT_NULL("IS NOT NULL"),
	LESS_THAN_EQUAL_TO("<="), GREATER_THAN_EQUAL_TO(">="),
	NOT_EQUAL_TO("<>"), IS_TRUE("IS TRUE"), IS_FALSE("IS FALSE");
	
	private String operator;
	
	private Operator(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}
	
	

}

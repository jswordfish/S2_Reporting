package com.innowave.mahaulb.reports.data;

public class Rule {
	
	ColumnValue columnValue;
	
	Operator operator = Operator.EQUALS;
	
	LogicalOperator logicalOperator = LogicalOperator.NOT_APPLICABLE;
	
	

	public ColumnValue getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(ColumnValue columnValue) {
		this.columnValue = columnValue;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(LogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	
	
	
	
}

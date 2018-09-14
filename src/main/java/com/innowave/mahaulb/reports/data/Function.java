package com.innowave.mahaulb.reports.data;

public enum Function {
NONE("NONE"), SUM("SUM"), AVERAGE("AVERAGE"), COUNT("COUNT"), DISTINCT("DISTINCT"), MAX("MAX"), MIN("MIN"), LOWER("LOWER"), UPPER("UPPER"), TRIM("TRIM"),
INITCAP("INITCAP"), ASCII("ASCII"), LENGTH("LENGTH"), ABS("ABS"), CEIL("CEIL"), FLOOR("FLOOR"),
LTRIM("LTRIM"), RTRIM("RTRIM"), LPAD("LPAD"), RPAD("RPAD"), MOD("MOD"), ROUND("ROUND"), TRUNC("TRUNC"),
TO_CHAR("TO_CHAR"), TO_NUMBER("TO_NUMBER"), POSITION("POSITION"), SUBSTRING("SUBSTRING"), COALESCE("COALESCE"), ADDITION("ADDITION"),
SUBSTRACTION("SUBSTRACTION"), MULTIPLICATION("MULTIPLICATION"), DIVISION("DIVISION"), CHR("CHR");
	
	private String function;
	
	private Function(String operator) {
		this.function = operator;
	}

	public String getFunction() {
		return function;
	}
	
}

package com.innowave.mahaulb.reports.util;

public class ReportException extends RuntimeException {

	public ReportException(){
		super();
	}
	
	public ReportException(String message){
		super(message);
	}
	
	public ReportException(String message, Throwable t){
		super(message, t);
	}
}

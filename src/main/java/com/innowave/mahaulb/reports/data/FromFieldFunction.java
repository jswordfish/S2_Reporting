package com.innowave.mahaulb.reports.data;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.Transient;

import com.innowave.mahaulb.reports.manager.Column;

public class FromFieldFunction {
Column column;

Function function = Function.NONE;

String functionValue =  "";

Boolean functionValueEdit = false;
@Transient
String placeholder;

	public FromFieldFunction() {
		
	}
	
	public FromFieldFunction(Column column, Function function) {
		this.column = column;
		this.function = function;
	}

	public Column getColumn() {
		return column;
	}
	
	public void setColumn(Column column) {
		this.column = column;
	}
	
	public Function getFunction() {
		return function;
	}
	
	public void setFunction(Function function) {
		this.function = function;
		if(function.getFunction().equalsIgnoreCase(Function.CHR.getFunction())) {
			getColumn().setDataType("string");
		}
	}

	public String getFunctionValue() {
		return functionValue;
	}

	public void setFunctionValue(String functionValue) {
		this.functionValue = functionValue;
	}
	
	

	public Boolean getFunctionValueEdit() {
		if(getFunction().getFunction().equals(Function.LPAD.getFunction()) || getFunction().getFunction().equals(Function.RPAD.getFunction()) || getFunction().getFunction().equals(Function.MOD.getFunction())) {
			functionValueEdit = true;
			
		}
		else if( getFunction().getFunction().equals(Function.ROUND.getFunction()) || getFunction().getFunction().equals(Function.TRUNC.getFunction()) ) {
			functionValueEdit = true;
		}
		else if(getFunction().getFunction().equals(Function.TO_CHAR.getFunction()) || getFunction().getFunction().equals(Function.TO_NUMBER.getFunction())) {
			functionValueEdit = true;
		}
		else if(getFunction().getFunction().equals(Function.POSITION.getFunction()) || getFunction().getFunction().equals(Function.SUBSTRING.getFunction())) {
			functionValueEdit = true;
		}
		else if(getFunction().getFunction().equals(Function.COALESCE.getFunction()) || getFunction().getFunction().equals(Function.ADDITION.getFunction())) {
			functionValueEdit = true;
		}
		else if(getFunction().getFunction().equals(Function.SUBSTRACTION.getFunction()) || getFunction().getFunction().equals(Function.DIVISION.getFunction())) {
			functionValueEdit = true;
		}
		else if(getFunction().getFunction().equals(Function.MULTIPLICATION.getFunction()) ) {
			functionValueEdit = true;
		}
		else {
			functionValueEdit = false;
		}
		return functionValueEdit;
	}

	public void setFunctionValueEdit(Boolean functionValueEdit) {
		this.functionValueEdit = functionValueEdit;
	}

	 public void valueChanged(ValueChangeEvent event) {
	        Function oldValue = (Function) event.getOldValue();
	        Function newValue = (Function) event.getNewValue();
	     System.out.println(oldValue);
	     setFunction(newValue);
	        // save new value to DB
	        // ...
	    }

	public String getPlaceholder() {
		return (getFunctionValueEdit() == true)?"Enter values  for "+getFunction().getFunction() : "Not Applicable";
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	 
	 

}

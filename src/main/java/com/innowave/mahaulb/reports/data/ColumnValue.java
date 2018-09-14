package com.innowave.mahaulb.reports.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.innowave.mahaulb.reports.manager.Column;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnValue {
	
	Column column;
	
	String value;
	
	boolean mapFromOtherTable = false;

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public String getValue() {
		if(getColumn().getDataType().equalsIgnoreCase("string")) {
			if(value != null && !value.contains("'") && !isMapFromOtherTable() ) {
				return "'"+value+"'";
			}
			
		}
		
		if(getColumn().getDataType() != null) {
			if(getColumn().getDataType().startsWith("date")) {
				if(value != null && value.contains("TO_DATE('")) {
					return value;
				}
				else if(value == null) {
					return "";
				}
				else {
					return "TO_DATE('"+value+"','YYYY-MM-DD')";
				}
				
			}
		}
		
		return value;
	}

	public void setValue(String value) {
//		if(getColumn().getDataType() != null) {
//			if(getColumn().getDataType().startsWith("date")) {
//				value = "TO_DATE('"+value+"','YYYY-MM-DD')";
//			}
//		}
		
		
		this.value = value;
	}

	public boolean isMapFromOtherTable() {
		return mapFromOtherTable;
	}

	public void setMapFromOtherTable(boolean mapFromOtherTable) {
		this.mapFromOtherTable = mapFromOtherTable;
	}

	public String getDateValue() {
		Pattern p = Pattern.compile("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])");
        Matcher m = p.matcher(getValue());
        while(m.find()) {
            return m.group();
        }
        return null;
	}

	
	

}

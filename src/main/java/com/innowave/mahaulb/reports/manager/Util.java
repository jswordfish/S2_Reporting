package com.innowave.mahaulb.reports.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Util {
	
	static Map<String, String> map ;
	static Properties properties = new Properties();
	static {
		try {
			properties.load(new FileInputStream("dataType_mappings.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map = new HashMap<String, String>((Map)properties);
	}
	
	public static String getDynamicReportsDatatype(String databaseType) {
		if(map.get(databaseType) == null) {
			return "string";
		}
	return map.get(databaseType);
	}

}

package com.innowave.mahaulb.reports.jsf.manager;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil  {
	
	static ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("reportContext.xml");
	
	
	public static  <T> T getService(Class<T> type){
		return ctx.getBean(type);
	}

}

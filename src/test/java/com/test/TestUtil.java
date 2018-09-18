package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class TestUtil {
//test
	@Test
	public void testDate() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String dt = "12-12-2017";
		Date dte = dateFormat.parse(dt);
		System.out.println(dte.getTime());
	}
}

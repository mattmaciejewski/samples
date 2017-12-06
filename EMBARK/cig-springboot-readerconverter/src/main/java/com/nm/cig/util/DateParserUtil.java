package com.nm.cig.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DateParserUtil {
	
	public static DateTime parseDate(String dateString) {
		
		String newDateString = dateString.replace(" PDT", "");
		DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
		DateTime date = format.parseDateTime(newDateString);
	
		return date;
	}
	
	public static java.sql.Timestamp getCurrentTimeStamp(){
		
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
		
	}
	
}

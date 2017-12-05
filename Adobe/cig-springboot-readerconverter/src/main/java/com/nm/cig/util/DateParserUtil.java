package com.nm.cig.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DateParserUtil {
	
	public static DateTime parseDate(String dateString) {
		
		if(dateString.contains("PDT")){
			dateString = dateString.replace(" PDT", "");
		}else if(dateString.contains("PST")){
			dateString = dateString.replace(" PST", "");
		}
		DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
		DateTime date = format.parseDateTime(dateString);
	
		return date;
	}
	
	public static DateTime parseUnixDate(String dateString) {
		long ds = Long.parseLong(dateString.substring(0, 13));
		DateTime time = new DateTime((long)ds);
		//String unixDate = time.toString().substring(0, 12);
		DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
		//DateTime newDate = format.parseDateTime(unixDate);
		//DateTime xTime = newDate;
		
		//String unixDate2 = time.toString();
		
		return time;
	}
	
	public static java.sql.Timestamp getCurrentTimeStamp(){
		
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
		
	}
	
}

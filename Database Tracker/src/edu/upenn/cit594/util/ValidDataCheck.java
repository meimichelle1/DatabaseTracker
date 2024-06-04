package edu.upenn.cit594.util;

import java.util.regex.Pattern;

public class ValidDataCheck {
	
	static Pattern timeFormat  = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"); 
	
	static Pattern pattern  = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$"); 
	
	/*
	 * check if the string is number
	 */
	public static boolean isNumeric(String str) {
		// check if the string is not null and contains only digits 
		return str != null && str.matches("\\d+"); 
	}
	
	/*
	 * check if the input is partial or full
	 */
	public static boolean isPatialOrFull(String str) {
		return (str.equals("partial")) || (str.equals("full")); 
	}
	
	/*
	 * check if the input date is in the YYYY-MM-DD format 
	 */
	public static boolean isValidDateFormat(String str) {
		return pattern.matcher(str).matches(); 
	}
	
	public static boolean isValidTimestamp(String timestamp) {
		return timeFormat.matcher(timestamp).matches(); 
	}
	
}

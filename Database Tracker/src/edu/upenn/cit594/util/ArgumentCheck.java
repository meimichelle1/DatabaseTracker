package edu.upenn.cit594.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentCheck {
	
	public static boolean isLogArgument(String arg) {
		Pattern pattern = Pattern.compile("^--log=(?<value>.+)$"); 
		
		Matcher matcher = pattern.matcher(arg); 
		
		return matcher.matches(); 
	}
	
	public static boolean isValidArgument(String arg) {
		Pattern pattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$"); 
		
		Matcher matcher = pattern.matcher(arg); 
		
		return matcher.matches(); 
	}
	
	public enum Argument {
		covid, properties, population, log
	}
	
	// check file accessibility 
	public static boolean fileCanBeRead(String filename) {
		File file = new File(filename); 

	    if (!file.exists() || !file.canRead()){
	      System.err.println("Error: File does not exist or cannot be read - " + filename);
	      return false;
	    }

	    return true; // file exists and readable 
	}
	
}

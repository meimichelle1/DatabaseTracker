package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

	private PrintWriter writer; 
	
	// private constructor to prevent instantiation from outside 
	private Logger() {} 
	
	// singleton instance  of logger
	private static Logger instance = new Logger(); 
	
	// singleton accessor method 
	public static Logger getInstance() { 
		return instance; 
	}
	
	// set the output file for logging , if the file is already open, it's closed first 
	public boolean setOutputFile(String log) {
		if (writer != null) {
			writer.close(); // close current writer if it existed 
		}
		
		try {
			// initialize the PrintWriter in append mode and with autoFlush set to true
			writer = new PrintWriter(new FileWriter(log, true), true); // set to auto flush
			return true; 
		} catch (IOException e) {
			System.err.println("Error: trouble to open/create the log file: " + e.getMessage()); 
			writer = null; 
			return false; 
		}
	}
	
	// log a message to the output file 
	public void log(String msg) {
		// get current time 
		long timestamp = System.currentTimeMillis(); 
		
		if (writer == null) {
			System.out.println("Logger output file does not set!"); 
			return; 
		}
		
		// construct log message 
		writer.println(timestamp + " " + msg); // since autoFlush = true, we dont need to call flush() here
	}

}

package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.upenn.cit594.util.*;

public class CSVPopulationFileReader implements CSVPopulationReader {

	private String filename; 
	
	/* 
	 * constructor to take the filename as argument 
	 */
	public CSVPopulationFileReader(String filename) {
		this.filename = filename; 
	}
	
	/*
	 * get the treemap that included all the zip population
	 */
	public TreeMap<String, ZipPopulation> getAllZipPopulation() throws IOException {
		TreeMap<String, ZipPopulation> l = new TreeMap<>(); 
		
		try(BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
			String headerLine = reader.readLine();
			if (headerLine == null) {
                throw new IllegalArgumentException("CSV file is empty.");
            }
			String[] headers = headerLine.split(","); 
			
			// create map to hold header position 
			Map<String, Integer> headerMap = new HashMap<>(); 
						
			for (int i = 0; i < headers.length; i++) {
				// remove the " "
				
				String header = DoubleQuotesParse.cleanQuote(headers[i]); 
				headerMap.put(header, i); 
		    }
			
			// ensure certain header names exist 
			if (!headerMap.containsKey("zip_code") || !headerMap.containsKey("population")) {
				throw new IllegalArgumentException("CSV does not contain expected headers. "); 
			}
			
			String line; 
			
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(","); 
				
				// verify the ZIP is exactly 5 digits 
				String zip = DoubleQuotesParse.cleanQuote(parts[headerMap.get("zip_code")]); 
				
				if (zip == null || zip.length() != 5 || !ValidDataCheck.isNumeric(zip)) {
					continue; 
				}
				
				// verify the population is digit 
				int population; 	
				String tempPopulation = DoubleQuotesParse.cleanQuote(parts[headerMap.get("population")]);
				
				try {
					population = Integer.parseInt(tempPopulation);
				} catch (NumberFormatException e) {
					continue; // skip the invalid line 
				} 
				
				// add the zip instance to the list 
				if (!l.containsKey(zip)) {
					l.put(zip, new ZipPopulation(zip, population)); 
				}
				
			}
		} catch (IOException e) {
			throw new IOException("Unable to open or read from the file: " + filename, e);
		} 
		
		return l; 
	}
}

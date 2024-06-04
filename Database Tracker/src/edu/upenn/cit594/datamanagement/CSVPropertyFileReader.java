package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.upenn.cit594.util.*; 

public class CSVPropertyFileReader implements CSVPropertyReader {

	private String filename; 
	
	public CSVPropertyFileReader(String filename) {
		this.filename = filename; 
	}
	
	public TreeMap<String, ZipProperty> getAllZipProperty() throws IOException {
		TreeMap<String, ZipProperty> l = new TreeMap<>(); 
		
		try(BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
			String headerLine = reader.readLine();
			
			if (headerLine == null) throw new IllegalArgumentException("CSV file is empty. "); 
			
			String[] headers = headerLine.split(","); 
			
			// create map to hold header position 
			Map<String, Integer> headerMap = new HashMap<>(); 
			
			for (int i = 0; i < headers.length; i++) {
				// remove the " "
				String header = DoubleQuotesParse.cleanQuote(headers[i]); 
				headerMap.put(header, i); 
			}
			
			// ensure certain header names exist 
			if (!headerMap.containsKey("zip_code") || !headerMap.containsKey("market_value") || !headerMap.containsKey("total_livable_area")) {
				throw new IllegalArgumentException("CSV does not ciontain expected headers. "); 
			}
			
			String line; 
			
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(","); 
				
				// ignore the zip that has less than 5 or the first 5 character is not digit 
				String zip = DoubleQuotesParse.cleanQuote(parts[headerMap.get("zip_code")]); 
				if (zip == null || zip.length() < 5 || !ValidDataCheck.isNumeric(zip.substring(0, 5))) {
					continue; 
				} else {
					zip = zip.substring(0, 5); 
				}
				
				// get the market value 
				String marketValue = DoubleQuotesParse.cleanQuote(parts[headerMap.get("market_value")]); 
				
				// get the total livable area 
				String livableArea = DoubleQuotesParse.cleanQuote(parts[headerMap.get("total_livable_area")]); 
				
				// add data 
				if (l.containsKey(zip)) {
					ZipProperty prop = l.get(zip); 
					prop.setMarketValue(marketValue);
					prop.setLivableArea(livableArea);
				} else {
					ZipProperty prop = new ZipProperty(zip); 
					prop.setMarketValue(marketValue);
					prop.setLivableArea(livableArea);
					l.put(zip, prop); 
				}
				
			}
		} catch (IOException e) {
			throw new IOException("Unable to open or read from the file: " + filename, e);
		} 
		
		return l; 
	}
}

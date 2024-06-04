package edu.upenn.cit594.datamanagement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.upenn.cit594.util.*;

public class CovidFileReader implements CovidReader {
	
	private String filename; 

	public CovidFileReader(String filename) {
		this.filename = filename; 
	}
	
	public TreeMap<String, ZipCovid> getAllZipCovid() throws IOException, ParseException {
		if (filename.toLowerCase().endsWith(".json")) {
			// if the file is json use the JSON parsing method 
			return parseJsonFile(); 
		} else if (filename.toLowerCase().endsWith(".csv")) {
			// if the file is csv use the csv parsing method 
			return parseCSVFile(); 
		} else {
			// throw an exception indicate Unsupported file type 
			throw new IllegalArgumentException("Unsupported file extension: " + filename);
		}
	}
	

	private TreeMap<String, ZipCovid> parseCSVFile() throws IOException {
		// store zip in order 
		TreeMap<String, ZipCovid> l = new TreeMap<>(); 
		
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
			if (!headerMap.containsKey("zip_code") || !headerMap.containsKey("partially_vaccinated") || !headerMap.containsKey("fully_vaccinated") 
					|| !headerMap.containsKey("etl_timestamp")) {
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
				
				// ignore the timestamp that is not in the right format 
				
				String timestamp = DoubleQuotesParse.cleanQuote(parts[headerMap.get("etl_timestamp")]);
				if (!ValidDataCheck.isValidTimestamp(timestamp)) {
					continue; 
				}
				// extract the time in format YYYY-MM-DD
				String time = timestamp.split(" ")[0]; 
				
				int partialVal = 0,fullVal =0;
				try {
					/// Attempt to parse partial and full vaccination values
					partialVal = parseVaccinationData(parts, headerMap.get("partially_vaccinated"));
		            fullVal = parseVaccinationData(parts, headerMap.get("fully_vaccinated"));
				} catch(IOException e) {
					
				}
				
				// create the new instance 
				if (l.containsKey(zip)) {
					ZipCovid tempInstance = l.get(zip);
	                tempInstance.getMap().put(time, new Integer[]{partialVal, fullVal});
				} else {
					ZipCovid tempInstance = new ZipCovid(zip);
	                tempInstance.getMap().put(time, new Integer[]{partialVal, fullVal});
	                l.put(zip, tempInstance);
				}
			}
		} catch (IOException e) {
			throw new IOException("Unable to open or read from the file: " + filename, e);
		} 
		
		return l; 
	}
	
	// helper function
	private int parseVaccinationData(String[] parts, Integer index) throws IOException {
        if (index == null || index >= parts.length || parts[index] == null) return 0;
        String data = DoubleQuotesParse.cleanQuote(parts[index]);
        if (data != null && ValidDataCheck.isNumeric(data)) {
            return Integer.parseInt(data);
        }
        return 0; // Return 0 if data is non-numeric or null
    }

	
	private TreeMap<String, ZipCovid> parseJsonFile() throws IOException, ParseException {
		// store zip in order 
		TreeMap<String, ZipCovid> l = new TreeMap<>(); 
		
		// create JSONParser indicator 
		JSONParser parser = new JSONParser(); 
		
		try(FileReader reader = new FileReader(filename)){
			// tries to read the file and parse its content
			JSONArray fileArray = (JSONArray) parser.parse(reader); 
			
			for (Object obj : fileArray) {
				// iterate over each obj in the array, cast the object to a JSONObject representing a tweet  
				JSONObject objJson = (JSONObject) obj; 
				
				// try to extract the zip_code from the obj 
				Long zip =(Long) objJson.get("zip_code");
				int zipInt = zip.intValue();
				String ZIP = String.valueOf(zipInt); 
				
				// check if zip valid 
				// skip the entries with invalid or missing zip code 
				if (ZIP == null || ZIP.length() != 5 || !ValidDataCheck.isNumeric(ZIP)) continue; 
				
				String timestamp = (String) objJson.get("etl_timestamp");
				// ignore timestamp that is not in the right format 
				if (!ValidDataCheck.isValidTimestamp(timestamp)) continue; 
				String date = timestamp.split(" ")[0]; 
				
		  
	                // get the number of partially vaccinated, fully vaccinated and booster doses
	                Long  partiallyVac = (Long)objJson.get("partially_vaccinated");
	                Long fullyVac = (Long) objJson.get("fully_vaccinated");

	                // if the number is null, set it to 0
	                if(partiallyVac == null){
	                	partiallyVac = 0L;
	                }
	                if(fullyVac == null){
	                	fullyVac = 0L;
	                }

	                // convert the number to int
	                int partiallyVaccinated = partiallyVac.intValue();
	                int fullyVaccinated = fullyVac.intValue();
	  
	             // create the new instance 
					if (l.containsKey(ZIP)) {
						ZipCovid tempInstance = l.get(ZIP);
		                tempInstance.getMap().put(date, new Integer[]{partiallyVaccinated, fullyVaccinated});
					} else {
						ZipCovid tempInstance = new ZipCovid(ZIP);
		                tempInstance.getMap().put(date, new Integer[]{partiallyVaccinated, fullyVaccinated});
		                l.put(ZIP, tempInstance);
					}
	           
			}
			reader.close(); 
		}
		
		return l; 
	}
	
}

package edu.upenn.cit594.util;

import java.util.HashMap;
import java.util.Map;

public class ZipCovid {
	
	private String zip; 
	private Map<String, Integer[]> vaccinatedDate = new HashMap<>(); 

	public ZipCovid(String zip) {
		this.zip = zip; 
	}
	
	// get the zip
	public String getZip() {
		return zip; 
	}
	
	public Map<String, Integer[]> getMap(){
		return vaccinatedDate; 
	}

}

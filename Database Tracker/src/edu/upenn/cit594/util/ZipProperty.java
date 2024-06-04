package edu.upenn.cit594.util;


public class ZipProperty {

	private String zip; 
	private int totalMarketProperty; 
	private int totalLiveProperty; 
	private double marketValue; 
	private double livableArea; 
	

	public ZipProperty(String zip) {
		this.zip = zip; 
	}
	
	public void setMarketValue(String value) {
		// check if the value is null or empty 
		if (value == null || value.trim().isEmpty()) return; 
		
		try {
			double market = Double.parseDouble(value); 
			marketValue += market; 
			totalMarketProperty++; 
		} catch(NumberFormatException e){
			// expected to ignore 
		}
	}
	
	public void setLivableArea(String value) {
		// check if the value is null or empty 
		if (value == null || value.trim().isEmpty()) return; 
				
		try {
			double live = Double.parseDouble(value); 
			livableArea += live; 
			totalLiveProperty++; 
		} catch(NumberFormatException e){
			//expected to ignore 
		}
	}
	
	public int getTotalMarketCount() {
		return this.totalMarketProperty; 
	}
	
	public double getMarketValues() {
		return this.marketValue; 
	}
	
	public double getLivevable() {
		return this.livableArea; 
	}
	
	public int getTotalLiveProperty() {
		return this.totalLiveProperty; 
	}
}

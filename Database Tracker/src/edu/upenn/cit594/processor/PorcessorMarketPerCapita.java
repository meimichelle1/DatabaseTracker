package edu.upenn.cit594.processor;

import java.io.IOException;
import java.util.TreeMap;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.util.*; 

public class PorcessorMarketPerCapita {
	
	protected CSVPropertyReader propReader; 
	protected CSVPopulationReader popReader; 
	
	protected String zip; 
	
	protected TreeMap<String, ZipProperty> propMap;
	protected TreeMap<String, ZipPopulation> popMap;

	public PorcessorMarketPerCapita(CSVPopulationReader popReader, CSVPropertyReader propReader, String zip) throws IOException {
		this.popReader = popReader; 
		this.propReader = propReader; 
		this.zip = zip; 
		
		this.propMap = propReader.getAllZipProperty(); 
		this.popMap = popReader.getAllZipPopulation(); 
	}
	
	public int totalMarketPerCapita() {
		if (!propMap.containsKey(zip) || !popMap.containsKey(zip)) return 0; 
		
		ZipProperty zipProperty = propMap.get(zip); 
		ZipPopulation zipPopulation = popMap.get(zip); 
		
		if (zipProperty.getMarketValues() == 0 || zipPopulation.getPopulation() == 0) {
			return 0; 
		}
		
		double result = (double) zipProperty.getMarketValues() / zipPopulation.getPopulation(); 
		
		return (int) result; 
	}

}

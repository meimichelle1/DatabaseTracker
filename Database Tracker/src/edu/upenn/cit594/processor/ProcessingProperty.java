package edu.upenn.cit594.processor;

import java.io.IOException;
import java.util.TreeMap;

import edu.upenn.cit594.datamanagement.CSVPropertyReader;
import edu.upenn.cit594.util.ZipProperty;

public class ProcessingProperty implements ProcessorProperty{
	
	protected CSVPropertyReader propReader; 
	
	protected String zip; 
	
	protected TreeMap<String, ZipProperty> propMap; 

	public ProcessingProperty(CSVPropertyReader propReader, String zip) throws IOException {
		this.propReader = this.propReader;
		this.zip = zip; 
		
		this.propMap = propReader.getAllZipProperty(); 
	}
	
	public int getAverageMarketValue() {
		if (propMap.containsKey(zip)) {
			ZipProperty inputZip = propMap.get(zip); 
			
			if (inputZip.getTotalMarketCount() == 0) {
				return (int) 0; 
			}
			double result = inputZip.getMarketValues()/inputZip.getTotalMarketCount(); 
			return (int) result; 
		}
		/* what if the zip is not contained? */
		return (int) 0; 
	}
	
	public int getAverageLiavble() {
		if (propMap.containsKey(zip)) {
			ZipProperty inputZip = propMap.get(zip); 
			if (inputZip.getTotalLiveProperty() == 0) {
				return (int) 0; 
			}
			
			double result = inputZip.getLivevable()/inputZip.getTotalLiveProperty(); 
			return (int) result; 
		}
		/* what if the zip is not contained? */
		return (int) 0; 
	}

}

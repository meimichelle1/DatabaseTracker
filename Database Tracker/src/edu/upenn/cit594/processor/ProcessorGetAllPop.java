package edu.upenn.cit594.processor;

import java.io.IOException;
import java.util.TreeMap;

import edu.upenn.cit594.datamanagement.CSVPopulationReader;
import edu.upenn.cit594.util.ZipPopulation;

public class ProcessorGetAllPop {
	protected CSVPopulationReader popReader; 
	
	protected TreeMap<String, ZipPopulation> popMap; 
	
	private int totalPop; 

	public ProcessorGetAllPop(CSVPopulationReader popReader) throws IOException {
		this.popReader = popReader; 
		
		popMap = popReader.getAllZipPopulation(); 
	}
	
	public int getTotalPop() {
		for (String zip : popMap.keySet()) {
			ZipPopulation tempZip = popMap.get(zip); 
			totalPop += tempZip.getPopulation(); 
		}
		return totalPop; 
	}

}

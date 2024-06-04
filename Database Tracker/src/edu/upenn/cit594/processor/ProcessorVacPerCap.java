package edu.upenn.cit594.processor;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.parser.ParseException;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.util.ZipCovid;
import edu.upenn.cit594.util.ZipPopulation;

public class ProcessorVacPerCap {
	
	protected CSVPopulationReader popReader; 
	protected CovidReader covidReader; 
	protected String status; 
	protected String date; 
	
	protected TreeMap<String, ZipPopulation> populationMap; 
	protected TreeMap<String, ZipCovid> covidMap; 

	public ProcessorVacPerCap(CSVPopulationReader popReader, CovidReader covidReader, String status, String date) throws IOException, ParseException {
		this.popReader = popReader; 
		this.covidReader = covidReader; 
		this.status = status; 
		this.date = date; 
		
		this.populationMap = popReader.getAllZipPopulation(); 
		this.covidMap = covidReader.getAllZipCovid(); 
	}
	
	public TreeMap<String, Double> getAllVaccineData(){
		TreeMap<String, Double> map = new TreeMap<>(); 
		
		// check if population is 0 
		for (String zip: populationMap.keySet()) {
			ZipPopulation zipPopulation = populationMap.get(zip); 
			
			if (zipPopulation.getPopulation() == 0) continue; // Skip if population is zero
			
			if (covidMap.containsKey(zip)) {
				ZipCovid zipCovid = covidMap.get(zip); 
				Map<String, Integer[]> dateMap = zipCovid.getMap(); 
				
				// if the provided date is out of range 
				if (!dateMap.containsKey(date)) {
					map.put(zip, (double)0); 
					
					continue; 
				} 
				
				Integer[] vaccinationData = dateMap.get(date); 
				// if the total vaccinations on specific date is 0 
				if (vaccinationData != null && (vaccinationData[0] + vaccinationData[1]) > 0) {
					
					double result = status.equals("partial") ? (double) vaccinationData[0]/zipPopulation.getPopulation()
							:(double) vaccinationData[1]/zipPopulation.getPopulation(); 
					
					map.put(zip, result); 
				} else {
					map.put(zip, 0.0); 
				}
			}
		}
		
		return map; 
	}

}

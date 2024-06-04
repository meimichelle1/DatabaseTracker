package edu.upenn.cit594.processor;

import java.io.IOException;
import java.util.TreeMap;

import org.json.simple.parser.ParseException;

import edu.upenn.cit594.datamanagement.CovidReader;
import edu.upenn.cit594.datamanagement.CSVPopulationReader;
import edu.upenn.cit594.datamanagement.CSVPropertyReader;
import edu.upenn.cit594.util.ZipCovid;
import edu.upenn.cit594.util.ZipPopulation;
import edu.upenn.cit594.util.ZipProperty;

public class ProcessorCase7 {
	
	protected CSVPopulationReader popReader; 
	protected CovidReader covidReader; 
	protected CSVPropertyReader propReader; 
	
	protected String zip; 
	protected String date; 
	
	protected TreeMap<String, ZipProperty> propMap;
	protected TreeMap<String, ZipPopulation> popMap;
	protected TreeMap<String, ZipCovid> covidMap; 

	public ProcessorCase7(CSVPopulationReader popReader, CovidReader covidReader, CSVPropertyReader propReader, String zip, String date) throws IOException, ParseException {
		this.popReader = popReader; 
		this.covidReader = covidReader; 
		this.propReader = propReader; 
		
		this.zip = zip; 
		this.date = date; 
		
		this.propMap = propReader.getAllZipProperty(); 
		this.popMap = popReader.getAllZipPopulation(); 
		this.covidMap = covidReader.getAllZipCovid(); 
	}

	public int getResult7() {
		if (!propMap.containsKey(zip) || !popMap.containsKey(zip) || !covidMap.containsKey(zip)) {
			return 0; 
		}
		
		ZipProperty prop = propMap.get(zip); 
		ZipPopulation pop = popMap.get(zip); 
		ZipCovid covid = covidMap.get(zip); 
		
		if (pop.getPopulation() == 0 || prop.getTotalLiveProperty() == 0) {
			return 0; 
		}
		
		double averageLivable = prop.getLivevable()/pop.getPopulation(); 
		
		Integer[] vaccine; 
		if (covid.getMap().containsKey(date)) {
			vaccine = covid.getMap().get(date); 
		} else {
			return 0; 
		}
		
		int sum = vaccine[0] + vaccine[1]; 
		
		if (sum == 0) {
			return 0; 
		}
		
		double result = sum * averageLivable; 
		
		return (int) result; 
	}
}

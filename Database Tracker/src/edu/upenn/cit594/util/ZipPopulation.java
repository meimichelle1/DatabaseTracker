package edu.upenn.cit594.util;

public class ZipPopulation {

	private String zip; 
	private int population; 
	
	public ZipPopulation(String zip, int population) {
		this.zip = zip; 
		this.population = population; 
	}
	
	public int getPopulation() {
		return this.population; 
	}
	
	public String getZip() {
		return zip; 
	}
	

}

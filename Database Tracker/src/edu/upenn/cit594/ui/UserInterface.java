package edu.upenn.cit594.ui;

import java.util.List;
import java.util.TreeMap;

import edu.upenn.cit594.processor.PorcessorMarketPerCapita;
import edu.upenn.cit594.processor.ProcessorCase7;
import edu.upenn.cit594.processor.ProcessorGetAllPop;
import edu.upenn.cit594.processor.ProcessorProperty;
import edu.upenn.cit594.processor.ProcessorVacPerCap;

public class UserInterface {
	/*
	 * display the main men
	 */
	public static void displayMenu() {
		System.out.println("0. Exit the program.");
		System.out.println("1. Show the available actions.");
		System.out.println("2. Show the total population for all ZIP Codes.");
		System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
		System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
		System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
		System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
		System.out.println("7. Show the total livable area available to vaccinated individuals (fully and partially) in a specific ZIP code on a specific date (by dividing the total livable space by the total population).");
		prompt(); 
	}
	
	/*
	 * helper method to get prompt from user 
	 */
	public static void prompt() {
		System.out.print("> ");
		System.out.flush(); // flush the output buffer 
	}
	
	/*
	 * Case 1: to display available actions to user 
	 */
	public static void availableActions(List<String> l) {
		System.out.println("BEGIN OUTPUT");
		for (String i: l) {
			System.out.println(i); 
		}
		System.out.println("END OUTPUT");
	}
	
	/* 
	 * case 2: to display the total population for all ZIP 
	 */
	public static void displayTotalPopulation(ProcessorGetAllPop processor){
		int result = processor.getTotalPop(); 
		
		System.out.println("BEGIN OUTPUT");
		System.out.println(result);
		System.out.println("END OUTPUT");
	}
	
	public static void partialOrFull() {
		System.out.println("Please type partial or full for vacinations per capital for each Zip Code. ");
		prompt(); 
	}
	
	public static void vacinationDate() {
		System.out.println("Please enter a specific date in the format: YYYY-MM-DD. ");
		prompt(); 
	}
	
	public static void askForZip() {
		System.out.println("Please enter a 5-digit ZIP Code. "); 
		prompt(); 
	}
	
	/* case 3: display partial or full vaccinations per capita */
	public static void displayVacPerCap(ProcessorVacPerCap processor) {
		TreeMap<String, Double> map = processor.getAllVaccineData(); 
		
		System.out.println("BEGIN OUTPUT"); 
		for (String zip: map.keySet()) {
			System.out.printf("%s %.4f%n", zip, map.get(zip)); 
		}
		System.out.println("END OUTPUT"); 
	}
	
	/* case 4: display average market value */
	public static void displayAverageMarketValue(ProcessorProperty processor) {
		int result = processor.getAverageMarketValue(); 
		
		System.out.println("BEGIN OUTPUT");
		System.out.println(result); 
		System.out.println("END OUTPUT"); 
	}
	
	/* case 5: display average livable area */
	public static void displayAverageLivable (ProcessorProperty processor) {
		int result = processor.getAverageLiavble(); 
		
		System.out.println("BEGIN OUTPUT");
		System.out.println(result); 
		System.out.println("END OUTPUT"); 
	}
	
	/* case 6: display total market value per capita */
	public static void displayTotalMarketPerCap(PorcessorMarketPerCapita processor) {
		int result = processor.totalMarketPerCapita(); 
		
		System.out.println("BEGIN OUTPUT");
		System.out.println(result); 
		System.out.println("END OUTPUT"); 
	}
	
	/* case 7: display the result */ 
	public static void displayCase7(ProcessorCase7 processor) {
		int result = processor.getResult7(); 
		
		System.out.println("BEGIN OUTPUT");
		System.out.println(result); 
		System.out.println("END OUTPUT"); 
	}
}

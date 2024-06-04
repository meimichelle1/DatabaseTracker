package edu.upenn.cit594;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.ui.UserInterface;
import edu.upenn.cit594.util.ArgumentCheck;
import edu.upenn.cit594.util.ValidDataCheck;

public class Main {

	public static void main(String[] args) {
		// check if there have at least 1 arg
		if (args.length == 0) {
			System.err.println("No arguments provided. "); 
			return; 
		}
		// initialize and configure the logger 
		Logger logger = Logger.getInstance(); 
		
		Map<String, String> names = new HashMap<>(); 
		List<String> availableList; 
		
		// find the log file first 
		for (String arg: args) {
			// file the log file by its pattern 
			if (ArgumentCheck.isLogArgument(arg)) {
				String value = arg.split("=")[1];
				
				if (!value.toLowerCase().endsWith(".log")) {
					System.err.println("Error: Log file extension is not acceptable.");
				    return;
				}
				
				if (!logger.setOutputFile(value) ||!ArgumentCheck.fileCanBeRead(value)){
					System.err.println("Error: Log file cannot be created/opened for writing.");
				    return;
				}
				
				//put log into the map 
				if (names.containsKey("log")) {
					System.err.println("Error: The name of log is used more than one. "); 
					return; 
				} 
				
				names.put("log", value); 
				// log the current command arguments
				logger.log(String.join(" ", args)); 
			} else if (ArgumentCheck.isValidArgument(arg)) {
				// check if the name of argument is valid 
				String[] parts = arg.split("="); 
				String name = parts[0].substring(2); 
				String value = parts[1]; 
				ArgumentCheck.Argument argument = ArgumentCheck.Argument.valueOf(name); 
				
				if (argument == null) {
					System.err.println("Error: Invalid argument name: " + name); 
					return; 
				}
				
				// check if argument use more than one 
				if (names.containsKey(name)) {
					System.err.println("Error: The name of anargument is used more than one: " + name); 
					return; 
				}
				
				// check file extension
				if (!value.toLowerCase().endsWith(".json") && !value.toLowerCase().endsWith(".csv")) {
			        System.err.println("Error: Unsupported file extension " + value);
			        return;
			    } 
				
				// Check file existence and readability
			    if (!ArgumentCheck.fileCanBeRead(value)) {
			        return; // Error message already displayed in the method
			    }
				
				names.put(name, value); 
			} else {
				System.err.println("Error: Invalid argument format: " + arg);
				return; 
			}
		}
		
		// check if no log file provide 
		if (!names.containsKey("log")) {
			System.err.println("Please provide the valid log arguments. "); 
			return; 
		}
		
		/* run the program */
		Scanner scanner = new Scanner(System.in); 
		boolean exit = false; 
		
		// set up the available list 
		ProcessorGetAvailableActions processorAvailable = new ProcessorGetAvailableActions(names); 
		availableList = processorAvailable.getAllAvailableActions(); 
		
		
		while (!exit) {
			UserInterface.displayMenu(); 
			
			// get response from the user and logger the input 
			String input = getResponse(scanner, logger); 
			
			/* need to verify if the choice is within the available */
			if (availableList.contains(input)) {
				int choice = Integer.parseInt(input); 
					
				switch (choice) {
				
				/* exit the program */
				case 0 -> {
					exit = true; 
				}
				
				/* show available actions */
				case 1 -> {
					UserInterface.availableActions(availableList); 
				}
				
				/* show total population for all ZIP code */
				case 2 -> {
					try {
						CSVPopulationReader popReader = new CSVPopulationFileReader(names.get("population"));
						// log the name of the file is opened for reading 
						logger.log("population"); 
						
						ProcessorGetAllPop processor = new ProcessorGetAllPop(popReader); 
						
						UserInterface.displayTotalPopulation(processor); 
					} catch (IOException e) {
						System.err.println("Error processing files: " + e.getMessage());
					}
					
				}
				
				case 3 -> {
					boolean validResponse = false; // Flag to check if response is valid
					String response = ""; 
					
					while (!validResponse) {
						// prompt user to type partial or full
						UserInterface.partialOrFull(); 
						
						// get response from the user and logger the input 
						response = getResponse(scanner, logger); 
						
						if (ValidDataCheck.isPatialOrFull(response)) {
							validResponse = true;
						} else {	
							System.out.println("Invalid format, please enter partial or full. "); 
						}
					}
					
					boolean validDate = false; 
					String date = ""; 
							
					while (!validDate) {
						// prompt user to type date 
						UserInterface.vacinationDate(); 
								
						// get response from the user and logger the input 
						date = getResponse(scanner, logger); 
								
						if (!ValidDataCheck.isValidDateFormat(date)) {
							System.out.println("Invalid format, please enter a date in the format: YYYY-MM-DD. "); 
						} else {
							validDate = true; 
							try {
								CSVPopulationReader popReader = new CSVPopulationFileReader(names.get("population")); 
								// log the name of the file is opened for reading 
								logger.log("population"); 
								
								CovidReader covidReader = new CovidFileReader(names.get("covid")); 
								// log the name of the file is opened for reading 
								logger.log("covid"); 
								
								ProcessorVacPerCap processor = new ProcessorVacPerCap(popReader, covidReader, response, date);
								UserInterface.displayVacPerCap(processor); 
							} catch (IOException | ParseException e) {
								System.err.println("Error processing files: " + e.getMessage());
							} 
						}
	
					} 
					
				}
				
				case 4 -> {
					boolean validResponse = false; // flag to check if response is valid 
					String zip = ""; 
					while (!validResponse) {
						// prompt user to type zip
						UserInterface.askForZip(); 
						// get zip response from the user and logger the input 
						zip = getResponse(scanner, logger); 
						
						if (zip.length() == 5 && ValidDataCheck.isNumeric(zip)) {
							validResponse = true; 
							try {
								CSVPropertyReader propReader = new CSVPropertyFileReader(names.get("properties")); 
								// log the name of the file is opened for reading 
								logger.log("properties"); 
								
								ProcessorProperty processor  = new ProcessingProperty(propReader, zip); 
								
								UserInterface.displayAverageMarketValue(processor); 
							} catch (IOException e) {
								System.err.println("Error processing files: " + e.getMessage());
							}
						} else {
							System.out.println("Invalid input, please eneter a 5-digit ZIP Code. "); 
						}
					}
				}
				
				case 5 -> {
					boolean validResponse = false; // flag to check if response is valid 
					String zip = ""; 
					
					while (!validResponse) {
						// prompt user to type zip
						UserInterface.askForZip(); 
						// get zip response from the user and logger the input 
						zip = getResponse(scanner, logger); 
						
						if (zip.length() == 5 && ValidDataCheck.isNumeric(zip)) {
							validResponse = true; 
							try {
								CSVPropertyReader propReader = new CSVPropertyFileReader(names.get("properties")); 
								// log the name of the file is opened for reading 
								logger.log("properties");
								
								ProcessorProperty processor  = new ProcessingProperty(propReader, zip); 
								
								UserInterface.displayAverageLivable(processor) ; 
							} catch (IOException e) {
								System.err.println("Error processing files: " + e.getMessage());
							}
						} else {
							System.out.println("Invalid input, please eneter a 5-digit ZIP Code. "); 
						}
					}
				}
				
				case 6 -> {
					boolean validResponse = false; // flag to check if response is valid 
					String zip = ""; 
					
					while (!validResponse) {
						// prompt user to type zip
						UserInterface.askForZip(); 
						// get zip response from the user and logger the input 
						zip = getResponse(scanner, logger); 
					
						if (zip.length() == 5 && ValidDataCheck.isNumeric(zip)) {
							validResponse = true; 
							try {
								
								CSVPopulationReader popReader = new CSVPopulationFileReader(names.get("population")); 
								// log the name of the file is opened for reading 
								logger.log("population");
								
								CSVPropertyReader propReader = new CSVPropertyFileReader(names.get("properties")); 
								// log the name of the file is opened for reading 
								logger.log("properties");
								
								PorcessorMarketPerCapita processor  = new PorcessorMarketPerCapita(popReader, propReader, zip); 
								
								UserInterface.displayTotalMarketPerCap(processor); 
								
							} catch (IOException e) {
								System.err.println("Error processing files: " + e.getMessage());
							}
						} else {
							System.out.println("Invalid input, please eneter a 5-digit ZIP Code. "); 
						}
					}
				}
				
				case 7 -> {
					boolean validResponse = false; // flag to check if response is valid 
					String zip = ""; 
					
					while (!validResponse) {
						// prompt user to type zip
						UserInterface.askForZip(); 
						// get zip response from the user and logger the input 
						zip = getResponse(scanner, logger); 
						
						if (zip.length() == 5 && ValidDataCheck.isNumeric(zip)) {
							validResponse = true; 
						} else {
							System.out.println("Invalid input, please eneter a 5-digit ZIP Code. "); 
						}
					}
					
					boolean validDate = false; 
					String date = ""; 
					while (!validDate) {
						// prompt user to type date 
						UserInterface.vacinationDate(); 
								
						// get response from the user and logger the input 
						date = getResponse(scanner, logger); 
								
						if (!ValidDataCheck.isValidDateFormat(date)) {
							System.out.println("Invalid format, please enter a date in the format: YYYY-MM-DD. "); 
						} else {
							validDate = true; 
							try {
								CSVPopulationReader popReader = new CSVPopulationFileReader(names.get("population"));
								// log the name of the file is opened for reading 
								logger.log("population");
								
								CovidReader covidReader = new CovidFileReader(names.get("covid")); 
								// log the name of the file is opened for reading 
								logger.log("covid");
								
								CSVPropertyReader propReader = new CSVPropertyFileReader(names.get("properties")); 
								// log the name of the file is opened for reading 
								logger.log("properties");
								
								ProcessorCase7 processor = new ProcessorCase7(popReader, covidReader, propReader, zip, date); 
								
								UserInterface.displayCase7(processor); 
							} catch (IOException | ParseException e) {
								System.err.println("Error processing files: " + e.getMessage());
							}
						} 
					}
							
					}
				} 
				
		} else {
			System.out.println("Error: the input value is not acceptable / the action is not available. "); 
		}
		}
		scanner.close(); // close the scanner 
	}

	/*
	 * helper function for geting response from the user and log the input 
	 */
	private static String getResponse(Scanner scanner, Logger logger) {
		String input = scanner.nextLine(); 
		logger.log(input); 
		return input; 
	}
}

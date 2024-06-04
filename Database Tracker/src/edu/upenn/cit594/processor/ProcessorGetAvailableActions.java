package edu.upenn.cit594.processor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProcessorGetAvailableActions {
	
	protected Map<String, String> names; 

	public ProcessorGetAvailableActions(Map<String, String> names) {
		this.names = names; 
	}

	public List<String> getAllAvailableActions(){
		List<String> l = new LinkedList<>(Arrays.asList("0", "1")); 
		
		if (names.containsKey("population")) l.add("2"); 
		
		if (names.containsKey("covid") && names.containsKey("population")) l.add("3"); 
		
		if (names.containsKey("properties")) { 
			l.add("4");
			l.add("5"); 
		}
		
		if (names.containsKey("population") && names.containsKey("properties")) l.add("6");
		
		if (names.containsKey("population") && names.containsKey("properties") && names.containsKey("covid")) l.add("7");
		
		// sort the list 
		Collections.sort(l);
		
		return l; 
	}
}

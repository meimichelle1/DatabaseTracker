package edu.upenn.cit594.util;

import java.io.IOException;

public class DoubleQuotesParse {

	enum State{
        START, 
        IN_QUOTE
    }
	
	public static String cleanQuote(String str) throws IOException {
		if (str == null || str.isEmpty()) return ""; 
		
		StringBuilder sb = new StringBuilder(); 
		State state = State.START; 
		
		for (char cur : str.toCharArray()) {
			
			switch (state) {
			case START -> {
				if (cur == '"'){
                    state = State.IN_QUOTE;  
                }  else {
                     sb.append(cur); 
                }
			}
			
			case IN_QUOTE -> {
                if (cur == '"'){
                    state = State.START; 
                } else {
                    sb.append(cur); 
                }
            }
			}
			
		}
		if (state == State.IN_QUOTE) {
            throw new IOException("Format error: String ended inside a quote."); // Handle error if string ends inside quotes
        }
		
		return sb.toString(); 
	}

}

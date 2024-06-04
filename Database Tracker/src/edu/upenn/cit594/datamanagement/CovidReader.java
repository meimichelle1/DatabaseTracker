package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.util.TreeMap;

import org.json.simple.parser.ParseException;

import edu.upenn.cit594.util.ZipCovid; 

public interface CovidReader {
	
	public TreeMap<String, ZipCovid> getAllZipCovid() throws IOException, ParseException; 
}

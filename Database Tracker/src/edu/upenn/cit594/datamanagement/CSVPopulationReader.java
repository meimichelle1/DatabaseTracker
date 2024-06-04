package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.util.TreeMap;

import edu.upenn.cit594.util.ZipPopulation;

public interface CSVPopulationReader {

	public TreeMap<String, ZipPopulation> getAllZipPopulation() throws IOException;
}

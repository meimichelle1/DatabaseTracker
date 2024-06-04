package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.util.TreeMap;

import edu.upenn.cit594.util.ZipProperty;

public interface CSVPropertyReader {
	public TreeMap<String, ZipProperty> getAllZipProperty() throws IOException; 
}

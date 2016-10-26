package assignment1;

import java.io.*;
import assignment1.UniformCostSearch;



public class FindPath {

	public static void main(String[] args) {
		String environmentMapFileName = args[1];
		String queryFileName = args[2];
		String outputFileName = args[3];
		
		try {
			UniformCostSearch.startSearch(environmentMapFileName, queryFileName,outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}

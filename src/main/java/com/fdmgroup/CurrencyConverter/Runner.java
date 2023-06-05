package com.fdmgroup.CurrencyConverter;

import java.io.*;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.google.common.io.Files;

/**
 * Main runner class to execute the Currency converter
 * Once run, initalises two main classes database and processTransaction
 * Database takes in the file paths of all associated files
 * ProcessTransaction takes in database and makes use of its
 * attributes to convert and update the user json file
 * 
 * @author Randy
 *
 */
public class Runner {

	/**
	 * Resets the users for assessment json file to aid checking
	 */
	static void reset() {
		
	    File original= new File("src/backup/resources/users_for_assessment - backup.json");
	    File copied= new File("src/main/resources/users_for_assessment.json");
	    try {
			Files.copy(original, copied);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("File copied over");
	}
	
	public static void main(String[] args) {
		String filePath = "src/main/resources";
		String userPath = filePath + "/users_for_assessment.json";
		String FXPath = filePath + "/fx_rates.json";
		String transactionsPath = filePath + "/transactions_for_assessment.txt";
		
		Database database = new Database(userPath, transactionsPath, FXPath);
		ProcessTransaction processTransaction = new ProcessTransaction(database);

		processTransaction.processAll();
		
//		reset();
		
	}
	
}

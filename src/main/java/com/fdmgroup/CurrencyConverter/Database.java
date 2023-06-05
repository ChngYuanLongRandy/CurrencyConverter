package com.fdmgroup.CurrencyConverter;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Class that holds all of the variables retrieved from the files
 * including the fx rates, users and the transactions.
 * Constructor (String userPath, String transactionsPath, String FXPath)
 * @author Randy
 *
 */
public class Database implements Idatabase {

	private static final Logger logger = LogManager.getLogger("Log to file warn");

	ArrayList<Transaction> transactions = new ArrayList<>();
	
	User[] users;
	List<FXcurrencyDetails> FXcurrencyDetails;
	String userPath;
	String transactionPath;
	String FXPath;
	
	/**
	 * @param userPath String : path to the user file. Expects a JSON file
	 * @param transactionsPath String : Path to the transactions file. Expects a txt file
	 * @param FXPath String: Path to the fx file. Expects a json file
	 */
	public Database(String userPath, String transactionsPath, String FXPath) {
		logger.trace("Database created with params \nuserPath:" + userPath + "\ntransactionsPath: " + transactionsPath + "\nFXPath: " + FXPath );
		this.userPath = userPath;
		this.transactionPath = transactionsPath;
		this.FXPath = FXPath;
	}

	
	/**
	 * @return the fXcurrencyDetails
	 */
	public List<FXcurrencyDetails> getFXcurrencyDetails() {
		return FXcurrencyDetails;
	}


	/**
	 * @param fXcurrencyDetails the fXcurrencyDetails to set
	 */
	public void setFXcurrencyDetails(List<FXcurrencyDetails> fXcurrencyDetails) {
		FXcurrencyDetails = fXcurrencyDetails;
	}


	/**
	 * @return the users
	 */
	public User[] getUsers() {
		return users;
	}


	/**
	 * @param users the users to set
	 */
	public void setUsers(User[] users) {
		this.users = users;
	}


	/**
	 * Read users from the userPath and stores as in the users variable
	 */
	@Override
	public void readUsers(){
		// TODO Auto-generated method stub
		ObjectMapper om = new ObjectMapper();
		File userFile = new File(userPath);
		User[] users = null;
		try {
			users = om.readValue(userFile, User[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to read file due to " + e);
		}
		if (users != null) {
			logger.trace("Value of users read:");
			logger.trace("Length:" + users.length);
			for (User user: users) {
				logger.trace(user);	
			}
			this.users = users;
		}
	}

	/**
	 * Takes users from users variable and writes them out to the 
	 * userPath
	 */
	@Override
	public void writeUsers() {
		ObjectMapper om = new ObjectMapper();
		File userFile = new File(userPath);
		
		
		logger.trace("Contents of users");
		for (User user : users) {
			logger.trace(user);
		}
		
		try {
			om.writerWithDefaultPrettyPrinter().writeValue(userFile, users);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to write file due to " + e);
		}
		
	}



	/**
	 * Reads the transaction text file and saves to the transaction variable
	 */
	@Override
	public void readTransactions() {
		// TODO Auto-generated method stub
		File transactionFile = new File(transactionPath);
		FileReader fr =null;
		String input =null;
		try {
			fr = new FileReader(transactionFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to read file due to " + e);
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			input = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to read line due to " + e);
		}
		
		do {

			String[] inputs = input.split(" ");
			logger.trace("Input read "+ inputs);
			String name = inputs[0];
			String sellCurrencyType= inputs[1];
			String buyCurrencyType = inputs[2];
			String amount = inputs[3];
			
			Currency sellCurrency = new Currency(sellCurrencyType, Double.parseDouble(amount));
			Currency buyCurrency = new Currency(buyCurrencyType, 0);			
			
			Transaction currentTransaction = new Transaction(name,sellCurrency, buyCurrency);
			
			logger.trace("current Transaction "+ currentTransaction);
			
			transactions.add(currentTransaction);
			
			try {
				input = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Unable to read line due to " + e);
			}
		}
		
		while (input != null);
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to close file due to " + e);
		}
		
		
	}


	/**
	 * Reads the FX file and saves to FX variable
	 */
	@Override
	public void readFX() {
		// TODO Auto-generated method stub
		ObjectMapper om = new ObjectMapper();
		File FXFile = new File(FXPath);
		List<FXcurrencyDetails> FXcurrencyDetails = null;
		try {
			FXcurrencyDetails = om.readValue(FXFile, FXCurrency.class).getFXCurrencies();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to read file due to " + e);
		}
		if (FXcurrencyDetails != null) {
			logger.trace("Value of FXcurrencyDetails read:");
			logger.trace("Length:" + FXcurrencyDetails.size());
			for (FXcurrencyDetails fx: FXcurrencyDetails) {
				logger.trace(fx);	
			}
			this.FXcurrencyDetails = FXcurrencyDetails;
		}
		
	}

}

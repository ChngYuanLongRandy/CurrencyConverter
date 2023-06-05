package com.fdmgroup.CurrencyConverter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class DatabaseTest {

	private static final Logger logger = LogManager.getLogger("Log to file debug");
	String filePath = "src/main/resources";
	String testUserPath = filePath + "/users_for_testing.json";
	String invalidtestUserPath = filePath + "/invalid_users_for_test.json";
	String testWritePath = filePath + "/testWrite.json";
	String correctUserPath = filePath + "/users_for_assessment.json";
	String nonExistentPath = filePath + "/wrongpath.json";
	String correctFXPath = filePath + "/fx_rates.json";
	String correctTransactionsPath = filePath + "/transactions_for_assessment.txt";
	
	@Test
	public void Test_AllValidPaths_TestUsers_ReadDatabaseMethod() {
		logger.debug("Test_AllValidPaths_TestUsers_ReadDatabaseMethod");
		Database database = new Database(testUserPath, correctTransactionsPath, correctFXPath);
		
		Wallet jamesWallet = new Wallet(200.0,500.0,0,0,0,0);
		User james = new User("James",jamesWallet);


		Wallet randyWallet = new Wallet(500.0,200.0,0,0,0,0);
		User randy = new User("Randy",randyWallet);

		User[] users = {randy, james};
		
		database.readUsers();
		
		assertEquals(0,users[0].toString().compareTo(database.users[0].toString()));
		assertEquals(0,users[1].toString().compareTo(database.users[1].toString()));
		assertEquals(2,database.users.length);
	}
	@Test
	public void Test_AllValidPaths_ActualUsers_ReadDatabaseMethod() {
		logger.debug("Test_AllValidPaths_ActualUsers_ReadDatabaseMethod");
		Database database = new Database(correctUserPath, correctTransactionsPath, correctFXPath);

		Wallet janeWallet = new Wallet(100,0,0,120,0,0);
		User jane = new User("Jane", janeWallet);

		Wallet maryWallet = new Wallet(0,0,0.3,508,0,0);
		User mary = new User("Mary", maryWallet);
		
		Wallet johnnyWallet = new Wallet(252.34,0,0,0,3267.12,398);
		User johnny = new User("Johnny", johnnyWallet);
		
		User[] users = {jane, mary, johnny};
		
		database.readUsers();
		
		assertEquals(0,users[0].toString().compareTo(database.users[0].toString()));
		assertEquals(0,users[1].toString().compareTo(database.users[1].toString()));
		assertEquals(0,users[2].toString().compareTo(database.users[2].toString()));
		assertEquals(3,database.users.length);
	}

	@Test
	public void Test_AllValidPaths_InvalidUserFormat_ReadDatabaseMethod(){
		logger.debug("Test_AllValidPaths_InvalidUserFormat_ReadDatabaseMethod");
		Database database = new Database(invalidtestUserPath, correctTransactionsPath, correctFXPath);
		
		database.readUsers();
		
		assertNull(database.users);
	}
	

	@Test
	public void Test_WriteUsers_WriteDatabaseMethod(){
		logger.debug("Test_WriteUsers_WriteDatabaseMethod");
		Wallet jamesWallet = new Wallet(200.0,500.0,0,0,0,0);
		User james = new User("James",jamesWallet);


		Wallet randyWallet = new Wallet(500.0,200.0,0,0,0,0);
		User randy = new User("Randy",randyWallet);

		User[] users = {randy, james};
		
		
		
		Database database = new Database(testWritePath, correctTransactionsPath, correctFXPath);

		database.setUsers(users);
		
		database.writeUsers();
		
		Database databaseTest = new Database(testWritePath, correctTransactionsPath, correctFXPath);
		
		databaseTest.readUsers();
		
		assertEquals(0,databaseTest.users[0].toString().compareTo(database.users[0].toString()));
		assertEquals(0,databaseTest.users[1].toString().compareTo(database.users[1].toString()));
		assertEquals(2,databaseTest.users.length);
	}
	
	@Test
	public void Test_ReadFX_ReadFXMethod() {
		logger.debug("Test_ReadFX_ReadFXMethod");
		List<FXcurrencyDetails> FX;
		Database database = new Database(testWritePath, correctTransactionsPath, correctFXPath);
		
		Double rate =0.98535489535028;
		Double inverseRate = 1.0148627714936;
		String code = "EUR";
		
		database.readFX();
		
		FXcurrencyDetails fxEur = new FXcurrencyDetails();
		
		fxEur.setCode(code);
		fxEur.setInverseRate(inverseRate);
		fxEur.setRate(rate);
		
		assertEquals(0,fxEur.toString().compareTo(database.FXcurrencyDetails.get(0).toString()));
		
	}
	
	@Test
	public void Test_ReadTransaction_Method() throws IOException {
		logger.debug("Test_ReadTransaction_Method");
		Database database = new Database(testWritePath, correctTransactionsPath, correctFXPath);
		
		String user = "Jane";
		String sellCurrencyType = "cad";
		String buyCurrencyType = "usd";
		Double amount = 150.0;
		Currency sellCurrency = new Currency(sellCurrencyType, amount);
		Currency buyCurrency = new Currency(buyCurrencyType, 0);
		
		Transaction transaction = new Transaction(user, sellCurrency, buyCurrency);
		
		database.readTransactions();
		
		assertEquals(0,transaction.sellCurrency.toString().compareTo(database.transactions.get(0).sellCurrency.toString()));
		assertEquals(0,transaction.buyCurrency.toString().compareTo(database.transactions.get(0).buyCurrency.toString()));
		assertEquals(0,transaction.userName.toString().compareTo(database.transactions.get(0).userName.toString()));
		
	}
	
	
	
}

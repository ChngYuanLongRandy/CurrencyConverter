package com.fdmgroup.CurrencyConverter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito.Then;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProcessTransactionTest {
	
	private static final Logger logger = LogManager.getLogger("Log to file debug");
	
	static String filePath = "src/main/resources";
	static String testUserPath = filePath + "/users_for_testing.json";
	static String invalidtestUserPath = filePath + "/invalid_users_for_test.json";
	static String testWritePath = filePath + "/testWrite.json";
	static String correctUserPath = filePath + "/users_for_assessment.json";
	static String nonExistentPath = filePath + "/wrongpath.json";
	static String correctFXPath = filePath + "/fx_rates.json";
	static String correctTransactionsPath = filePath + "/transactions_for_assessment.txt";
	
	static Database database = null;
	static ProcessTransaction processTranscation = null;

//	static Database database = null;
//	static ProcessTransaction processTranscation = null;
	
	@Mock
	static Database databaseService;
	
	@BeforeEach
	public void Test_setup() {
		database = new Database(testUserPath, correctTransactionsPath, correctFXPath);
		processTranscation = new ProcessTransaction(database);
		database.readFX();
		
//		database = new Database(testUserPath, correctTransactionsPath, correctFXPath);
//		processTranscation = new ProcessTransaction(databaseService);
		
	}
	
	@Test
	public void Test_InvalidCurrency_XXX_CheckCurrency() {
		String invalidCurrency = "xxx";
		
		assertFalse(processTranscation.validateCurrency(invalidCurrency));
		
	}

	@Test
	public void Test_ValidCurrency_MYR_CheckCurrency() {
		String invalidCurrency = "myr";
		
		assertTrue(processTranscation.validateCurrency(invalidCurrency));
		
	}
	
	@Test
	public void Test_ValidString_InputSGDAndKrw_ReturnRate() {
		logger.trace("Test_ValidString_InputSGDAndKrw_ReturnRate");

		String fromCode="sgd";

		String buyCode="krw";
		
		
		processTranscation.returnRate(fromCode, buyCode);
		
		assertNotEquals(1,processTranscation.toRate,0.1);
		assertNotEquals(1,processTranscation.fromRate,0.1);
		assertNotEquals(-1,processTranscation.toRate,0.1);
		assertNotEquals(-1,processTranscation.fromRate,0.1);
		
	}
	
	@Test
	public void Test_InvalidString_InputXXXAndKrw_ReturnRate() {
		logger.trace("Test_InvalidString_InputXXXAndKrw_ReturnRate");

		String fromCode="xxx";

		String toCode="krw";
		
		
		processTranscation.returnRate(fromCode, toCode);
		
		assertNotEquals(-1,processTranscation.toRate,0.1);
		assertEquals(-1,processTranscation.fromRate,0.1);
		
	}
	
	@Test
	public void Test_InvalidString_InputSGDAndAAA_ReturnRate() {
		logger.trace("Test_InvalidString_InputSGDAndAAA_ReturnRate");
		String fromCode="sgd";
		
		String toCode="AAA";
		
		
		processTranscation.returnRate(fromCode, toCode);
		
		assertEquals(-1,processTranscation.toRate,0.1);
		assertNotEquals(-1,processTranscation.fromRate,0.1);
		
	}
	
	@Test
	public void Test_ValidString_BuyingUSD_ReturnRate() {
		logger.trace("Test_ValidString_BuyingUSD_ReturnRate");

		String fromCode="sgd";
		
		String toCode="AAA";
		

		
		processTranscation.returnRate(fromCode, toCode);
		
		assertNotEquals(1,processTranscation.toRate,0.1);
		assertNotEquals(1,processTranscation.fromRate,0.1);
		
	}
	
	@Test
	public void Test_ValidString_SellingUSD_ReturnRate() {
		logger.trace("Test_ValidString_SellingUSD_ReturnRate");

		String fromCode="usd";
		
		String toCode="AAA";
		

		
		processTranscation.returnRate(fromCode, toCode);
		
		assertNotEquals(1,processTranscation.toRate,0.1);
		assertEquals(1,processTranscation.fromRate,0.1);
		
	}

	@Test
	public void Test_ValidString_BuyingAndSellingUSD_ReturnRate() {
		logger.trace("Test_ValidString_BuyingAndSellingUSD_ReturnRate");

		String fromCode="usd";
		
		String buyCode="usd";
		

		
		processTranscation.returnRate(fromCode, buyCode);
		
		assertEquals(1,processTranscation.toRate,0.1);
		assertEquals(1,processTranscation.fromRate,0.1);
		
	}
	
	@Test
	public void Test_ValidUser_ValidTransaction_ProcessTransaction_Method() {
		logger.trace("Test_ValidUser_ValidTransaction_ProcessTransaction_Method");
		HashMap<String,Integer> change = new HashMap<>();
		
		String user = "Jane";
		String sellCurrencyType = "cad";
		String buyCurrencyType = "sgd";
		Double amount = 150.0;
		Currency sellCurrency = new Currency(sellCurrencyType, amount);
		Currency buyCurrency = new Currency(buyCurrencyType, 0);
		
		Transaction transaction = new Transaction(user, sellCurrency, buyCurrency);
		
//		Buy 300USD, Sell 150CAD at 2.0 
//		Buy 60SGD, Sell 300USD at 0.2
		
		processTranscation.setFromRate(2.0);
		processTranscation.setToRate(0.2);		
		
		change.put(sellCurrencyType,-150);
		change.put(buyCurrencyType,60);
		
		assertEquals(change, processTranscation.processTxn(transaction));
		
	}	
	
	@Test
	public void Test_ValidUser_ValidTransaction_InsufficientBalance_ValidateTransaction_Method() {
		logger.trace("Test_ValidUser_ValidTransaction_InsufficientBalance_ValidateTransaction_Method");
		HashMap<String,Integer> change = new HashMap<>();
		
//		Jane does not have enough CAD to make the transaction. Validation should fail
		
//		Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
		
		Wallet janeWallet = new Wallet(0,500.0,0,0,0,0);
		User jane = new User("Jane",janeWallet);
		
		String user = "Jane";
		String sellCurrencyType = "cad";
		String buyCurrencyType = "sgd";
		Double amount = 150.0;
		Currency sellCurrency = new Currency(sellCurrencyType, amount);
		Currency buyCurrency = new Currency(buyCurrencyType, 0);
		
		Transaction transaction = new Transaction(user, sellCurrency, buyCurrency);
		
//		Buy 300USD, Sell 150CAD at 2.0 
//		Buy 60SGD, Sell 300USD at 0.2
		
		processTranscation.setFromRate(2.0);
		processTranscation.setToRate(0.2);		
		
		change.put(sellCurrencyType,-150);
		change.put(buyCurrencyType,60);
		
		processTranscation.setChange(change);
		
		assertFalse(processTranscation.validateTransaction(change, transaction, jane));
	}
	
	@Test
	public void Test_ValidUser_ValidTransaction_NoValidCurrency_ValidateTransaction_Method() {
		logger.trace("Test_ValidUser_ValidTransaction_NoValidCurrency_ValidateTransaction_Method");
		HashMap<String,Integer> change = new HashMap<>();
		
//		Transaction contains an invalid currency. Validation should fail
		
//		Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
		
		Wallet janeWallet = new Wallet(0,500.0,0,0,0,0);
		User jane = new User("Jane",janeWallet);
		
		String user = "Jane";
		String sellCurrencyType = "xxx";
		String buyCurrencyType = "sgd";
		Double amount = 150.0;
		Currency sellCurrency = new Currency(sellCurrencyType, amount);
		Currency buyCurrency = new Currency(buyCurrencyType, 0);
		
		Transaction transaction = new Transaction(user, sellCurrency, buyCurrency);
		
//		Buy 300USD, Sell 150CAD at 2.0 
//		Buy 60SGD, Sell 300USD at 0.2
		
		processTranscation.setFromRate(2.0);
		processTranscation.setToRate(0.2);		
		
		change.put(sellCurrencyType,-150);
		change.put(buyCurrencyType,60);
		
		processTranscation.setChange(change);
		
		assertFalse(processTranscation.validateTransaction(change, transaction, jane));
	}
	
	@Test
	public void Test_InvalidUser_ValidTransaction_ValidateTransaction_Method() {
		logger.trace("Test_InvalidUser_ValidTransaction_ValidateTransaction_Method");
		HashMap<String,Integer> change = new HashMap<>();
		
//		Transaction belongs to James. Validation should fail
		
//		Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
		
		Wallet janeWallet = new Wallet(0,500.0,0,0,0,0);
		User jane = new User("Jane",janeWallet);
		
		String user = "James";
		String sellCurrencyType = "cad";
		String buyCurrencyType = "sgd";
		Double amount = 150.0;
		Currency sellCurrency = new Currency(sellCurrencyType, amount);
		Currency buyCurrency = new Currency(buyCurrencyType, 0);
		
		Transaction transaction = new Transaction(user, sellCurrency, buyCurrency);
		
//		Buy 300USD, Sell 150CAD at 2.0 
//		Buy 60SGD, Sell 300USD at 0.2
		
		processTranscation.setFromRate(2.0);
		processTranscation.setToRate(0.2);		
		
		change.put(sellCurrencyType,-150);
		change.put(buyCurrencyType,60);
		
		processTranscation.setChange(change);
		
		assertFalse(processTranscation.validateTransaction(change, transaction, jane));
	}
	
	@Test
	public void Test_ValidUser_ValidTransaction_SufficientBalance_ValidateTransaction_Method() {
		logger.trace("Test_ValidUser_ValidTransaction_SufficientBalance_ValidateTransaction_Method");
		HashMap<String,Integer> change = new HashMap<>();
		
//		All is okay, validation should return true
		
//		Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
		
		Wallet jamesWallet = new Wallet(0,500.0,0,0,0,0);
		User james = new User("James",jamesWallet);
		
		String user = "James";
		String sellCurrencyType = "sgd";
		String buyCurrencyType = "jpy";
		Double amount = 150.0;
		Currency sellCurrency = new Currency(sellCurrencyType, amount);
		Currency buyCurrency = new Currency(buyCurrencyType, 0);
		
		Transaction transaction = new Transaction(user, sellCurrency, buyCurrency);
		
//		Buy 300USD, Sell 150CAD at 2.0 
//		Buy 60SGD, Sell 300USD at 0.2
		
		processTranscation.setFromRate(2.0);
		processTranscation.setToRate(0.2);		
		
		change.put(sellCurrencyType,-150);
		change.put(buyCurrencyType,60);
		
		processTranscation.setChange(change);
		
		assertTrue(processTranscation.validateTransaction(change, transaction, james));
	}
	
	@Test
	public void Test_ValidUser_ValidTransaction_SufficientBalance_effectTransaction_Method() {
		logger.trace("Test_ValidUser_ValidTransaction_SufficientBalance_effectTransaction_Method");
		HashMap<String,Integer> change = new HashMap<>();
		
//		All is okay, validation should return true
		
//		Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
		
		Wallet jamesWallet = new Wallet(0,0,0,500,0,0);
		Wallet newJamesWallet = new Wallet(0,0,60,350,0,0);
		Wallet updatedJamesWallet;
		User james = new User("James",jamesWallet);

		String sellCurrencyType = "cad";
		String buyCurrencyType = "jpy";
		
//		Transaction transaction = new Transaction(user, sellCurrency, buyCurrency);
		
//		Buy 300USD, Sell 150CAD at 2.0 
//		Buy 60SGD, Sell 300USD at 0.2
		
		processTranscation.setFromRate(2.0);
		processTranscation.setToRate(0.2);		
		
		change.put(sellCurrencyType,-150);
		change.put(buyCurrencyType,60);
		
		processTranscation.setChange(change);
		
		updatedJamesWallet = processTranscation.effectTransaction(james, change);
		
		james.setWallet(updatedJamesWallet); 
		
		assertEquals(newJamesWallet.toString(), james.wallet.toString());
	}
	
}

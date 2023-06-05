package com.fdmgroup.CurrencyConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CurrencyConverterTest {

	@Mock
	Database database;
	
	public void Test_ValidInput_ConvertToBaseUSD_ConvertMethod_ProcessTransaction() {
		
//		Wallet Constructor (USD,SGD,JPY,CAD,AUD,KRW)
		Wallet wallet = new Wallet(200.0,500.0,0,0,0,0);
		User user = new User("user", wallet);
		HashMap<String, Double> change = new HashMap<>();
		double rate = 2;
		Currency sellCurrency = new Currency("USD",500.0);
		Currency buyCurrency = new Currency("SGD",200.0);
		
		Transaction transaction = new Transaction(user,sellCurrency, buyCurrency);
		
		ProcessTransaction processTransaction = new ProcessTransaction(database);

		
		change = processTransaction.convert(user,transaction, rate);
		
		assertEquals(, change.get("USD"));
		
	}
	
//	public void Test_ConvertNonExistentBuyCurrency_ConvertMethod() {
//		
//		Wallet wallet = new Wallet(200.0,500.0,0,0,0,0);
//		User user = new User("user", wallet);
//		String buyCurrency = "TWD";
//		String sellCurrency = "USD";
//		HashMap<String, Double> sampleFX = new HashMap<>();
//		
//		sampleFX.put("USDtoTWD", 12.0);
//		
//		Transaction transaction = new Transaction(user,sellCurrency, buyCurrency);
//		
//		CurrencyConverter currencyConverter = new CurrencyConverter(database);
//
//		when(database.readFX()).thenReturn(sampleFX);
//		
//		currencyConverter.convert(user, transaction, database.readFX());
//		
//		assertEquals(0, converted);
//	}
	
}

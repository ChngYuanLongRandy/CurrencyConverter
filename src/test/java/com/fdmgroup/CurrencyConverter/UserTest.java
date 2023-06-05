package com.fdmgroup.CurrencyConverter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;


public class UserTest {

	@Test
	public void Test_UnavailableCurrency_ReturnFalse_GetCurrencyMethod() {
		
		Wallet janeWallet = new Wallet(100,0,0,120,0,0);
		User jane = new User("Jane", janeWallet);
		
		assertFalse(jane.getCurrency("NZD"));
	}

	@Test
	public void Test_AvailableCurrency_ReturnFalse_GetCurrencyMethod() {
		
		Wallet janeWallet = new Wallet(100,0,0,120,0,0);
		User jane = new User("Jane", janeWallet);
		
		assertTrue(jane.getCurrency("USD"));
	}
}

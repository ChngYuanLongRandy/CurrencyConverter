package com.fdmgroup.CurrencyConverter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class WalletTest {

	@Test
	public void Test_ReturnWalletBalance() {
		
//		Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
		Wallet janeWallet = new Wallet(100,0,0,120,0,0);
		User jane = new User("Jane", janeWallet);
		
		HashMap<String,Integer> walletBalance = new HashMap<>();
		walletBalance.put("usd", 100);
		walletBalance.put("sgd", 0);
		walletBalance.put("jpy", 0);
		walletBalance.put("cad", 120);
		walletBalance.put("aud", 0);
		walletBalance.put("krw", 0);
		
		assertEquals(walletBalance,jane.wallet.getWalletBalance());
		
	}
}

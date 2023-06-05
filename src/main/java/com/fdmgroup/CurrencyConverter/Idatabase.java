package com.fdmgroup.CurrencyConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public interface Idatabase {

	void readUsers();
	
	void writeUsers();
	
	void readTransactions();
	
	void readFX();
	
}

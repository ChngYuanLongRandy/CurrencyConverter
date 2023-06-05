package com.fdmgroup.CurrencyConverter;

/**
 * Holds the type of the currency (which strictly speaking should be the
 * currency itself e.g usd sgd, hkd, etc.) as well as the amount
 * 
 * Constructor (String type, double amount) 
 * 
 * @author Randy
 *
 */
public class Currency {

	String type;
	double amount;
	
	public Currency(String type, double amount) {
		this.type = type;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Currency [type=" + type + ", amount=" + amount + "]";
	}

}

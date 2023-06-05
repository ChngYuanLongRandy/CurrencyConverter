package com.fdmgroup.CurrencyConverter;

/**
 * An entry from the transaction file
 * @author Randy
 *
 */
public class Transaction {

	Currency sellCurrency;
	Currency buyCurrency;
	String userName;
	User user;
	
	public Transaction(User user, Currency sellCurrency, Currency buyCurrency) {
		// TODO Auto-generated constructor stub
	}

	public Transaction(String userName, Currency sellCurrency, Currency buyCurrency) {
		// TODO Auto-generated constructor stub
		
		this.sellCurrency = sellCurrency;
		this.buyCurrency = buyCurrency;
		this.userName = userName;
	}

	@Override
	public String toString() {
		
		return "Transaction [sellCurrency=" + sellCurrency + ", buyCurrency=" + buyCurrency + ", userName=" + userName
				+ ", user=" + user + "]";
	}
	


}

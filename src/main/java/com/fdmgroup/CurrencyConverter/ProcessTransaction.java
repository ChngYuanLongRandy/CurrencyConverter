package com.fdmgroup.CurrencyConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Processes, validates, effects the transaction to the user in the database
 * @author Randy
 *
 */
public class ProcessTransaction {

	private static final Logger loggerWarn = LogManager.getLogger("Log to file warn");
	Database database;
	HashMap<String, Double> change = new HashMap<>();
	double fromRate = -1;
	double toRate= -1;
	
	
	
	/**
	 * @return the change
	 */
	public HashMap<String, Double> getChange() {
		return change;
	}



	/**
	 * @param change the change to set
	 */
	public void setChange(HashMap<String, Double> change) {
		this.change = change;
	}



	/**
	 * @param database
	 */
	public ProcessTransaction(Database database) {
		// TODO Auto-generated constructor stub
		this.database = database;
	}

	/**
	 * @return the fromRate
	 */
	public double getFromRate() {
		return fromRate;
	}


	/**
	 * @param fromRate the fromRate to set
	 */
	public void setFromRate(double fromRate) {
		this.fromRate = fromRate;
	}


	/**
	 * @return the toRate
	 */
	public double getToRate() {
		return toRate;
	}


	/**
	 * @param toRate the toRate to set
	 */
	public void setToRate(double toRate) {
		this.toRate = toRate;
	}


	/**
	 * Pick out the return Rates for both selling the Sell Currency
	 * and buying the buy currency
	 * 
	 * All currency is quoted as USD pair
	 * FROM -> Selling curr, buying USD
	 * TO -> Selling USD, buying Curr
	 * 
	 * FROM == sellCurrency, TO == buy currency
	 * 
	 * Rates is defined as sell for USD, buy currency
	 * Inverse Rate is defined as buy rate for USD, sell currency 
	 * 
	 * If usd is found, sets rates to 1
	 * 
	 * @param fromCurrency
	 * @param toCurrency
	 */
	/**
	 * @param fromCurrency
	 * @param toCurrency
	 */
	public void returnRate(String fromCurrency, String toCurrency) {
		// TODO Auto-generated method stub
		
		if (validateCurrency(fromCurrency) == true ) {
			for (int i =0 ; i< database.FXcurrencyDetails.size(); i++) {
				if(database.FXcurrencyDetails.get(i).code.toString().toLowerCase().compareTo(fromCurrency)==0) {
					this.fromRate = database.FXcurrencyDetails.get(i).rate;
					loggerWarn.debug("Buy currency found with from rate :" + this.fromRate);
				}
			}
		}
		
		if (fromCurrency.toString().toLowerCase().compareTo("usd")==0) {
			this.fromRate = 1;
			loggerWarn.debug("Buy currency found with from rate : 1");
			
		}
		
		if (validateCurrency(toCurrency) == true ) {
			for (int i =0 ; i< database.FXcurrencyDetails.size(); i++) {
				if(database.FXcurrencyDetails.get(i).code.toString().toLowerCase().compareTo(toCurrency)==0) {
					this.toRate = database.FXcurrencyDetails.get(i).rate;
					loggerWarn.debug("Sell currency found with to rate :" + this.toRate);
				}
			}
		}
		
		if (toCurrency.toString().toLowerCase().compareTo("usd")==0) {
			this.toRate = 1;
			loggerWarn.debug("Sell currency found with to rate : 1");
		}
		
	}
	
	
	/**
	 * Validates the currency, essentially tries to find it in the FX table provided
	 * in the database as well as the base currency
	 * @param currency 
	 * @return True if currency is either found in fx table or is base currency
	 */
	public boolean validateCurrency(String currency) {
		
		for (int i =0 ; i< database.FXcurrencyDetails.size(); i++) {
			if(database.FXcurrencyDetails.get(i).code.toString().toLowerCase().compareTo(currency)==0) {
				loggerWarn.trace("Currency found " + currency + " = " +  database.FXcurrencyDetails.get(i).code.toString().toLowerCase());
				return true;
			}
		}
		if (currency.toLowerCase().compareTo("usd")==0) {
			loggerWarn.trace("Currency found as usd ");
			return true;
		}
		else 
			loggerWarn.trace("Currency " + currency + " not found ");
			return false;
		
	}

	/**
	 * Reset the rates to 1
	 */
	public void resetRates() {
		this.toRate = 1.0;
		this.fromRate = 1.0;
	}


	/**
	 * Takes the saved rates and multiply to the buying and 
	 * selling currency and return the change in currency
	 * 
	 * Example
	 * fromCurrency: 1000SGD at 1.2
	 * toCurrency: KRW at 100
	 * 
	 * Buy 1200USD for 1000SGD at 1.2
	 * Sell 1200USD for 12KRW at 100
	 * 
	 * This works out to
	 * 
	 * change = {"SGD" : -1000, "KRW": 100}
	 * 
	 * @param transaction
	 * @return a hashmap of two entries with an entry consisting of the currency and the change
	 */
	public HashMap<String,Double> processTxn(Transaction transaction) {
		double sellAmount = transaction.sellCurrency.amount;
//		Selling USD
		if (transaction.sellCurrency.type.toString().toLowerCase().compareTo("usd") == 0) {
			loggerWarn.trace("Customer is selling base currency");
			double buyAmount = (this.toRate * sellAmount );
			
			change.put(transaction.sellCurrency.type, sellAmount * -1); //Selling so we need to remove this amount from wallet
			change.put(transaction.buyCurrency.type, buyAmount);
			
			resetRates();
			loggerWarn.trace("Transaction processed , change is" + change.toString());
			return change;
		}
//		Buying USD
		else if (transaction.buyCurrency.type.toString().toLowerCase().compareTo("usd") == 0) {
			loggerWarn.trace("Customer is buying base currency");
			double buyAmount = (sellAmount * this.fromRate);
			
			change.put(transaction.sellCurrency.type, sellAmount * -1); //Selling so we need to remove this amount from wallet
			change.put(transaction.buyCurrency.type, buyAmount);
			resetRates();
			loggerWarn.trace("Transaction processed , change is" + change.toString());
			return change;
		}
//		With intermediary
		else {
			loggerWarn.trace("Customer is using base as intermediary");
			double USD = ((1/this.fromRate) * sellAmount );
			loggerWarn.trace("USD amount : " +USD);
			double buyAmount = (USD * this.toRate);
			
			change.put(transaction.sellCurrency.type, sellAmount * -1); //Selling so we need to remove this amount from wallet
			change.put(transaction.buyCurrency.type, buyAmount);
			resetRates();
			return change;
		}
	}



	
	/**validates the transaction by ensuring that the 
	 * - user is correct
	 * - user has the correct currency to make the transaction
	 * - user has enough currency to make the transaction
	 * - user does not buy and sell the same currency
	 * @param change :  change to be effected
	 * @param transaction : currencies to be traded
	 * @param user 
	 * @return True if all conditions satisfies.
	 */
	public boolean validateTransaction(HashMap<String, Double> change, Transaction transaction, User user) {
		// TODO Auto-generated method stub
		loggerWarn.trace("Entering transaction validation with params \n"+ change + "\n" + transaction + "\n" + user);	
		loggerWarn.trace("Transaction user name :" + transaction.userName.toString().toLowerCase());	
		loggerWarn.trace("User name :" + user.name.toString().toLowerCase());	
		loggerWarn.trace("Check name condition " + transaction.userName.toString().toLowerCase().compareTo(user.name.toString().toLowerCase()));	
		
//		Check Name
		if (transaction.userName.toString().toLowerCase().compareTo(user.name.toString().toLowerCase()) != 0) {
			loggerWarn.trace("User names do not match, fails transaction validation");
			return false;
		}
		
//		Check currency
		if (user.getCurrency(transaction.sellCurrency.type.toLowerCase()) == false) {
			loggerWarn.warn("User does not own currency to sell, fails transaction validation");
			return false;
		}
		
//		If currency is available, check if the amount is enough
		if (user.wallet.getWalletBalance().get(transaction.sellCurrency.type.toLowerCase()) < (int) transaction.sellCurrency.amount ) {
			loggerWarn.warn("User has insufficient currency to sell, fails transaction validation");
			return false;
		}
		
//		If both currencies are the same, fail it
		if (transaction.buyCurrency.type.toString().toLowerCase().compareTo(transaction.sellCurrency.type.toString().toLowerCase()) == 0) {
			loggerWarn.warn("User has attempted to buy and sell same currency, transaction is skipped");
			return false;
		}
		return true;
	}



	/**Effects the transaction as captured in the change
	 * 
	 * User's wallet will be deducted with the selling currency amount
	 * and added with the new amount from the buying currency
	 * 
	 * If user does not have the buying currency in the wallet, it 
	 * will be added and the new balance will be reflected
	 * 
	 * @param user
	 * @param change
	 * @return Wallet after the transaction has taken effect for the user to set
	 */
	public Wallet effectTransaction(User user, HashMap<String, Double> change) {
		// TODO Auto-generated method stub
//		Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
		String currency1 = (String) change.keySet().toArray()[0];
		String currency2 = (String) change.keySet().toArray()[1];
		loggerWarn.trace("currency1 :" + currency1);
		loggerWarn.trace("currency2 :" + currency2);
		Double amount1 = change.get(currency1);
		Double amount2 = change.get(currency2);
		loggerWarn.trace("amount1 :" + amount1);
		loggerWarn.trace("amount2 :" + amount2);
//		Currency 1
		if (currency1.toString().compareTo("usd")==0) {
			user.wallet.usd += amount1;
		}

		else if (currency1.toString().compareTo("jpy")==0) {
			user.wallet.jpy += amount1;
		}
		else if (currency1.toString().compareTo("cad")==0) {
			user.wallet.cad += amount1;
		}
		else if (currency1.toString().compareTo("aud")==0) {
			user.wallet.aud += amount1;
		}
		else if (currency1.toString().compareTo("krw")==0) {
			user.wallet.krw += amount1;
		}
		else if (currency1.toString().compareTo("eur")==0) {
			user.wallet.eur += amount1;
		}
		else if (currency1.toString().compareTo("hkd")==0) {
			user.wallet.hkd += amount1;
		}


//		Currency 2
		
		if (currency2.toString().compareTo("usd")==0) {
			user.wallet.usd += amount2;
		}

		if (currency2.toString().compareTo("jpy")==0) {
			user.wallet.jpy += amount2;
		}
		else if (currency2.toString().compareTo("cad")==0) {
			user.wallet.cad += amount2;
		}
		if (currency2.toString().compareTo("aud")==0) {
			user.wallet.aud += amount2;
		}
		
		if (currency2.toString().compareTo("krw")==0) {
			user.wallet.krw += amount2;
		}
		else if (currency2.toString().compareTo("eur")==0) {
			user.wallet.eur += amount2;
		}
		else if (currency2.toString().compareTo("hkd")==0) {
			user.wallet.hkd += amount2;
		}
		
		loggerWarn.trace(user.getWallet().toString());
		return user.getWallet();
	}

	
	/**
	 * This will be the main method to process all the 
	 * transaction and effect all of them 
	 * 
	 * It will call the database's methods to retrieve
	 * - FX rates
	 * - Transactions
	 * - Users
	 * 
	 * It will then loop through all of the transactions, 
	 * validate currencies, gather the changes, 
	 * validate the transaction and finally effect them
	 */
	public void processAll() {
		
		this.database.readFX();
		this.database.readTransactions();
		this.database.readUsers();
		
		for (User user: this.database.users) {
			loggerWarn.trace("User :" + user);
			for (Transaction transaction:this.database.transactions) {
				loggerWarn.trace("New Transaction");
				loggerWarn.trace("Transaction :" + transaction);
				if (validateCurrency(transaction.buyCurrency.type) && validateCurrency(transaction.sellCurrency.type)) {
					loggerWarn.trace("Currencies validation passed, proceeding with rates");
					returnRate(transaction.sellCurrency.type, transaction.buyCurrency.type);
					this.change.clear(); // If i dont wipe it here as well, if the transaction is invalidated, change will still remain
					this.change = processTxn(transaction);
					if (validateTransaction(this.change, transaction, user)) {
						loggerWarn.trace("Transaction validated, proceeding with effecting transaction");
						loggerWarn.trace("User's wallet before effecting :" + user.getWallet());
						Wallet newWallet = effectTransaction(user, this.change);
						user.setWallet(newWallet);
						loggerWarn.trace("User's wallet after effecting :" + user.getWallet());
					}
					else {
						loggerWarn.trace("Transaction : " + transaction +"\n Under user name : "+ user.name.toString() +"\n is invalid as user name is incorrect or "
								+ "amount from selling currency is insufficient or attempt to sell currency that user does not have");
						continue;
					}
				}
				else {
					loggerWarn.warn("Transaction : " + transaction +"\n"+ user.name.toString() +"\n is invalid as currency pair invalid");
					continue;
				}
			}
		}
		loggerWarn.trace("All users data before writing :" + this.database.users);
		this.database.writeUsers();
	}
}

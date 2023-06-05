package com.fdmgroup.CurrencyConverter;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An entry from the user. Contains a wallet which keeps track of balance
 * @author Randy
 *
 */
public class User {

	Wallet wallet;
	String name;
	
	@JsonCreator
	public User(@JsonProperty("name")String name, @JsonProperty("wallet") Wallet wallet) {
		super();
		this.wallet = wallet;
		this.name = name;
	}


	@Override
	public String toString() {
		return "User [wallet=" + wallet + ", name=" + name + "]";
	}



	/**
	 * @return the wallet
	 */
	@JsonProperty("wallet")
	public Wallet getWallet() {
		return wallet;
	}



	/**
	 * @param wallet the wallet to set
	 */
	@JsonProperty("wallet")
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}



	/**
	 * @return the name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}


	public boolean getCurrency(String currency) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> walletBalance = new HashMap<>();
		
		walletBalance = this.wallet.getWalletBalance();
		
		return walletBalance.containsKey(currency.toLowerCase());
	}


}

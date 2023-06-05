package com.fdmgroup.CurrencyConverter;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Keeps track of the user's balance 
 * @author Randy
 * Wallet Constructor (USD, SGD,JPY,CAD,AUD,KRW)
 */
public class Wallet {

	double usd =0;
	double sgd =0;
	double cad =0;
	double jpy =0;
	double aud =0;
	double krw =0;
	double hkd =0;
	double eur=0;
	
	
	/**
	 * @return the eur
	 */
	public double getEur() {
		return eur;
	}
	/**
	 * @param eur the eur to set
	 */
	public void setEur(double eur) {
		this.eur = eur;
	}
	/**
	 * @return the hkd
	 */
	@JsonProperty("hkd")
	public double getHkd() {
		return hkd;
	}
	/**
	 * @param hkd the hkd to set
	 */
	@JsonProperty("hkd")
	public void setHkd(double hkd) {
		this.hkd = hkd;
	}
	@JsonCreator
	public Wallet(@JsonProperty("usd") double usd, @JsonProperty("sgd") double sgd
			,@JsonProperty("jpy") double jpy, @JsonProperty("cad") double cad
			,@JsonProperty("aud") double aud, @JsonProperty("krw") double krw) {
		super();
		this.usd = usd;
		this.sgd = sgd;
		this.cad = cad;
		this.jpy = jpy;
		this.aud = aud;
		this.krw = krw;
	}
	/**
	 * @return the usd
	 */
	@JsonProperty("usd")
	public double getUsd() {
		return usd;
	}
	/**
	 * @param usd the usd to set
	 */
	@JsonProperty("usd")
	public void setUsd(double usd) {
		this.usd = usd;
	}
	/**
	 * @return the sgd
	 */
	@JsonProperty("sgd")
	public double getSgd() {
		return sgd;
	}
	/**
	 * @param sgd the sgd to set
	 */
	@JsonProperty("sgd")
	public void setSgd(double sgd) {
		this.sgd = sgd;
	}
	/**
	 * @return the cad
	 */
	public double getCad() {
		return cad;
	}
	/**
	 * @param cad the cad to set
	 */
	public void setCad(double cad) {
		this.cad = cad;
	}
	/**
	 * @return the jpy
	 */
	public double getJpy() {
		return jpy;
	}
	/**
	 * @param jpy the jpy to set
	 */
	public void setJpy(double jpy) {
		this.jpy = jpy;
	}
	/**
	 * @return the aud
	 */
	public double getAud() {
		return aud;
	}
	/**
	 * @param aud the aud to set
	 */
	public void setAud(double aud) {
		this.aud = aud;
	}
	/**
	 * @return the krw
	 */
	public double getKrw() {
		return krw;
	}
	/**
	 * @param krw the krw to set
	 */
	public void setKrw(double krw) {
		this.krw = krw;
	}


	@Override
	public String toString() {
		return "Wallet [usd=" + usd + ", sgd=" + sgd + ", cad=" + cad + ", jpy=" + jpy + ", aud=" + aud + ", krw=" + krw
				+ ", hkd=" + hkd + ", eur=" + eur + "]";
	}
	
	@JsonIgnore
	public HashMap<String,Integer> getWalletBalance() {
		// TODO Auto-generated method stub
		HashMap<String,Integer> walletBalance = new HashMap<>();
		
		walletBalance.put("usd", (int) this.usd);
		walletBalance.put("sgd", (int) this.sgd);
		walletBalance.put("cad", (int) this.cad);
		walletBalance.put("jpy", (int) this.jpy);
		walletBalance.put("aud", (int) this.aud);
		walletBalance.put("krw", (int) this.krw);
		walletBalance.put("hkd", (int) this.hkd);
		walletBalance.put("eur", (int) this.eur);
		
		
		return walletBalance;
	}

	
	
}

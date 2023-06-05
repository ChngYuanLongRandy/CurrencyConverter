package com.fdmgroup.CurrencyConverter;

public class InvalidCurrencyException extends Exception{

	public InvalidCurrencyException(String ErrorMessage) {
		super(ErrorMessage);
	}
}

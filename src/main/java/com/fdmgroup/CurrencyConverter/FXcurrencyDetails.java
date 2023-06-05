package com.fdmgroup.CurrencyConverter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * The details of the fx currency pair including the rate, inverseRate and the code
 * @author Randy
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FXcurrencyDetails {
    
	Double rate;
	Double inverseRate;
	
	String code;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the rate
	 */
	public Double getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}
	/**
	 * @return the inverseRate
	 */
	public Double getInverseRate() {
		return inverseRate;
	}
	/**
	 * @param inverseRate the inverseRate to set
	 */
	public void setInverseRate(Double inverseRate) {
		this.inverseRate = inverseRate;
	}
	@Override
	public String toString() {
		return "Details [rate=" + rate + ", inverseRate=" + inverseRate + ", code=" + code + "]";
	}

	
}

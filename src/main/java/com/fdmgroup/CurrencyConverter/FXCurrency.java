package com.fdmgroup.CurrencyConverter;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A currency pair meant to contain the readouts of the FX file
 * @author Randy
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FXCurrency {

	private List<FXcurrencyDetails> FXCurrencies = new ArrayList<>();
	
	@JsonAnySetter
    public void setFXCurrency(String name, FXcurrencyDetails FXCurrency) {
        this.FXCurrencies.add(FXCurrency);
    }

    public List<FXcurrencyDetails> getFXCurrencies() {
        return FXCurrencies;
    }

	
}

package edu.cmu.inmind.composition.aserv;

import edu.cmu.inmind.multiuser.controller.log.Log4J;

public class ACurrencyConverterService {

    /**
     * This method converts the currency of the specified USD amount to EUROs.
     * 
     * @param amountInUSD the amount in USD to convert. 
     * 
     * @return the converted amount to EURO.
     */
    public Long convertToEuro(Long amountInUSD) {
        Log4J.warn(this, String.format("Executing ACurrencyConverterService.convertToEuro for: "
        		+ "[amountInUSD: %s]", amountInUSD));
        return null;
    }
}

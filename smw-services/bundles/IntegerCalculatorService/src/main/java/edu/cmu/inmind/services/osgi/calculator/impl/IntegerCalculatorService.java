package edu.cmu.inmind.services.osgi.calculator.impl;

import edu.cmu.inmind.osgi.commons.markers.BundleImpl;
import edu.cmu.inmind.services.apis.CalculatorService;
import edu.cmu.inmind.services.pojos.CalculatorPOJO;

/**
 * Created by adangi on 4/21/18.
 */
@BundleImpl(alias = CalculatorService.CALCULATOR_INTEGER)
public class IntegerCalculatorService implements CalculatorService {

    @Override
    public CalculatorPOJO add(Number one, Number two) {
        System.out.println("Executing IntegerCalculatorService.add()...");

        // compute the result
        Number result = one.intValue() + two.intValue();

        CalculatorPOJO calculatorPOJO
                = new CalculatorPOJO.CalculatorPOJOBuilder(
                        CalculatorService.SERVICE,
                        CalculatorService.CALCULATOR_INTEGER,
                        CalculatorService.CALCULATOR_SUM)
                .setOne(one)
                .setTwo(two)
                .setOperation(CalculatorService.CALCULATOR_SUM)
                .setResult(result)
                .build();

        return calculatorPOJO;
    }

    @Override
    public CalculatorPOJO multiply(Number one, Number two) {
        System.out.println("Executing IntegerCalculatorService.multiply()...");

        // compute the result
        Number result = one.intValue() * two.intValue();

        CalculatorPOJO calculatorPOJO
                        = new CalculatorPOJO.CalculatorPOJOBuilder(
                        CalculatorService.SERVICE,
                        CalculatorService.CALCULATOR_INTEGER,
                        CalculatorService.CALCULATOR_MULTIPLY)
                .setOne(one)
                .setTwo(two)
                .setOperation(CalculatorService.CALCULATOR_MULTIPLY)
                .setResult(result)
                .build();

        return calculatorPOJO;
    }

    @Override
    public void execute() {
        System.out.println("Executing IntegerCalculatorService...");
    }
}

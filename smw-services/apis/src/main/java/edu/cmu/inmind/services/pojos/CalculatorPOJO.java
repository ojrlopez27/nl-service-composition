package edu.cmu.inmind.services.pojos;

import edu.cmu.inmind.services.apis.CalculatorService;
import edu.cmu.inmind.services.commons.GenericPOJO;
import java.util.ArrayList;
import java.util.List;

public class CalculatorPOJO extends GenericPOJO {

    private static final String ERR_TRANSFORM_UNSUPPORTED = "Invalid operation specified to CalculatorPOJO: ";

    private Number one;
    private Number two;
    private String operation;
    private Number result;

    protected CalculatorPOJO(CalculatorPOJOBuilder calculatorPOJOBuilder) {
        super(calculatorPOJOBuilder);
        this.one = calculatorPOJOBuilder.one;
        this.two = calculatorPOJOBuilder.two;
        this.operation = calculatorPOJOBuilder.operation;
        this.result = calculatorPOJOBuilder.result;
    }

    public CalculatorPOJO setResult(Number result) {
        this.result = result;
        return this;
    }

    public Number getOne() {
        return one;
    }

    public Number getTwo() {
        return two;
    }

    public String getOperation() {
        return operation;
    }

    public Number getResult() {
        return result;
    }

    @Override
    public List<Object> transform() {
        List<Object> parameters = new ArrayList<>();
        switch (this.operation) {
            case CalculatorService.CALCULATOR_SUM:
            case CalculatorService.CALCULATOR_MULTIPLY: {
                parameters.add(this.getOne());
                parameters.add(this.getTwo());
                break;
            }
            default:
                throw new UnsupportedOperationException(ERR_TRANSFORM_UNSUPPORTED + this.operation);
        }
        return parameters;
    }

    public static class CalculatorPOJOBuilder extends PojoBuilder {
        private Number one;
        private Number two;
        private String operation;
        private Number result;

        public CalculatorPOJOBuilder(String serviceId, String alias, String methodName) {
            super(serviceId, alias, methodName);
        }

        public CalculatorPOJOBuilder setOne(Number one) {
            this.one = one;
            return this;
        }

        public CalculatorPOJOBuilder setTwo(Number two) {
            this.two = two;
            return this;
        }

        public CalculatorPOJOBuilder setOperation(String operation) {
            this.operation = operation;
            return this;
        }

        public CalculatorPOJOBuilder setResult(Number result) {
            this.result = result;
            return this;
        }

        public CalculatorPOJO build() {
            return new CalculatorPOJO(this);
        }
    }
}

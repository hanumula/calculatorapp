package com.synopsys.calculator;

public class CalculatorAppException extends Exception {

    private String errorMessage;

    public CalculatorAppException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}

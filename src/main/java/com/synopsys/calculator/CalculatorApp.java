package com.synopsys.calculator;

public class CalculatorApp {
    /**
     * Main method for calculator app
     * @param args
     * arg[0] is for the expressionInput
     * arg[1] is for the LOGGING Priority
     */
    public static void main(String[] args) throws Exception {

        Expression expressionObj = new Expression(args[0], args[1]);
        System.out.println(expressionObj.evaluateExpression());

    }
}

package com.synopsys.calculator;

public class CalculatorApp {
    public static void main(String[] args) throws Exception {

        Expression expressionObj = new Expression(args[0], args[1]);
        System.out.println(expressionObj.evaluateExpression());

    }
}

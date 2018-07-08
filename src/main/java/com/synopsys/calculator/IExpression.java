package com.synopsys.calculator;

public interface IExpression {

    Integer evaluateExpression() throws CalculatorAppException;

    int applyOp(char op, int firstVal, int secondVal) throws CalculatorAppException;

    Character charEquivForOp(String operator) throws CalculatorAppException;

}

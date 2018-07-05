package com.synopsys.calculator;

public class MessageConstants {
    public static final String INVALID_OPERATOR = "Operators other than let, add, sub, mult, div are not accepted";
    public static final String DIVISION_BY_ZERO_INVALID = "Number cannot be divided by 0";
    public static final String INTERNAL_ISSUE = "System Error during expression evaluation";
    public static final String NUMBER_FORMAT_EXCEPTION_MSG = "Format exception happened while parsing integer. Check if the numbers in the expression are Integer.MIN_VALUE and Integer.MAX_VALUE";
    public static final String INVALID_CHARACTER_FOUND = "Invalid character found in the expression input. Only characters with numbers, a-z, A-Z, (, ), comma, space are allowed in the input expression.";
}

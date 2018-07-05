package com.synopsys.calculator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CalculatorApp {

    private static Logger logger = LogManager.getLogger(CalculatorApp.class.getName());

    /**
     * Main method for calculator app
     * @param args
     * arg[0] is for the expressionInput
     * arg[1] is for the LOGGING Priority
     */
    public static void main(String[] args) throws CalculatorAppException {

        Expression expressionObj = new Expression(args[0], args[1]);
        Integer result = null;
        try {
            result = expressionObj.evaluateExpression();
        } catch (Exception exception) {
            expressionObj.logMessage(MessageConstants.INTERNAL_ISSUE + exception.getMessage());
            throw new CalculatorAppException(MessageConstants.INTERNAL_ISSUE);
        }
        logger.info(result); // Using INFO in case of result logging. Using command line logging priority in all other cases

    }
}

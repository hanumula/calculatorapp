package com.synopsys.calculator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Expression {

    private String expressionInput;
    private char[] tokens;
    private Stack<Integer> values;
    private Stack<Character> ops;
    private Stack<Character> vars;
    private Map<Character,Integer> map;
    private Integer index;
    private String logPriority;

    private final static Logger logger = LogManager.getLogger(CalculatorApp.class.getName());

    /**
     *
     * @param expressionInput
     * @param logPriority
     */
    public Expression(String expressionInput, String logPriority) {
        this.expressionInput = expressionInput; //expression input
        this.tokens = this.expressionInput.toCharArray(); //fetch all chars in expression input
        this.values = new Stack<>(); //instantiate a stack for values during exp evaluation
        this.ops = new Stack<>(); // instantiate a stack for operators during exp evaluation
        this.vars = new Stack<>(); // instantiate a stack for variables that need to be evaluated in the expression
        this.map = new HashMap<>(); // maintain a map for variable input key value pairs
        this.index = 0; // maintain index to scan through the expression character by character
        this.logPriority = logPriority; // this fetches the log priority chosen by the user
    }

    /**
     * this evaluates the expression input and returns the result
     * @return
     * @throws Exception
     */
    public Integer evaluateExpression() throws CalculatorAppException {
        try {
            while(index<this.tokens.length){ //loop through all the characters in the expression to evaluate it
                if(this.tokens[index] == ' ' || this.tokens[index] == ','){ // increment the loop and continue for space and comma
                    this.index++;
                    continue;
                }
                if(this.tokens[index] >= 65 && this.tokens[index] <=122){ // check for text in the expression input and translate it to variables or operators
                    String str = getOperand();
                    if(str.length()>1){
                        this.ops.push(charEquivForOp(str));
                    } else {
                        if(this.map.get(str.charAt(0)) != null) {
                            calcVarValue(str.charAt(0));
                        } else {
                            this.vars.push(str.charAt(0));
                            this.map.put(str.charAt(0),null);
                        }
                    }
                    if(this.tokens[index] == '('){
                        this.ops.push(this.tokens[index]);
                    }
                } else if(this.tokens[index] == '('){ // push the opening parenthesis into ops stack
                    this.ops.push('(');
                } else if(tokens[index] == ')'){ // evaluate the expression so far when closing parenthesis is encountered
                    if(!ops.isEmpty()) {
                        char temp = ops.pop();
                        if (!ops.isEmpty()&& ops.peek() == '=') {
                            this.values.push(calcValue());
                        } else if (!ops.isEmpty() && values.size() >= 2) {
                            this.values.push(calcValue());
                        } else {
                            ops.push(temp);
                        }
                    }
                    fillValue();
                } else if(this.tokens[index] >= 48 && this.tokens[index] <= 57){ // check for numbers in the expression input to push them into values stack
                    this.values.push(getValue());
                    if(this.tokens[index] == ')'){
                        this.ops.pop();
                        this.values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    fillValue();
                } else {
                    logMessage(MessageConstants.INVALID_CHARACTER_FOUND);
                    throw new CalculatorAppException(MessageConstants.INVALID_CHARACTER_FOUND);
                }
                this.index++;
            }

            return values.pop(); // return the final result
        } catch (EmptyStackException emptyStackException) { // catch runtime empty stack exception and return an appropriate message to the user
            logMessage(MessageConstants.INTERNAL_ISSUE + emptyStackException.getMessage());
            throw new CalculatorAppException(MessageConstants.INTERNAL_ISSUE);
        }
    }

    /**
     * this method scans the verbal operator part in the expressionInput and returns it as a string
     * @return
     */
    private String getOperand() {
        StringBuilder sb = new StringBuilder();
        while(index<this.tokens.length && this.tokens[index] >=65 && this.tokens[index] <=122){
            sb.append(this.tokens[index]);
            this.index ++;
        }
        return sb.toString();
    }

    /**
     * this method applies the operator on the values and returns the result
     * @return
     * @throws CalculatorAppException
     */
    private int calcValue() throws CalculatorAppException {
        int firstVal, secondVal;
        firstVal = this.values.pop();
        if(!this.values.empty()) {
            secondVal = this.values.pop();
        } else {
            secondVal = 0;
        }
        return applyOp(this.ops.pop(), firstVal, secondVal);
    }

    /**
     * this method reads the number part from the expressionInput and converts it to Integer
     * @return
     * @throws CalculatorAppException
     */
    private int getValue() throws CalculatorAppException {
        StringBuilder sb = new StringBuilder();
        Integer value;
        while(index<this.tokens.length && this.tokens[index] >= '0' && this.tokens[index] <= '9') {
            sb.append(this.tokens[index]);
            this.index++;
        }
        try {
            value = Integer.parseInt(sb.toString());
        } catch (NumberFormatException numberFormatException) {
            logMessage(MessageConstants.NUMBER_FORMAT_EXCEPTION_MSG  + numberFormatException.getMessage());
            throw new CalculatorAppException(MessageConstants.NUMBER_FORMAT_EXCEPTION_MSG);
        }
        return value;
    }

    /**
     * this method finds and fills the value for the variable key provided in the expressionInput
     */
    private void fillValue() {
        if(!this.vars.empty() && this.map.get(this.vars.peek()) == null && this.ops.peek()=='('){
            this.ops.pop();
            if(this.ops.peek()=='='){
                this.map.put(this.vars.pop(), this.values.pop());
                this.ops.pop();
            } else {
                this.ops.push('(');
            }

        }
    }

    /**
     * this method substitutes the value for the variable key provided in the expressionInput
     * @param var
     */
    private void calcVarValue(Character var){
        if(this.ops.peek() == '('){
            this.ops.pop();
            if(this.ops.peek() == '='){
                this.vars.push(var);
                this.map.put(var,null);
            }
            else {
                this.values.push(this.map.get(var));
            }
            this.ops.push('(');
        } else {
            this.values.push(this.map.get(var));
        }
    }

    /**
     * this method applies the operator on the values and returns result
     * @param op
     * @param firstVal
     * @param secondVal
     * @return
     * @throws CalculatorAppException
     */
    private int applyOp(char op, int firstVal, int secondVal) throws CalculatorAppException {
        switch (op){
            case '+':
                return firstVal+secondVal;
            case '-':
                return secondVal-firstVal;
            case '*':
                return firstVal*secondVal;
            case '/':
                if(firstVal == 0){
                    logMessage(MessageConstants.DIVISION_BY_ZERO_INVALID);
                    throw new CalculatorAppException(MessageConstants.DIVISION_BY_ZERO_INVALID);
                }
                return secondVal/firstVal;
            case '=':
                return firstVal;
                default:
                    logMessage(MessageConstants.INVALID_OPERATOR);
                    throw new CalculatorAppException(MessageConstants.INVALID_OPERATOR);
        }
    }

    /**
     * this method substitutes the verbal operator with the symbol
     * @param operator
     * @return
     * @throws CalculatorAppException
     */
    private Character charEquivForOp(String operator) throws CalculatorAppException {
        switch (operator){
            case "add":
                return '+';
            case "sub":
                return '-';
            case "mult":
                return '*';
            case "div":
                return '/';
            case "let":
                return '=';
                default:
                    logMessage(MessageConstants.INVALID_OPERATOR);
                    throw new CalculatorAppException(MessageConstants.INVALID_OPERATOR);
        }

    }

    /**
     * this method logs message as per the logging priority chosen by the user
     * @param message
     */
    public void logMessage(String message) {
        switch (this.logPriority) {
            case "FATAL":
                logger.fatal(message);
                break;
            case "ERROR":
                logger.error(message);
                break;
            case "DEBUG":
                logger.debug(message);
                break;
            case "WARN":
                logger.warn(message);
                break;
                default:
                    logger.info(message);
        }
    }

}

package com.synopsys.calculator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Expression {

    private String expression;
    private char[] tokens;
    private Stack<Integer> values;
    private Stack<Character> ops;
    private Stack<Character> vars;
    private Map<Character,Integer> map;
    private Integer i;
    private String logPriority;

    final static Logger logger = LogManager.getLogger(CalculatorApp.class.getName());

    Expression(String expression, String logPriority) {
        this.expression = expression;
        this.tokens = this.expression.toCharArray();
        this.values = new Stack<>();
        this.ops = new Stack<>();
        this.vars = new Stack<>();
        this.map = new HashMap<>();
        this.i = 0;
        this.logPriority = logPriority;
    }

    Integer evaluateExpression() throws Exception {

        while(i<this.tokens.length){
            if(this.tokens[i] == ' ' || this.tokens[i] == ','){
                this.i++;
                continue;
            }
            if(this.tokens[i] >= 65 && this.tokens[i] <=122){
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
                if(this.tokens[i] == '('){
                    this.ops.push(this.tokens[i]);
                }
            } else if(this.tokens[i] == '('){
                this.ops.push('(');
            } else if(tokens[i] == ')'){
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
            } else if(this.tokens[i] >= 48 && this.tokens[i] <= 57){
                this.values.push(getValue());
                if(this.tokens[i] == ')'){
                    this.ops.pop();
                    this.values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                fillValue();
            }
            this.i++;
        }

        return values.pop();
    }

    private String getOperand() {
        StringBuilder sb = new StringBuilder();
        while(i<this.tokens.length && this.tokens[i] >=65 && this.tokens[i] <=122){
            sb.append(this.tokens[i]);
            this.i ++;
        }
        return sb.toString();
    }

    private int calcValue(){
        int firstVal, secondVal;
        firstVal = this.values.pop();
        if(!this.values.empty()) {
            secondVal = this.values.pop();
        } else {
            secondVal = 0;
        }
        return applyOp(this.ops.pop(), firstVal, secondVal);
    }

    private int getValue() {
        StringBuilder sb = new StringBuilder();
        while(i<this.tokens.length && this.tokens[i] >= '0' && this.tokens[i] <= '9') {
            sb.append(this.tokens[i]);
            this.i++;
        }
        return Integer.parseInt(sb.toString());
    }

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

    private void calcVarValue(Character var) {
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

    private int applyOp(char op, int a, int b){
        try {
            switch (op){
                case '+':
                    return a+b;
                case '-':
                    return b-a;
                case '*':
                    return a*b;
                case '/':
                    return b/a;
                case '=':
                    return a;
            }
        } catch (Exception e){
            logMessage(e.getMessage());
        }
        return 0;
    }

    private Character charEquivForOp(String s){
        switch (s){
            case "add":
                return '+';
            case "sub":
                return '-';
            case "mult":
                return '*';
            case "div":
                return '/';
        }
        return '=';
    }

    private void logMessage(String message) {
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

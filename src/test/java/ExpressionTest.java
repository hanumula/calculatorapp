import com.synopsys.calculator.CalculatorAppException;
import com.synopsys.calculator.Expression;
import com.synopsys.calculator.MessageConstants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {

    /**
     * test case 1
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression1() throws CalculatorAppException {
        Expression expression = new Expression("add(1,2)","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 3);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 2
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression2() throws CalculatorAppException {
        Expression expression = new Expression("add(1, mult(2, 3))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 7);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 3
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression3() throws CalculatorAppException {
        Expression expression = new Expression("mult(add(2, 2), div(9, 3))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 12);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 4
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression4() throws CalculatorAppException {
        Expression expression = new Expression("let(a, 5, add(a, a))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 10);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 5
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression5() throws CalculatorAppException {
        Expression expression = new Expression("let(a, 5, let(b, mult(a, 10), add(b, a)))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 55);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 6
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression6() throws CalculatorAppException {
        Expression expression = new Expression("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 40);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 7
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression7() throws CalculatorAppException {
        Expression expression = new Expression("let(a, mult(add(2, 2), div(9, 3)), let(b, mult(a, 10), add(b, a)))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 132);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 8
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression8() throws CalculatorAppException {
        Expression expression = new Expression("let(a, let(b, add(1, mult(2, 3)), add(b, b)), let(b, 20, add(a, b))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 34);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 9
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression9() throws CalculatorAppException {
        Expression expression = new Expression("add(let(a, 5, add(a, a)), let(a, 5, add(a, a)))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 20);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test case 10
     * @throws CalculatorAppException
     */
    @Test
    public void testEvaluateExpression10() throws CalculatorAppException {
        Expression expression = new Expression("add(let(a, 5, let(b, mult(a, 10), add(b, a))), let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))","ERROR");
        try {
            Integer result = expression.evaluateExpression();
            assertTrue("value doesn't match", result == 95);
        } catch (AssertionError assertionError){
            fail("Wrong result for expression evaluation");
        }
    }

    /**
     * test invalid operator exception
     */
    @Test
    public void testEvaluateExpressionInvalidOperationException() {
        Expression expression = new Expression("adden(1,2)","WARN");
        try {
            expression.evaluateExpression();
        } catch (CalculatorAppException ex){
            assertEquals(ex.getMessage(), MessageConstants.INVALID_OPERATOR);
        }
    }

    /**
     * test number format exception
     */
    @Test
    public void testEvaluateExpressionNumberFormatException() {
        Expression expression = new Expression("add(1353454354354354353453454535435435435435345,2)","WARN");
        try {
            expression.evaluateExpression();
        } catch (CalculatorAppException ex){
            assertTrue("No number format exception", ex.getMessage().contains(MessageConstants.NUMBER_FORMAT_EXCEPTION_MSG));
        }
    }

    /**
     * test bad input
     */
    @Test
    public void testEvaluateInvalidCharacterFoundException() {
        Expression expression = new Expression("add(1?,2)","WARN");
        try {
            expression.evaluateExpression();
        } catch (CalculatorAppException ex){
            assertTrue("No invalid character exception", ex.getMessage().contains(MessageConstants.INVALID_CHARACTER_FOUND));
        }
    }

    // test divide by 0 exception
    @Test
    public void testEvaluateDivisionByZeroException() {
        Expression expression = new Expression("div(1,0)","WARN");
        try {
            expression.evaluateExpression();
        } catch (CalculatorAppException ex){
            assertTrue("No div by 0 exception", ex.getMessage().contains(MessageConstants.DIVISION_BY_ZERO_INVALID));
        }
    }

}

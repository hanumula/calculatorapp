import com.synopsys.calculator.CalculatorApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CalculatorAppTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setup() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testAppConstructor() {
        try {
            new CalculatorApp();
        } catch (Exception e) {
            fail("Construction failed.");
        }
    }

    @Test
    public void testAppMain1() throws Exception {
        String[] args = {"add(1,2)","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("3", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain2() throws Exception {
        String[] args = {"add(1, mult(2, 3))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("7", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain3() throws Exception {
        String[] args = {"mult(add(2, 2), div(9, 3))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("12", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain4() throws Exception {
        String[] args = {"let(a, 5, add(a, a))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("10", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain5() throws Exception {
        String[] args = {"let(a, 5, let(b, mult(a, 10), add(b, a)))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("55", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain6() throws Exception {
        String[] args = {"let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("40", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain7() throws Exception {
        String[] args = {"let(a, mult(add(2, 2), div(9, 3)), let(b, mult(a, 10), add(b, a)))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("132", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain8() throws Exception {
        String[] args = {"let(a, let(b, add(1, mult(2, 3)), add(b, b)), let(b, 20, add(a, b))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("34", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain9() throws Exception {
        String[] args = {"add(let(a, 5, add(a, a)), let(a, 5, add(a, a)))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("20", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @Test
    public void testAppMain10() throws Exception {
        String[] args = {"add(let(a, 5, let(b, mult(a, 10), add(b, a))), let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))","INFO"};
        CalculatorApp.main(args);

        try{
            assertEquals("95", outContent.toString().trim());
        } catch (AssertionError assertionError){
            fail("Wrong value for expression evaluation");
        }
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }
}


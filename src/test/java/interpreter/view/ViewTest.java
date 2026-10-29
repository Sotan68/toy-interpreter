package interpreter.view;

import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {

    @Test
    void viewRunsExample1AndExits() {
        String input = "1\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBytes);

        View view = new View(in, out);
        view.setDisplayStepByStep(false);
        view.start();

        String output = outBytes.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Final Output: {2}"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void viewHandlesInvalidOptionGracefully() {
        String input = "xyz\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBytes);

        View view = new View(in, out);
        view.start();

        String output = outBytes.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Invalid option. Please try again."));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void viewCatchesExecutionErrorsWithoutCrashing() {
        IStmt faultyPrg = new CompStmt(
                new VarDeclStmt("x", new IntType()),
                new AssignStmt("x", new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(10)), new ValueExp(new IntValue(0))))
        );

        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBytes);

        View view = new View(new ByteArrayInputStream(new byte[0]), out);
        view.runProgram(faultyPrg);

        String output = outBytes.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Execution Error: division by zero"));
    }

    @Test
    void toggleStepByStepDisplay() {
        View view = new View();
        assertTrue(view.isDisplayStepByStep());
        view.setDisplayStepByStep(false);
        assertFalse(view.isDisplayStepByStep());
    }
}


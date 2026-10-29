package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), null);
    }

    @Test
    void executeAppendsEvaluatedValueToOut() throws MyException {
        IStmt printStmt = new PrintStmt(new ValueExp(new IntValue(100)));
        printStmt.execute(state);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(100), state.getOut().get(0));
    }

    @Test
    void executePropagatesExpressionErrors() {
        // e.g. undefined variable
        IStmt printStmt = new PrintStmt(new VarExp("notDefined"));
        assertThrows(ExpressionException.class, () -> printStmt.execute(state));

        // e.g. division by zero
        IStmt divZeroPrint = new PrintStmt(new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(0))));
        assertThrows(ExpressionException.class, () -> divZeroPrint.execute(state));
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt printStmt = new PrintStmt(new VarExp("v"));
        assertEquals("print(v)", printStmt.toString());
    }
}


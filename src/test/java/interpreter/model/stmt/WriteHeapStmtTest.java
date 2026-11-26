package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteHeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(null);
        new VarDeclStmt("v", new RefType(new IntType())).execute(state); // Ref int v
    }

    @Test
    void changesTheHeapLocationAndKeepsTheVariable() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);

        assertEquals(new IntValue(30), state.getHeap().get(1));
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
    }

    @Test
    void undeclaredVariableThrows() {
        var stmt = new WriteHeapStmt("x", new ValueExp(new IntValue(1)));
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("wH: x is not declared", ex.getMessage());
    }

    @Test
    void nonReferenceVariableThrows() throws MyException {
        new VarDeclStmt("n", new IntType()).execute(state);
        var stmt = new WriteHeapStmt("n", new ValueExp(new IntValue(1)));
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("wH: n is not a reference", ex.getMessage());
    }

    @Test
    void addressNotInTheHeapThrows() {
        var stmt = new WriteHeapStmt("v", new ValueExp(new IntValue(1))); // v is still (0, int)
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("wH: address 0 is not in the heap", ex.getMessage());
    }

    @Test
    void valueOfTheWrongTypeThrowsAndKeepsTheOldValue() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        var stmt = new WriteHeapStmt("v", new ValueExp(new BoolValue(true)));

        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("wH: true has type bool, expected int", ex.getMessage());
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void toStringFormatsCorrectly() {
        assertEquals("wH(v, 30)", new WriteHeapStmt("v", new ValueExp(new IntValue(30))).toString());
    }
}

package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(null);
        new VarDeclStmt("v", new RefType(new IntType())).execute(state); // Ref int v
    }

    @Test
    void allocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);

        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void secondAllocationUsesANewAddressAndKeepsTheOldOne() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new NewStmt("v", new ValueExp(new IntValue(30))).execute(state);

        assertEquals(new RefValue(2, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
        assertEquals(new IntValue(30), state.getHeap().get(2));
    }

    @Test
    void allocatesAReferenceToAReference() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new VarDeclStmt("a", new RefType(new RefType(new IntType()))).execute(state);
        new NewStmt("a", new VarExp("v")).execute(state);

        assertEquals(new RefValue(2, new RefType(new IntType())), state.getSymTable().lookup("a"));
        assertEquals(new RefValue(1, new IntType()), state.getHeap().get(2));
    }

    @Test
    void undeclaredVariableThrows() {
        var stmt = new NewStmt("x", new ValueExp(new IntValue(1)));
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("new: x is not declared", ex.getMessage());
    }

    @Test
    void nonReferenceVariableThrows() throws MyException {
        new VarDeclStmt("n", new IntType()).execute(state);
        var stmt = new NewStmt("n", new ValueExp(new IntValue(1)));
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("new: n is not a reference", ex.getMessage());
    }

    @Test
    void valueOfTheWrongTypeThrowsAndAllocatesNothing() {
        var stmt = new NewStmt("v", new ValueExp(new BoolValue(true)));
        var ex = assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals("new: true has type bool, expected int", ex.getMessage());
        assertTrue(state.getHeap().getContent().isEmpty());
    }

    @Test
    void toStringFormatsCorrectly() {
        assertEquals("new(v, 20)", new NewStmt("v", new ValueExp(new IntValue(20))).toString());
    }
}

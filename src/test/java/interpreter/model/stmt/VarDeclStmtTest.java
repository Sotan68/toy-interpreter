package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), null);
    }

    @Test
    void declaresIntVariableWithDefaultValue() throws MyException {
        IStmt decl = new VarDeclStmt("x", new IntType());
        decl.execute(state);

        assertTrue(state.getSymTable().isDefined("x"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void declaresBoolVariableWithDefaultValue() throws MyException {
        IStmt decl = new VarDeclStmt("flag", new BoolType());
        decl.execute(state);

        assertTrue(state.getSymTable().isDefined("flag"));
        assertEquals(new BoolValue(false), state.getSymTable().lookup("flag"));
    }

    @Test
    void declaresStringVariableWithDefaultValue() throws MyException {
        IStmt decl = new VarDeclStmt("s", new StringType());
        decl.execute(state);

        assertTrue(state.getSymTable().isDefined("s"));
        assertEquals(new StringValue(""), state.getSymTable().lookup("s"));
    }

    @Test
    void redeclaringVariableThrows() throws MyException {
        IStmt decl1 = new VarDeclStmt("x", new IntType());
        decl1.execute(state);

        IStmt decl2 = new VarDeclStmt("x", new IntType());
        var ex = assertThrows(StatementException.class, () -> decl2.execute(state));
        assertEquals("variable x is already declared", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt decl = new VarDeclStmt("x", new IntType());
        assertEquals("int x", decl.toString());
    }
}

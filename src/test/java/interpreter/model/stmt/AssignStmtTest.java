package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), null);
    }

    @Test
    void assignsValueToDeclaredVariable() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        IStmt assign = new AssignStmt("v", new ValueExp(new IntValue(42)));
        assign.execute(state);

        assertEquals(new IntValue(42), state.getSymTable().lookup("v"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        IStmt assign = new AssignStmt("notDeclared", new ValueExp(new IntValue(10)));
        var ex = assertThrows(StatementException.class, () -> assign.execute(state));
        assertEquals("variable notDeclared was not declared", ex.getMessage());
    }

    @Test
    void assignmentWithTypeMismatchThrows() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        IStmt assign = new AssignStmt("v", new ValueExp(new BoolValue(true)));
        var ex = assertThrows(StatementException.class, () -> assign.execute(state));
        assertEquals("declared type of variable v and type of the assigned expression do not match", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt assign = new AssignStmt("v", new ValueExp(new IntValue(2)));
        assertEquals("v = 2", assign.toString());
    }
}


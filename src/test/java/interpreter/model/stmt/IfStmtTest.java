package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IfStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), null);
    }

    @Test
    void executePushesThenBranchWhenConditionIsTrue() throws MyException {
        IStmt thenBranch = new AssignStmt("x", new ValueExp(new IntValue(1)));
        IStmt elseBranch = new AssignStmt("x", new ValueExp(new IntValue(2)));
        IStmt ifStmt = new IfStmt(new ValueExp(new BoolValue(true)), thenBranch, elseBranch);

        ifStmt.execute(state);

        assertSame(thenBranch, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void executePushesElseBranchWhenConditionIsFalse() throws MyException {
        IStmt thenBranch = new AssignStmt("x", new ValueExp(new IntValue(1)));
        IStmt elseBranch = new AssignStmt("x", new ValueExp(new IntValue(2)));
        IStmt ifStmt = new IfStmt(new ValueExp(new BoolValue(false)), thenBranch, elseBranch);

        ifStmt.execute(state);

        assertSame(elseBranch, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void executeWithNonBooleanConditionThrows() {
        IStmt thenBranch = new NopStmt();
        IStmt elseBranch = new NopStmt();
        IStmt ifStmt = new IfStmt(new ValueExp(new IntValue(1)), thenBranch, elseBranch);

        var ex = assertThrows(StatementException.class, () -> ifStmt.execute(state));
        assertEquals("conditional expression is not a boolean", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt ifStmt = new IfStmt(new ValueExp(new BoolValue(true)), new NopStmt(), new NopStmt());
        assertEquals("(if true then nop else nop)", ifStmt.toString());
    }
}


package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = PrgState.of(null);
    }

    @Test
    void executePushesSecondThenFirstOntoStack() throws MyException {
        IStmt s1 = new NopStmt();
        IStmt s2 = new NopStmt();
        IStmt comp = new CompStmt(s1, s2);

        comp.execute(state);

        // s1 was pushed last, so it should be on top
        assertSame(s1, state.getExeStack().pop());
        assertSame(s2, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt comp = new CompStmt(new NopStmt(), new NopStmt());
        assertEquals("(nop; nop)", comp.toString());
    }
}


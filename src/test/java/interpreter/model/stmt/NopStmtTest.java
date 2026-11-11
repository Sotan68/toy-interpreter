package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = PrgState.of(null);
    }

    @Test
    void executeLeavesStateUnchanged() throws MyException {
        IStmt nop = new NopStmt();
        PrgState result = nop.execute(state);

        assertSame(state, result);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt nop = new NopStmt();
        assertEquals("nop", nop.toString());
    }
}


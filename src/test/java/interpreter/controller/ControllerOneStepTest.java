package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerOneStepTest {
    private PrgState state;
    private IRepository repo;
    private Controller controller;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), null);
        repo = new Repository(state);
        controller = new Controller(repo, false);
    }

    @Test
    void oneStepOnEmptyStackThrows() {
        var ex = assertThrows(StatementException.class, () -> controller.oneStep(state));
        assertEquals("program state stack is empty", ex.getMessage());
    }

    @Test
    void oneStepExecutesTopStatement() throws MyException {
        IStmt stmt = new VarDeclStmt("x", new IntType());
        state.getExeStack().push(stmt);

        controller.oneStep(state);

        assertTrue(state.getExeStack().isEmpty());
        assertTrue(state.getSymTable().isDefined("x"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void oneStepExecutesSequentiallyUntilEmpty() throws MyException {
        state.getExeStack().push(new AssignStmt("x", new ValueExp(new IntValue(10))));
        state.getExeStack().push(new VarDeclStmt("x", new IntType()));

        // Step 1: execute declaration
        controller.oneStep(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
        assertEquals(1, ((MyStack<?>) state.getExeStack()).isEmpty() ? 0 : 1);

        // Step 2: execute assignment
        controller.oneStep(state);
        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));
        assertTrue(state.getExeStack().isEmpty());

        // Step 3: empty stack throws
        assertThrows(StatementException.class, () -> controller.oneStep(state));
    }

    @Test
    void displayFlagGettersAndSettersWork() {
        assertFalse(controller.isDisplayFlag());
        controller.setDisplayFlag(true);
        assertTrue(controller.isDisplayFlag());
        assertSame(repo, controller.getRepo());
    }
}


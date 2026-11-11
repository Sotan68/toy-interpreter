package interpreter.controller;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerAllStepTest {

    @Test
    void allStepExecutesProgramUntilStackIsEmpty() throws MyException {
        // int v; v = 10; print(v)
        IStmt prg = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(10))),
                        new PrintStmt(new VarExp("v"))
                )
        );

        PrgState state = PrgState.of(prg);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(10), state.getSymTable().lookup("v"));
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(10), state.getOut().get(0));
    }

    @Test
    void allStepWithEmptyRepositoryThrows() {
        IRepository repo = new Repository();
        Controller controller = new Controller(repo, false);

        var ex = assertThrows(StatementException.class, controller::allStep);
        assertEquals("repository has no program state", ex.getMessage());
    }

    @Test
    void allStepPropagatesRuntimeErrors() {
        // int x; x = 5 / 0
        IStmt prg = new CompStmt(
                new VarDeclStmt("x", new IntType()),
                new AssignStmt("x", new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(0))))
        );

        PrgState state = PrgState.of(prg);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo, false);

        var ex = assertThrows(ExpressionException.class, controller::allStep);
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void allStepRunsWithDisplayFlagEnabled() throws MyException {
        IStmt prg = new VarDeclStmt("a", new IntType());
        PrgState state = PrgState.of(prg);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo, true);

        controller.allStep();
        assertTrue(state.getExeStack().isEmpty());
    }
}


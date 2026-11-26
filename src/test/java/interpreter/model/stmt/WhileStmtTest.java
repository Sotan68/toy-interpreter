package interpreter.model.stmt;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTest {

    // int v; v = start; (while (v > 0) (print(v); v = v - 1))
    private static PrgState runCountdown(int start) throws MyException {
        IStmt body = new CompStmt(
                new PrintStmt(new VarExp("v")),
                new AssignStmt("v", new ArithExp(ArithOp.SUB, new VarExp("v"), new ValueExp(new IntValue(1))))
        );
        IStmt program = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(start))),
                        new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                body
                        )
                )
        );
        PrgState state = PrgState.of(program);
        new Controller(new Repository(state)).allStep();
        return state;
    }

    @Test
    void loopRunsZeroTimes() throws MyException {
        PrgState state = runCountdown(0);

        assertEquals(0, state.getOut().size());
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void loopRunsOnce() throws MyException {
        PrgState state = runCountdown(1);

        assertEquals("{1}", state.getOut().toString());
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void loopRunsSeveralTimes() throws MyException {
        PrgState state = runCountdown(4);

        assertEquals("{4, 3, 2, 1}", state.getOut().toString());
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void trueConditionPushesTheBodyOnTopOfTheLoop() throws MyException {
        PrgState state = PrgState.of(null);
        IStmt body = new NopStmt();
        IStmt loop = new WhileStmt(new ValueExp(new BoolValue(true)), body);

        loop.execute(state);

        assertSame(body, state.getExeStack().pop());
        assertSame(loop, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void falseConditionPushesNothing() throws MyException {
        PrgState state = PrgState.of(null);

        new WhileStmt(new ValueExp(new BoolValue(false)), new NopStmt()).execute(state);

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void nonBooleanConditionThrows() {
        PrgState state = PrgState.of(null);
        var loop = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());

        var ex = assertThrows(StatementException.class, () -> loop.execute(state));
        assertEquals("while: condition 1 is not a boolean", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt loop = new WhileStmt(new VarExp("b"), new NopStmt());
        assertEquals("(while (b) nop)", loop.toString());
    }
}

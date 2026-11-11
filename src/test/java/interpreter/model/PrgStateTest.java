package interpreter.model;

import interpreter.exceptions.ADTException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateTest {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IStmt program;

    @BeforeEach
    void setUp() {
        exeStack = new MyStack<>();
        symTable = new MyDictionary<>();
        out = new MyList<>();
        fileTable = new MyDictionary<>();
        program = new NopStmt();
    }

    @Test
    void constructorPushesProgramOntoExeStack() throws ADTException {
        PrgState state = new PrgState(exeStack, symTable, out, fileTable, program);

        assertFalse(state.getExeStack().isEmpty());
        assertSame(program, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void constructorWithNullProgramLeavesExeStackEmpty() {
        PrgState state = new PrgState(exeStack, symTable, out, fileTable, null);

        assertTrue(state.getExeStack().isEmpty());
        assertNull(state.getOriginalProgram());
    }

    @Test
    void ofFactoryMethodCreatesInitializedState() throws ADTException {
        PrgState state = PrgState.of(program);

        assertNotNull(state.getExeStack());
        assertNotNull(state.getSymTable());
        assertNotNull(state.getOut());
        assertNotNull(state.getFileTable());
        assertSame(program, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        PrgState state = new PrgState(exeStack, symTable, out, fileTable, program);

        IStack<IStmt> newStack = new MyStack<>();
        IDictionary<String, IValue> newSymTable = new MyDictionary<>();
        IList<IValue> newOut = new MyList<>();
        IDictionary<StringValue, BufferedReader> newFileTable = new MyDictionary<>();

        state.setExeStack(newStack);
        state.setSymTable(newSymTable);
        state.setOut(newOut);
        state.setFileTable(newFileTable);

        assertSame(newStack, state.getExeStack());
        assertSame(newSymTable, state.getSymTable());
        assertSame(newOut, state.getOut());
        assertSame(newFileTable, state.getFileTable());
        assertSame(program, state.getOriginalProgram());
    }

    @Test
    void toStringIncludesAllComponents() {
        symTable.put("x", new IntValue(10));
        out.add(new IntValue(20));
        PrgState state = new PrgState(exeStack, symTable, out, fileTable, program);

        String repr = state.toString();
        assertTrue(repr.contains("ExeStack: {nop}"));
        assertTrue(repr.contains("SymTable: {x -> 10}"));
        assertTrue(repr.contains("Out: {20}"));
        assertTrue(repr.contains("FileTable: {}"));
    }
}

package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = PrgState.of(null);
    }

    @Test
    void readsIntegerSuccessfully(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("numbers.txt");
        Files.writeString(filePath, "15\n50\n");

        StringValue strVal = new StringValue(filePath.toString());
        new OpenRFileStmt(new ValueExp(strVal)).execute(state);

        new VarDeclStmt("x", new IntType()).execute(state);
        new VarDeclStmt("y", new IntType()).execute(state);

        new ReadFileStmt(new ValueExp(strVal), "x").execute(state);
        assertEquals(new IntValue(15), state.getSymTable().lookup("x"));

        new ReadFileStmt(new ValueExp(strVal), "y").execute(state);
        assertEquals(new IntValue(50), state.getSymTable().lookup("y"));

        new CloseRFileStmt(new ValueExp(strVal)).execute(state);
    }

    @Test
    void readsZeroAtEndOfFileOrEmptyFile(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("empty.txt");
        Files.writeString(filePath, "");

        StringValue strVal = new StringValue(filePath.toString());
        new OpenRFileStmt(new ValueExp(strVal)).execute(state);
        new VarDeclStmt("c", new IntType()).execute(state);

        new ReadFileStmt(new ValueExp(strVal), "c").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("c"));

        new CloseRFileStmt(new ValueExp(strVal)).execute(state);
    }

    @Test
    void undeclaredVariableThrows(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("numbers.txt");
        Files.writeString(filePath, "42\n");

        StringValue strVal = new StringValue(filePath.toString());
        new OpenRFileStmt(new ValueExp(strVal)).execute(state);

        IStmt readStmt = new ReadFileStmt(new ValueExp(strVal), "notDeclared");
        var ex = assertThrows(StatementException.class, () -> readStmt.execute(state));
        assertTrue(ex.getMessage().contains("was not declared"));

        new CloseRFileStmt(new ValueExp(strVal)).execute(state);
    }

    @Test
    void variableNotIntegerTypeThrows(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("numbers.txt");
        Files.writeString(filePath, "42\n");

        StringValue strVal = new StringValue(filePath.toString());
        new OpenRFileStmt(new ValueExp(strVal)).execute(state);

        new VarDeclStmt("b", new BoolType()).execute(state);

        IStmt readStmt = new ReadFileStmt(new ValueExp(strVal), "b");
        var ex = assertThrows(StatementException.class, () -> readStmt.execute(state));
        assertTrue(ex.getMessage().contains("not of type int"));

        new CloseRFileStmt(new ValueExp(strVal)).execute(state);
    }

    @Test
    void expressionNotStringThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        IStmt readStmt = new ReadFileStmt(new ValueExp(new IntValue(10)), "x");
        var ex = assertThrows(StatementException.class, () -> readStmt.execute(state));
        assertTrue(ex.getMessage().contains("not a string"));
    }

    @Test
    void fileNotOpenedThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        IStmt readStmt = new ReadFileStmt(new ValueExp(new StringValue("unopened.txt")), "x");
        var ex = assertThrows(StatementException.class, () -> readStmt.execute(state));
        assertTrue(ex.getMessage().contains("not opened"));
    }

    @Test
    void nonIntegerContentInFileThrows(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("invalid.txt");
        Files.writeString(filePath, "not_a_number\n");

        StringValue strVal = new StringValue(filePath.toString());
        new OpenRFileStmt(new ValueExp(strVal)).execute(state);
        new VarDeclStmt("x", new IntType()).execute(state);

        IStmt readStmt = new ReadFileStmt(new ValueExp(strVal), "x");
        var ex = assertThrows(StatementException.class, () -> readStmt.execute(state));
        assertTrue(ex.getMessage().contains("not a valid integer"));

        new CloseRFileStmt(new ValueExp(strVal)).execute(state);
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt readStmt = new ReadFileStmt(new ValueExp(new StringValue("test.in")), "var_c");
        assertEquals("readFile(test.in, var_c)", readStmt.toString());
    }
}


package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class OpenRFileStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = PrgState.of(null);
    }

    @Test
    void opensFileSuccessfully(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("test.txt");
        Files.writeString(filePath, "sample content\n");

        StringValue strVal = new StringValue(filePath.toString());
        IStmt openStmt = new OpenRFileStmt(new ValueExp(strVal));
        openStmt.execute(state);

        assertTrue(state.getFileTable().isDefined(strVal));
        assertNotNull(state.getFileTable().lookup(strVal));

        // Clean up
        state.getFileTable().lookup(strVal).close();
    }

    @Test
    void expressionNotStringThrows() {
        IStmt openStmt = new OpenRFileStmt(new ValueExp(new IntValue(123)));
        var ex = assertThrows(StatementException.class, () -> openStmt.execute(state));
        assertTrue(ex.getMessage().contains("not a string"));
    }

    @Test
    void fileDoesNotExistThrows() {
        IStmt openStmt = new OpenRFileStmt(new ValueExp(new StringValue("non_existent_file_xyz_123.txt")));
        var ex = assertThrows(StatementException.class, () -> openStmt.execute(state));
        assertTrue(ex.getMessage().contains("could not open file"));
    }

    @Test
    void fileAlreadyOpenedThrows(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("test.txt");
        Files.writeString(filePath, "data");

        StringValue strVal = new StringValue(filePath.toString());
        IStmt openStmt = new OpenRFileStmt(new ValueExp(strVal));
        openStmt.execute(state);

        var ex = assertThrows(StatementException.class, () -> openStmt.execute(state));
        assertTrue(ex.getMessage().contains("already opened"));

        // Clean up
        state.getFileTable().lookup(strVal).close();
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt openStmt = new OpenRFileStmt(new ValueExp(new StringValue("test.in")));
        assertEquals("openRFile(test.in)", openStmt.toString());
    }
}


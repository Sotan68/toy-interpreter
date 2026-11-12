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

class CloseRFileStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = PrgState.of(null);
    }

    @Test
    void closesFileSuccessfully(@TempDir Path tempDir) throws IOException, MyException {
        Path filePath = tempDir.resolve("close_me.txt");
        Files.writeString(filePath, "data");

        StringValue strVal = new StringValue(filePath.toString());
        new OpenRFileStmt(new ValueExp(strVal)).execute(state);
        assertTrue(state.getFileTable().isDefined(strVal));

        IStmt closeStmt = new CloseRFileStmt(new ValueExp(strVal));
        closeStmt.execute(state);

        assertFalse(state.getFileTable().isDefined(strVal));
    }

    @Test
    void expressionNotStringThrows() {
        IStmt closeStmt = new CloseRFileStmt(new ValueExp(new IntValue(42)));
        var ex = assertThrows(StatementException.class, () -> closeStmt.execute(state));
        assertTrue(ex.getMessage().contains("not a string"));
    }

    @Test
    void fileNotOpenedThrows() {
        IStmt closeStmt = new CloseRFileStmt(new ValueExp(new StringValue("unopened.txt")));
        var ex = assertThrows(StatementException.class, () -> closeStmt.execute(state));
        assertTrue(ex.getMessage().contains("not opened"));
    }

    @Test
    void toStringFormatsCorrectly() {
        IStmt closeStmt = new CloseRFileStmt(new ValueExp(new StringValue("test.in")));
        assertEquals("closeRFile(test.in)", closeStmt.toString());
    }
}


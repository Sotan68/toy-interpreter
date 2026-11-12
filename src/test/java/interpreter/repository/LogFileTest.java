package interpreter.repository;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LogFileTest {

    @Test
    void allStepLogsEveryStateWithFourSectionsInOrder(@TempDir Path tempDir) throws IOException, MyException {
        Path logPath = tempDir.resolve("test_log.txt");

        // int v; v = 2; print(v)
        IStmt prg = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );

        PrgState state = PrgState.of(prg);
        IRepository repo = new Repository(state, logPath.toString());
        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertTrue(Files.exists(logPath));
        String content = Files.readString(logPath);
        assertFalse(content.isEmpty());

        // Verify that the four sections appear in order in the log
        int exeIdx = content.indexOf("ExeStack");
        int symIdx = content.indexOf("SymTable");
        int outIdx = content.indexOf("Out");
        int fileIdx = content.indexOf("FileTable");

        assertTrue(exeIdx != -1, "ExeStack must be present in log");
        assertTrue(symIdx != -1, "SymTable must be present in log");
        assertTrue(outIdx != -1, "Out must be present in log");
        assertTrue(fileIdx != -1, "FileTable must be present in log");

        assertTrue(exeIdx < symIdx, "ExeStack must precede SymTable");
        assertTrue(symIdx < outIdx, "SymTable must precede Out");
        assertTrue(outIdx < fileIdx, "Out must precede FileTable");

        // Verify that allStep logged multiple states (append mode works)
        int firstExe = content.indexOf("ExeStack");
        int secondExe = content.indexOf("ExeStack", firstExe + 1);
        assertTrue(secondExe != -1, "allStep must log multiple intermediate states to the file");

        // Final output {2} must be recorded in the final logged state
        assertTrue(content.contains("{2}"));
    }

    @Test
    void logPrgStateExecDirectCallAppends(@TempDir Path tempDir) throws IOException, MyException {
        Path logPath = tempDir.resolve("direct_log.txt");
        PrgState state = PrgState.of(new VarDeclStmt("x", new IntType()));
        IRepository repo = new Repository(state, logPath.toString());

        repo.logPrgStateExec();
        repo.logPrgStateExec(state);

        String content = Files.readString(logPath);
        int firstOccur = content.indexOf("ExeStack");
        int secondOccur = content.indexOf("ExeStack", firstOccur + 1);
        assertTrue(secondOccur != -1, "logPrgStateExec must append to log file");
    }

    @Test
    void logPrgStateExecWithNoLogPathDoesNothing() {
        PrgState state = PrgState.of(null);
        IRepository repo = new Repository(state);
        assertDoesNotThrow(() -> repo.logPrgStateExec());
        assertDoesNotThrow(() -> repo.logPrgStateExec(state));
    }
}


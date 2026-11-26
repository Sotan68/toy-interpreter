package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ControllerGarbageCollectionTest {

    // builds s1; (s2; (...; sn))
    private static IStmt sequence(IStmt... statements) {
        IStmt program = statements[statements.length - 1];
        for (int i = statements.length - 2; i >= 0; i--) {
            program = new CompStmt(statements[i], program);
        }
        return program;
    }

    @Test
    void keepsALocationThatIsReachableOnlyThroughTheHeap() throws MyException {
        // Ex9: Ref int v; new(v, 20); Ref Ref int a; new(a, v); new(v, 30); print(rH(rH(a)))
        IStmt program = sequence(
                new VarDeclStmt("v", new RefType(new IntType())),
                new NewStmt("v", new ValueExp(new IntValue(20))),
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                new NewStmt("a", new VarExp("v")),
                new NewStmt("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new ReadHeapExp(new ReadHeapExp(new VarExp("a"))))
        );
        PrgState state = PrgState.of(program);

        new Controller(new Repository(state)).allStep();

        assertEquals("{20}", state.getOut().toString());
        assertEquals(Set.of(1, 2, 3), state.getHeap().getContent().keySet());
    }

    @Test
    void removesGarbageAndLogsTheStateBeforeAndAfter(@TempDir Path tempDir) throws MyException, IOException {
        // Ref int v; new(v, 20); new(v, 30)  -> address 1 becomes garbage
        IStmt program = sequence(
                new VarDeclStmt("v", new RefType(new IntType())),
                new NewStmt("v", new ValueExp(new IntValue(20))),
                new NewStmt("v", new ValueExp(new IntValue(30)))
        );
        Path log = tempDir.resolve("gc_log.txt");
        PrgState state = PrgState.of(program);

        new Controller(new Repository(state, log.toString())).allStep();

        assertEquals(Set.of(2), state.getHeap().getContent().keySet());
        String content = Files.readString(log);
        int before = content.indexOf("Heap:\n1 --> 20\n2 --> 30\n");
        int after = content.indexOf("Heap:\n2 --> 30\n");
        assertTrue(before != -1, "state before the collection must be logged");
        assertTrue(after > before, "state after the collection must be logged");
    }
}

package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static interpreter.view.Examples.EX5;
import static org.junit.jupiter.api.Assertions.*;

class ExamplesTest {

    // -------------------------------------------------------------------------
    // Ex4 helpers
    // -------------------------------------------------------------------------

    /** Builds the Ex4 program using the given absolute file path string. */
    private static IStmt ex4WithPath(String filePath) {
        return new CompStmt(
                new VarDeclStmt("fname", new StringType()),
                new CompStmt(
                        new VarDeclStmt("r", new IntType()),
                        new CompStmt(
                                new AssignStmt("fname", new ValueExp(new StringValue(filePath))),
                                new CompStmt(
                                        new OpenRFileStmt(new VarExp("fname")),
                                        new CompStmt(
                                                new ReadFileStmt(new VarExp("fname"), "r"),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("r")),
                                                        new CompStmt(
                                                                new ReadFileStmt(new VarExp("fname"), "r"),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("r")),
                                                                        new CloseRFileStmt(new VarExp("fname"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static Controller controllerFor(IStmt program) {
        PrgState state = PrgState.of(program);
        Repository repo = new Repository(state);
        return new Controller(repo);
    }

    // -------------------------------------------------------------------------
    // Ex4 — file with 15 and 50
    // -------------------------------------------------------------------------

    @Test
    void ex4PrintsFifteenAndFifty(@TempDir Path tmpDir) throws MyException, IOException {
        Path testIn = tmpDir.resolve("test.in");
        Files.writeString(testIn, "15\n50\n");

        Controller ctrl = controllerFor(ex4WithPath(testIn.toString()));
        ctrl.allStep();

        List<?> out = ctrl.getRepo().getCrtPrg().getOut().getList();
        assertEquals(2, out.size());
        assertEquals(new IntValue(15), out.get(0));
        assertEquals(new IntValue(50), out.get(1));
    }

    // -------------------------------------------------------------------------
    // Ex4 — empty file → 0, 0
    // -------------------------------------------------------------------------

    @Test
    void ex4PrintsZeroZeroForEmptyFile(@TempDir Path tmpDir) throws MyException, IOException {
        Path testIn = tmpDir.resolve("test.in");
        Files.writeString(testIn, "");

        Controller ctrl = controllerFor(ex4WithPath(testIn.toString()));
        ctrl.allStep();

        List<?> out = ctrl.getRepo().getCrtPrg().getOut().getList();
        assertEquals(2, out.size());
        assertEquals(new IntValue(0), out.get(0));
        assertEquals(new IntValue(0), out.get(1));
    }

    // -------------------------------------------------------------------------
    // Ex5 — prints 5
    // -------------------------------------------------------------------------

    @Test
    void ex5PrintsFive() throws MyException {
        Controller ctrl = controllerFor(EX5);
        ctrl.allStep();

        List<?> out = ctrl.getRepo().getCrtPrg().getOut().getList();
        assertEquals(1, out.size());
        assertEquals(new IntValue(5), out.get(0));
    }

    // -------------------------------------------------------------------------
    // Ex5 — else branch: value >= 8 prints 0
    // -------------------------------------------------------------------------

    @Test
    void ex5PrintsZeroWhenConditionFalse() throws MyException {
        // int v; v = 10; if (v < 8) then print(v) else print(0)  → prints 0
        IStmt prg = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(10))),
                        new IfStmt(
                                new RelationalExp(RelOp.LESS, new VarExp("v"), new ValueExp(new IntValue(8))),
                                new PrintStmt(new VarExp("v")),
                                new PrintStmt(new ValueExp(new IntValue(0)))
                        )
                )
        );

        Controller ctrl = controllerFor(prg);
        ctrl.allStep();

        List<?> out = ctrl.getRepo().getCrtPrg().getOut().getList();
        assertEquals(1, out.size());
        assertEquals(new IntValue(0), out.get(0));
    }
}


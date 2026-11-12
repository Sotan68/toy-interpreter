package interpreter.view;

import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CloseRFileStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IfStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.OpenRFileStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.ReadFileStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

public final class Examples {
    private Examples() {}

    // Ex1: int v; v = 2; print(v)
    public static final IStmt EX1 = new CompStmt(
            new VarDeclStmt("v", new IntType()),
            new CompStmt(
                    new AssignStmt("v", new ValueExp(new IntValue(2))),
                    new PrintStmt(new VarExp("v"))
            )
    );

    // Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)
    public static final IStmt EX2 = new CompStmt(
            new VarDeclStmt("a", new IntType()),
            new CompStmt(
                    new VarDeclStmt("b", new IntType()),
                    new CompStmt(
                            new AssignStmt("a",
                                    new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)),
                                            new ArithExp(ArithOp.MUL, new ValueExp(new IntValue(3)),
                                                    new ValueExp(new IntValue(5))))),
                            new CompStmt(
                                    new AssignStmt("b",
                                            new ArithExp(ArithOp.ADD, new VarExp("a"),
                                                    new ValueExp(new IntValue(1)))),
                                    new PrintStmt(new VarExp("b"))
                            )
                    )
            )
    );

    // Ex3: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
    public static final IStmt EX3 = new CompStmt(
            new VarDeclStmt("a", new BoolType()),
            new CompStmt(
                    new VarDeclStmt("v", new IntType()),
                    new CompStmt(
                            new AssignStmt("a", new ValueExp(new BoolValue(true))),
                            new CompStmt(
                                    new IfStmt(
                                            new VarExp("a"),
                                            new AssignStmt("v", new ValueExp(new IntValue(2))),
                                            new AssignStmt("v", new ValueExp(new IntValue(3)))
                                    ),
                                    new PrintStmt(new VarExp("v"))
                            )
                    )
            )
    );

    // Ex4 (A3): String fname; int r;
    //   fname = "test.in";
    //   openRFile(fname);
    //   readFile(fname, r); print(r);
    //   readFile(fname, r); print(r);
    //   closeRFile(fname)
    public static final IStmt EX4 = new CompStmt(
            new VarDeclStmt("fname", new StringType()),
            new CompStmt(
                    new VarDeclStmt("r", new IntType()),
                    new CompStmt(
                            new AssignStmt("fname", new ValueExp(new StringValue("test.in"))),
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

    // Ex5 (A3): int v; v = 4; (if (v < 8) then print(v) else print(0))
    public static final IStmt EX5 = new CompStmt(
            new VarDeclStmt("v", new IntType()),
            new CompStmt(
                    new AssignStmt("v", new ValueExp(new IntValue(4))),
                    new IfStmt(
                            new RelationalExp(RelOp.LESS, new VarExp("v"), new ValueExp(new IntValue(8))),
                            new PrintStmt(new VarExp("v")),
                            new PrintStmt(new ValueExp(new IntValue(0)))
                    )
            )
    );
}

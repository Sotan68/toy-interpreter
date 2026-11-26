package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void lessOperatorEvaluatesCorrectly() throws MyException {
        IExp trueExp = new RelationalExp(RelOp.LESS, num(1), num(2));
        assertEquals(new BoolValue(true), trueExp.eval(symTable, heap));

        IExp falseExpEqual = new RelationalExp(RelOp.LESS, num(2), num(2));
        assertEquals(new BoolValue(false), falseExpEqual.eval(symTable, heap));

        IExp falseExpGreater = new RelationalExp(RelOp.LESS, num(3), num(2));
        assertEquals(new BoolValue(false), falseExpGreater.eval(symTable, heap));
    }

    @Test
    void lessOrEqualOperatorEvaluatesCorrectly() throws MyException {
        IExp trueExpLess = new RelationalExp(RelOp.LESS_OR_EQUAL, num(1), num(2));
        assertEquals(new BoolValue(true), trueExpLess.eval(symTable, heap));

        IExp trueExpEqual = new RelationalExp(RelOp.LESS_OR_EQUAL, num(2), num(2));
        assertEquals(new BoolValue(true), trueExpEqual.eval(symTable, heap));

        IExp falseExp = new RelationalExp(RelOp.LESS_OR_EQUAL, num(3), num(2));
        assertEquals(new BoolValue(false), falseExp.eval(symTable, heap));
    }

    @Test
    void equalOperatorEvaluatesCorrectly() throws MyException {
        IExp trueExp = new RelationalExp(RelOp.EQUAL, num(5), num(5));
        assertEquals(new BoolValue(true), trueExp.eval(symTable, heap));

        IExp falseExp = new RelationalExp(RelOp.EQUAL, num(5), num(6));
        assertEquals(new BoolValue(false), falseExp.eval(symTable, heap));
    }

    @Test
    void notEqualOperatorEvaluatesCorrectly() throws MyException {
        IExp trueExp = new RelationalExp(RelOp.NOT_EQUAL, num(5), num(6));
        assertEquals(new BoolValue(true), trueExp.eval(symTable, heap));

        IExp falseExp = new RelationalExp(RelOp.NOT_EQUAL, num(5), num(5));
        assertEquals(new BoolValue(false), falseExp.eval(symTable, heap));
    }

    @Test
    void greaterOperatorEvaluatesCorrectly() throws MyException {
        IExp trueExp = new RelationalExp(RelOp.GREATER, num(7), num(3));
        assertEquals(new BoolValue(true), trueExp.eval(symTable, heap));

        IExp falseExpEqual = new RelationalExp(RelOp.GREATER, num(3), num(3));
        assertEquals(new BoolValue(false), falseExpEqual.eval(symTable, heap));

        IExp falseExpLess = new RelationalExp(RelOp.GREATER, num(2), num(3));
        assertEquals(new BoolValue(false), falseExpLess.eval(symTable, heap));
    }

    @Test
    void greaterOrEqualOperatorEvaluatesCorrectly() throws MyException {
        IExp trueExpGreater = new RelationalExp(RelOp.GREATER_OR_EQUAL, num(7), num(3));
        assertEquals(new BoolValue(true), trueExpGreater.eval(symTable, heap));

        IExp trueExpEqual = new RelationalExp(RelOp.GREATER_OR_EQUAL, num(3), num(3));
        assertEquals(new BoolValue(true), trueExpEqual.eval(symTable, heap));

        IExp falseExp = new RelationalExp(RelOp.GREATER_OR_EQUAL, num(2), num(3));
        assertEquals(new BoolValue(false), falseExp.eval(symTable, heap));
    }

    @Test
    void firstOperandNotIntegerThrows() {
        IExp e = new RelationalExp(RelOp.LESS, bool(true), num(2));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("first operand is not an integer", ex.getMessage());

        IExp eStr = new RelationalExp(RelOp.LESS, new ValueExp(new StringValue("a")), num(2));
        var exStr = assertThrows(ExpressionException.class, () -> eStr.eval(symTable, heap));
        assertEquals("first operand is not an integer", exStr.getMessage());
    }

    @Test
    void secondOperandNotIntegerThrows() {
        IExp e = new RelationalExp(RelOp.GREATER, num(2), bool(false));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("second operand is not an integer", ex.getMessage());
    }

    @Test
    void stringConstructorWorks() throws MyException {
        IExp e = new RelationalExp("<=", num(2), num(5));
        assertEquals(new BoolValue(true), e.eval(symTable, heap));
    }

    @Test
    void aliasesAndSymbolsWork() {
        assertSame(RelOp.LESS, RelOp.LT);
        assertSame(RelOp.LESS_OR_EQUAL, RelOp.LE);
        assertSame(RelOp.LESS_OR_EQUAL, RelOp.LESS_EQUAL);
        assertSame(RelOp.EQUAL, RelOp.EQ);
        assertSame(RelOp.NOT_EQUAL, RelOp.NE);
        assertSame(RelOp.GREATER, RelOp.GT);
        assertSame(RelOp.GREATER_OR_EQUAL, RelOp.GE);
        assertSame(RelOp.GREATER_OR_EQUAL, RelOp.GREATER_EQUAL);

        assertEquals("<", RelOp.LESS.toString());
        assertEquals("<=", RelOp.LESS_OR_EQUAL.toString());
        assertEquals("==", RelOp.EQUAL.toString());
        assertEquals("!=", RelOp.NOT_EQUAL.toString());
        assertEquals(">", RelOp.GREATER.toString());
        assertEquals(">=", RelOp.GREATER_OR_EQUAL.toString());
    }

    @Test
    void toStringFormatsCorrectly() {
        IExp e = new RelationalExp(RelOp.LESS, num(1), num(2));
        assertEquals("(1 < 2)", e.toString());
    }
}


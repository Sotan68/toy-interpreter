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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void andEvaluatesCorrectly() throws MyException {
        IExp trueAndFalse = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals(new BoolValue(false), trueAndFalse.eval(symTable, heap));

        IExp trueAndTrue = new LogicExp(LogicOp.AND, bool(true), bool(true));
        assertEquals(new BoolValue(true), trueAndTrue.eval(symTable, heap));
    }

    @Test
    void orEvaluatesCorrectly() throws MyException {
        IExp falseOrTrue = new LogicExp(LogicOp.OR, bool(false), bool(true));
        assertEquals(new BoolValue(true), falseOrTrue.eval(symTable, heap));

        IExp falseOrFalse = new LogicExp(LogicOp.OR, bool(false), bool(false));
        assertEquals(new BoolValue(false), falseOrFalse.eval(symTable, heap));
    }

    @Test
    void firstOperandNotBooleanThrows() {
        IExp e = new LogicExp(LogicOp.AND, num(1), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("first operand is not a boolean", ex.getMessage());
    }

    @Test
    void secondOperandNotBooleanThrows() {
        IExp e = new LogicExp(LogicOp.OR, bool(true), num(2));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        IExp e = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals("(true and false)", e.toString());
    }
}


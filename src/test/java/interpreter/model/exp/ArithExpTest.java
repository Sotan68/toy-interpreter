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

class ArithExpTest {
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
    void respectsOperatorPrecedenceEncodedInTheTree() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(2),
                new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), e.eval(symTable, heap));
    }

    @Test
    void additionEvaluatesCorrectly() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(5), num(3));
        assertEquals(new IntValue(8), e.eval(symTable, heap));
    }

    @Test
    void subtractionEvaluatesCorrectly() throws MyException {
        IExp e = new ArithExp(ArithOp.SUB, num(10), num(4));
        assertEquals(new IntValue(6), e.eval(symTable, heap));
    }

    @Test
    void multiplicationEvaluatesCorrectly() throws MyException {
        IExp e = new ArithExp(ArithOp.MUL, num(6), num(7));
        assertEquals(new IntValue(42), e.eval(symTable, heap));
    }

    @Test
    void divisionEvaluatesCorrectly() throws MyException {
        IExp e = new ArithExp(ArithOp.DIV, num(15), num(3));
        assertEquals(new IntValue(5), e.eval(symTable, heap));
    }

    @Test
    void divisionByZeroThrows() {
        IExp e = new ArithExp(ArithOp.DIV, num(1), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void firstOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, bool(true), num(2));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void secondOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, num(2), bool(false));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("second operand is not an integer", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        IExp e = new ArithExp(ArithOp.ADD, num(1), num(2));
        assertEquals("(1 + 2)", e.toString());
    }
}


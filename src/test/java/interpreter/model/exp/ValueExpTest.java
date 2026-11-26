package interpreter.model.exp;

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

class ValueExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void evalReturnsConstantValue() throws MyException {
        IExp intExp = new ValueExp(new IntValue(42));
        assertEquals(new IntValue(42), intExp.eval(symTable, heap));

        IExp boolExp = new ValueExp(new BoolValue(true));
        assertEquals(new BoolValue(true), boolExp.eval(symTable, heap));
    }

    @Test
    void toStringFormatsCorrectly() {
        IExp intExp = new ValueExp(new IntValue(42));
        assertEquals("42", intExp.toString());

        IExp boolExp = new ValueExp(new BoolValue(false));
        assertEquals("false", boolExp.toString());
    }
}


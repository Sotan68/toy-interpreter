package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadHeapExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
        // v -> (1, int), a -> (2, Ref(int)), heap: 1 -> 20, 2 -> (1, int)
        int first = heap.allocate(new IntValue(20));
        int second = heap.allocate(new RefValue(first, new IntType()));
        symTable.put("v", new RefValue(first, new IntType()));
        symTable.put("a", new RefValue(second, new RefType(new IntType())));
    }

    @Test
    void readsTheValueAtTheAddress() throws MyException {
        assertEquals(new IntValue(20), new ReadHeapExp(new VarExp("v")).eval(symTable, heap));
    }

    @Test
    void readsThroughNestedReferences() throws MyException {
        IExp exp = new ReadHeapExp(new ReadHeapExp(new VarExp("a")));
        assertEquals(new IntValue(20), exp.eval(symTable, heap));
    }

    @Test
    void canBeUsedInsideAnotherExpression() throws MyException {
        IExp exp = new ArithExp(ArithOp.ADD, new ReadHeapExp(new VarExp("v")), new ValueExp(new IntValue(5)));
        assertEquals(new IntValue(25), exp.eval(symTable, heap));
    }

    @Test
    void nonReferenceExpressionThrows() {
        IExp exp = new ReadHeapExp(new ValueExp(new IntValue(1)));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
        assertEquals("rH: 1 is not a reference", ex.getMessage());
    }

    @Test
    void addressNotInTheHeapThrows() {
        IExp exp = new ReadHeapExp(new ValueExp(new RefValue(7, new IntType())));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
        assertEquals("rH: address 7 is not in the heap", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        assertEquals("rH(v)", new ReadHeapExp(new VarExp("v")).toString());
    }
}

package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void evalReturnsVariableValue() throws MyException {
        symTable.put("x", new IntValue(10));
        IExp varExp = new VarExp("x");
        assertEquals(new IntValue(10), varExp.eval(symTable));

        symTable.put("flag", new BoolValue(true));
        IExp boolVar = new VarExp("flag");
        assertEquals(new BoolValue(true), boolVar.eval(symTable));
    }

    @Test
    void evalUndefinedVariableThrows() {
        IExp varExp = new VarExp("notDefined");
        var ex = assertThrows(ExpressionException.class, () -> varExp.eval(symTable));
        assertEquals("variable notDefined is not defined", ex.getMessage());
    }

    @Test
    void toStringFormatsCorrectly() {
        IExp varExp = new VarExp("count");
        assertEquals("count", varExp.toString());
    }
}


package interpreter.model.value;

import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {
    @Test
    void intValueBehavior() {
        IntValue val = new IntValue(42);
        assertEquals(42, val.val());
        assertEquals(new IntType(), val.getType());
        assertEquals("42", val.toString());
        assertEquals(new IntValue(42), val);
        assertNotEquals(new IntValue(43), val);
    }

    @Test
    void boolValueBehavior() {
        BoolValue trueVal = new BoolValue(true);
        BoolValue falseVal = new BoolValue(false);
        assertTrue(trueVal.val());
        assertFalse(falseVal.val());
        assertEquals(new BoolType(), trueVal.getType());
        assertEquals("true", trueVal.toString());
        assertEquals("false", falseVal.toString());
        assertEquals(new BoolValue(true), trueVal);
        assertNotEquals(trueVal, falseVal);
    }
}

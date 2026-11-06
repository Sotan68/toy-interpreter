package interpreter.model.value;

import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
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

    @Test
    void stringValueBehavior() {
        StringValue s1 = new StringValue("hello");
        StringValue s2 = new StringValue("hello");
        StringValue s3 = new StringValue("world");

        assertEquals("hello", s1.val());
        assertEquals(new StringType(), s1.getType());
        assertEquals("hello", s1.toString());

        // equals and hashCode tests
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
        assertNotEquals(s1.hashCode(), s3.hashCode());
        assertNotEquals(s1, new IntValue(42));
    }
}

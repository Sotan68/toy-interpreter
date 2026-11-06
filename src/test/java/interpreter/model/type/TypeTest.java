package interpreter.model.type;

import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeTest {
    @Test
    void intTypeDefaultValueAndString() {
        IType intType = new IntType();
        assertEquals(new IntValue(0), intType.defaultValue());
        assertEquals("int", intType.toString());
        assertEquals(new IntType(), intType);
    }

    @Test
    void boolTypeDefaultValueAndString() {
        IType boolType = new BoolType();
        assertEquals(new BoolValue(false), boolType.defaultValue());
        assertEquals("bool", boolType.toString());
        assertEquals(new BoolType(), boolType);
    }

    @Test
    void stringTypeDefaultValueAndString() {
        IType stringType = new StringType();
        assertEquals(new StringValue(""), stringType.defaultValue());
        assertEquals("string", stringType.toString());
        assertEquals(new StringType(), stringType);
    }

    @Test
    void typesAreNotEqual() {
        assertNotEquals(new IntType(), new BoolType());
        assertNotEquals(new IntType(), new StringType());
        assertNotEquals(new BoolType(), new StringType());
    }
}

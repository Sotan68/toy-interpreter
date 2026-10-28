package interpreter.model.type;

import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
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
    void typesAreNotEqual() {
        assertNotEquals(new IntType(), new BoolType());
    }
}

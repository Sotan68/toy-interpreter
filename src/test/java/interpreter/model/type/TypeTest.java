package interpreter.model.type;

import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
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

    @Test
    void refTypeDefaultValueAndString() {
        IType refType = new RefType(new IntType());
        assertEquals(new RefValue(0, new IntType()), refType.defaultValue());
        assertEquals("Ref(int)", refType.toString());
        assertEquals("Ref(Ref(int))", new RefType(refType).toString());
    }

    @Test
    void nestedRefTypesAreComparedRecursively() {
        IType refRefInt = new RefType(new RefType(new IntType()));
        IType sameType = new RefType(new RefType(new IntType()));

        assertEquals(refRefInt, sameType);
        assertEquals(refRefInt.hashCode(), sameType.hashCode());
        assertNotEquals(refRefInt, new RefType(new IntType()));
        assertNotEquals(refRefInt, new RefType(new RefType(new BoolType())));
        assertNotEquals(new RefType(new IntType()), new IntType());
    }
}

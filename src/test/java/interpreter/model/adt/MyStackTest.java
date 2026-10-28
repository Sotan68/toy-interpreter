package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest {
    private IStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new MyStack<>();
    }

    @Test
    void lifoOrder() throws ADTException {
        assertTrue(stack.isEmpty());
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertFalse(stack.isEmpty());

        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void popOnEmptyStackThrows() {
        assertTrue(stack.isEmpty());
        var ex = assertThrows(ADTException.class, () -> stack.pop());
        assertEquals("pop from an empty stack", ex.getMessage());
    }

    @Test
    void toStringRepresentation() {
        assertEquals("{}", stack.toString());
        stack.push(1);
        stack.push(2);
        assertEquals("{2 | 1}", stack.toString());
    }
}

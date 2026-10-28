package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    private IList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void addGetAndSize() throws ADTException {
        assertEquals(0, list.size());
        list.add("first");
        list.add("second");
        assertEquals(2, list.size());
        assertEquals("first", list.get(0));
        assertEquals("second", list.get(1));
    }

    @Test
    void getOutOfBoundsThrows() {
        assertThrows(ADTException.class, () -> list.get(0));
        list.add("item");
        assertThrows(ADTException.class, () -> list.get(-1));
        assertThrows(ADTException.class, () -> list.get(1));
    }

    @Test
    void toStringRepresentation() {
        assertEquals("{}", list.toString());
        list.add("a");
        list.add("b");
        assertEquals("{a, b}", list.toString());
    }
}

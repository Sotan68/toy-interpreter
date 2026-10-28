package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private IDictionary<String, Integer> dict;

    @BeforeEach
    void setUp() {
        dict = new MyDictionary<>();
    }

    @Test
    void putAndLookup() throws ADTException {
        assertFalse(dict.isDefined("x"));
        dict.put("x", 10);
        assertTrue(dict.isDefined("x"));
        assertEquals(10, dict.lookup("x"));
    }

    @Test
    void updateExistingKey() throws ADTException {
        dict.put("x", 10);
        dict.update("x", 20);
        assertEquals(20, dict.lookup("x"));
    }

    @Test
    void lookupMissingKeyThrows() {
        var ex = assertThrows(ADTException.class, () -> dict.lookup("missing"));
        assertTrue(ex.getMessage().contains("missing"));
    }

    @Test
    void updateMissingKeyThrows() {
        var ex = assertThrows(ADTException.class, () -> dict.update("missing", 99));
        assertTrue(ex.getMessage().contains("missing"));
    }
}

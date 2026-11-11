package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

    @Test
    void removeExistingKey() throws ADTException {
        dict.put("x", 10);
        assertTrue(dict.isDefined("x"));
        dict.remove("x");
        assertFalse(dict.isDefined("x"));
        assertThrows(ADTException.class, () -> dict.lookup("x"));
    }

    @Test
    void removeMissingKeyThrows() {
        var ex = assertThrows(ADTException.class, () -> dict.remove("missing"));
        assertTrue(ex.getMessage().contains("missing"));
    }

    @Test
    void toMapReturnsImmutableContent() {
        dict.put("a", 1);
        dict.put("b", 2);
        Map<String, Integer> map = dict.toMap();
        assertEquals(2, map.size());
        assertEquals(1, map.get("a"));
        assertEquals(2, map.get("b"));
        assertEquals(map, dict.getContent());
        assertThrows(UnsupportedOperationException.class, () -> map.put("c", 3));
    }
}

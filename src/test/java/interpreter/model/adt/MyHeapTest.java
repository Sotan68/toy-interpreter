package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateReturnsConsecutiveAddressesStartingAtOne() throws ADTException {
        assertEquals(1, heap.allocate(new IntValue(10)));
        assertEquals(2, heap.allocate(new IntValue(20)));
        assertEquals(3, heap.allocate(new IntValue(30)));
        assertEquals(new IntValue(20), heap.get(2));
    }

    @Test
    void getOfMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.get(0));
        assertThrows(ADTException.class, () -> heap.get(1));
    }

    @Test
    void updateChangesTheValue() throws ADTException {
        int address = heap.allocate(new IntValue(10));
        heap.update(address, new IntValue(99));
        assertEquals(new IntValue(99), heap.get(address));
    }

    @Test
    void updateOfMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.update(1, new IntValue(5)));
        assertFalse(heap.isDefined(1));
    }

    @Test
    void isDefinedOnlyForAllocatedAddresses() {
        int address = heap.allocate(new IntValue(10));
        assertTrue(heap.isDefined(address));
        assertFalse(heap.isDefined(0));
        assertFalse(heap.isDefined(address + 1));
    }

    @Test
    void getContentReturnsReadOnlyCopy() {
        heap.allocate(new IntValue(10));
        Map<Integer, IValue> content = heap.getContent();
        heap.allocate(new IntValue(20));

        assertEquals(1, content.size());
        assertThrows(UnsupportedOperationException.class, () -> content.put(5, new IntValue(5)));
    }

    @Test
    void setContentReplacesContentWithoutReusingAddresses() {
        heap.allocate(new IntValue(10));
        heap.allocate(new IntValue(20));
        heap.setContent(Map.of(2, new IntValue(20)));

        assertFalse(heap.isDefined(1));
        assertTrue(heap.isDefined(2));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }

    @Test
    void toStringListsOneLocationPerLineSortedByAddress() {
        assertEquals("", heap.toString());
        heap.setContent(Map.of(17, new IntValue(5), 2, new RefValue(17, new IntType())));
        assertEquals("2 --> (17, int)\n17 --> 5\n", heap.toString());
    }
}

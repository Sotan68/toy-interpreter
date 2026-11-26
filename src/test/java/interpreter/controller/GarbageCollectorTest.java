package interpreter.controller;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GarbageCollectorTest {
    // the state of Ex9 after new(v, 30): v -> (3, int), a -> (2, Ref(int))
    private final List<IValue> symTable = List.of(
            new RefValue(3, new IntType()), new RefValue(2, new RefType(new IntType())));
    private final Map<Integer, IValue> heap = Map.of(
            1, new IntValue(20), 2, new RefValue(1, new IntType()),
            3, new IntValue(30), 4, new IntValue(7));

    @Test
    void getAddrFromSymTableIgnoresValuesThatAreNotReferences() {
        List<IValue> values = List.of(new IntValue(1), new RefValue(5, new IntType()));
        assertEquals(Set.of(5), GarbageCollector.getAddrFromSymTable(values));
    }

    @Test
    void unsafeCollectorLosesTheLocationsReachableOnlyThroughTheHeap() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, heap);
        assertEquals(Set.of(2, 3), kept.keySet());
    }

    @Test
    void keepsTheLocationsReachableThroughTheHeap() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);
        assertEquals(Set.of(1, 2, 3), kept.keySet()); // 4 is garbage, 1 is not
        assertEquals(new IntValue(20), kept.get(1));
    }

    @Test
    void removesEverythingWhenThereAreNoRoots() {
        assertTrue(GarbageCollector.safeGarbageCollector(Set.of(), heap).isEmpty());
    }

    @Test
    void followsALongChainOfReferences() {
        IValue toOne = new RefValue(1, new IntType());
        IValue toTwo = new RefValue(2, new RefType(new IntType()));
        Map<Integer, IValue> chain = Map.of(1, new IntValue(5), 2, toOne, 3, toTwo, 4, new IntValue(0));

        assertEquals(Set.of(1, 2, 3), GarbageCollector.reachableAddresses(Set.of(3), chain));
    }

    @Test
    void stopsOnACycle() {
        Map<Integer, IValue> cycle = Map.of(
                1, new RefValue(2, new IntType()), 2, new RefValue(1, new IntType()));

        assertEquals(Set.of(1, 2), GarbageCollector.reachableAddresses(Set.of(1), cycle));
    }

    @Test
    void ignoresNullReferences() {
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(0), heap);
        assertTrue(kept.isEmpty());
    }
}

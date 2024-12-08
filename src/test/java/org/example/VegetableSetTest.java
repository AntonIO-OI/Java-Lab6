package org.example;

import org.example.exception.CalorieRangeException;
import org.example.vegetable.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for VegetableSet implementation.
 * Tests all major functionality including constructors, basic operations,
 * and special vegetable-specific features.
 */
class VegetableSetTest {
    private VegetableSet<Vegetable> emptySet;
    private VegetableSet<Vegetable> populatedSet;
    private Vegetable carrot;
    private Vegetable tomato;
    private Vegetable cucumber;

    @BeforeEach
    void setUp() {
        emptySet = new VegetableSet<>();
        populatedSet = new VegetableSet<>();
        carrot = new Carrot(150);
        tomato = new Tomato(200);
        cucumber = new Cucumber(300);

        populatedSet.add(carrot);
        populatedSet.add(tomato);
        populatedSet.add(cucumber);
    }

    @Test
    @DisplayName("Test empty constructor")
    void testEmptyConstructor() {
        assertEquals(0, emptySet.size());
        assertTrue(emptySet.isEmpty());
    }

    @Test
    @DisplayName("Test single element constructor")
    void testSingleElementConstructor() {
        VegetableSet<Vegetable> singleSet = new VegetableSet<>(carrot);
        assertEquals(1, singleSet.size());
        assertTrue(singleSet.contains(carrot));
    }

    @Test
    @DisplayName("Test collection constructor")
    void testCollectionConstructor() {
        VegetableSet<Vegetable> collectionSet = new VegetableSet<>(
                Arrays.asList(carrot, tomato, cucumber)
        );
        assertEquals(3, collectionSet.size());
        assertTrue(collectionSet.containsAll(Arrays.asList(carrot, tomato, cucumber)));
    }

    @Test
    @DisplayName("Test null handling in constructors")
    void testNullHandling() {
        assertThrows(NullPointerException.class, () -> new VegetableSet<>((Vegetable) null));
        assertThrows(NullPointerException.class, () -> new VegetableSet<>((Collection<Vegetable>) null));
    }

    @Test
    @DisplayName("Test add operation")
    void testAdd() {
        assertTrue(emptySet.add(carrot));
        assertEquals(1, emptySet.size());
        assertFalse(emptySet.add(carrot), "Should not add duplicate");
        assertEquals(1, emptySet.size());
    }

    @Test
    @DisplayName("Test remove operation")
    void testRemove() {
        assertTrue(populatedSet.remove(carrot));
        assertEquals(2, populatedSet.size());
        assertFalse(populatedSet.contains(carrot));

        assertFalse(populatedSet.remove(carrot), "Should return false when removing non-existent element");
    }

    @Test
    @DisplayName("Test contains operation")
    void testContains() {
        assertTrue(populatedSet.contains(carrot));
        assertTrue(populatedSet.contains(new Carrot(150)), "Should find equivalent vegetable");
        assertFalse(populatedSet.contains(new Carrot(151)), "Should not find vegetable with different weight");
    }

    @Test
    @DisplayName("Test iterator")
    void testIterator() {
        Iterator<Vegetable> iterator = populatedSet.iterator();
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());

        // Test remove through iterator
        iterator = populatedSet.iterator();
        iterator.next();
        iterator.remove();
        assertEquals(2, populatedSet.size());

        // Test exception when no more elements
        while(iterator.hasNext()) {
            iterator.next();
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @DisplayName("Test total calories calculation")
    void testGetTotalCalories() {
        double expectedCalories =
                carrot.getTotalCalories() +
                        tomato.getTotalCalories() +
                        cucumber.getTotalCalories();
        assertEquals(expectedCalories, populatedSet.getTotalCalories(), 0.001);
    }

    @Test
    @DisplayName("Test total cost calculation")
    void testGetTotalCost() {
        double expectedCost =
                (carrot.getPrice() * carrot.getWeight() / 1000.0) +
                        (tomato.getPrice() * tomato.getWeight() / 1000.0) +
                        (cucumber.getPrice() * cucumber.getWeight() / 1000.0);
        assertEquals(expectedCost, populatedSet.getTotalCost(), 0.001);
    }

    @Test
    @DisplayName("Test calorie range search")
    void testFindByCalorieRange() throws CalorieRangeException {
        VegetableSet<Vegetable> lowCalVegetables = populatedSet.findByCalorieRange(0, 20);
        assertTrue(lowCalVegetables.contains(tomato));
        assertTrue(lowCalVegetables.contains(cucumber));
        assertFalse(lowCalVegetables.contains(carrot));

        assertThrows(CalorieRangeException.class,
                () -> populatedSet.findByCalorieRange(20, 10),
                "Should throw exception when min > max");
    }

    @Test
    @DisplayName("Test collection operations")
    void testCollectionOperations() {
        // Test addAll
        VegetableSet<Vegetable> newSet = new VegetableSet<>();
        assertTrue(newSet.addAll(Arrays.asList(carrot, tomato)));
        assertEquals(2, newSet.size());

        // Test containsAll
        assertTrue(populatedSet.containsAll(Arrays.asList(carrot, tomato)));

        // Test retainAll
        assertTrue(populatedSet.retainAll(Arrays.asList(carrot, tomato)));
        assertEquals(2, populatedSet.size());
        assertFalse(populatedSet.contains(cucumber));

        // Test removeAll
        assertTrue(populatedSet.removeAll(Arrays.asList(carrot)));
        assertEquals(1, populatedSet.size());
        assertTrue(populatedSet.contains(tomato));
    }

    @Test
    @DisplayName("Test clear operation")
    void testClear() {
        populatedSet.clear();
        assertTrue(populatedSet.isEmpty());
        assertEquals(0, populatedSet.size());
    }

    @Test
    @DisplayName("Test toArray operations")
    void testToArray() {
        Object[] array = populatedSet.toArray();
        assertEquals(3, array.length);

        Vegetable[] typedArray = populatedSet.toArray(new Vegetable[0]);
        assertEquals(3, typedArray.length);

        Vegetable[] oversizedArray = populatedSet.toArray(new Vegetable[5]);
        assertEquals(5, oversizedArray.length);
        assertNull(oversizedArray[3]);
    }

    @Test
    @DisplayName("Test toString representation")
    void testToString() {
        String str = populatedSet.toString();
        assertTrue(str.contains("Carrot"));
        assertTrue(str.contains("Tomato"));
        assertTrue(str.contains("Cucumber"));
    }

    @Test
    @DisplayName("Test adding vegetables with nearly identical weights")
    void testNearlyIdenticalVegetables() {
        VegetableSet<Vegetable> set = new VegetableSet<>();
        Vegetable carrot1 = new Carrot(100.0001);
        Vegetable carrot2 = new Carrot(100.0002);
        
        assertTrue(set.add(carrot1));
        assertFalse(set.add(carrot2), "Should consider nearly identical weights as equal");
    }

    @Test
    @DisplayName("Test capacity growth")
    void testCapacityGrowth() {
        VegetableSet<Vegetable> set = new VegetableSet<>();
        // Add more than initial capacity (15) elements
        for (int i = 0; i < 20; i++) {
            set.add(new Carrot(100 + i));
        }
        assertEquals(20, set.size());
    }

    @Test
    @DisplayName("Test iterator remove without next")
    void testIteratorRemoveWithoutNext() {
        Iterator<Vegetable> iterator = populatedSet.iterator();
        assertThrows(IllegalStateException.class, iterator::remove,
            "Remove without next should throw IllegalStateException");
    }

    @Test
    @DisplayName("Test iterator concurrent modification")
    void testIteratorConcurrentModification() {
        Iterator<Vegetable> iterator = populatedSet.iterator();
        iterator.next();
        populatedSet.add(new Lettuce(100));
        
        assertThrows(ConcurrentModificationException.class, () -> {
            while (iterator.hasNext()) {
                iterator.next();
            }
        });
    }

    @Test
    @DisplayName("Test extreme calorie ranges")
    void testExtremeCalorieRanges() throws CalorieRangeException {
        VegetableSet<Vegetable> result = populatedSet.findByCalorieRange(0, Double.MAX_VALUE);
        assertEquals(populatedSet.size(), result.size(), "Should include all vegetables");
        
        result = populatedSet.findByCalorieRange(1000, Double.MAX_VALUE);
        assertEquals(0, result.size(), "Should be empty for too high calories");
    }

    @Test
    @DisplayName("Test empty collection operations")
    void testEmptyCollectionOperations() {
        List<Vegetable> emptyList = new ArrayList<>();
        
        // RemoveAll with empty collection should return false as no changes were made
        assertFalse(populatedSet.removeAll(emptyList),
            "RemoveAll with empty collection should return false");
        assertEquals(3, populatedSet.size(), 
            "Set size should not change after removing empty collection");
        
        // RetainAll with empty collection should clear the set
        assertTrue(populatedSet.retainAll(emptyList),
            "RetainAll with empty collection should return true");
        assertEquals(0, populatedSet.size(), 
            "Set should be empty after retainAll with empty collection");
    }

    @Test
    @DisplayName("Test adding mixed vegetable types")
    void testMixedVegetables() {
        VegetableSet<Vegetable> mixedSet = new VegetableSet<>();
        mixedSet.add(new Carrot(100));
        mixedSet.add(new Tomato(100));
        mixedSet.add(new Cucumber(100));
        mixedSet.add(new Lettuce(100));
        mixedSet.add(new BellPepper(100));
        mixedSet.add(new Onion(100));
        
        assertEquals(6, mixedSet.size());
        
        // Test calorie calculations for mixed set
        double expectedCalories =
            (41.0 * 100 / 100.0) + // Carrot
            (18.0 * 100 / 100.0) + // Tomato
            (15.0 * 100 / 100.0) + // Cucumber
            (15.0 * 100 / 100.0) + // Lettuce
            (31.0 * 100 / 100.0) + // Bell Pepper
            (40.0 * 100 / 100.0);  // Onion
        
        assertEquals(expectedCalories, mixedSet.getTotalCalories(), 0.001);
    }

    @Test
    @DisplayName("Test boundary cases for vegetable weights")
    void testBoundaryWeights() {
        VegetableSet<Vegetable> set = new VegetableSet<>();
        
        // Test very small weights
        set.add(new Carrot(0.001));
        assertEquals(1, set.size());
        
        // Test very large weights
        set.add(new Tomato(999999.999));
        assertEquals(2, set.size());
        
        // Verify calculations still work with extreme weights
        assertTrue(set.getTotalCalories() > 0);
        assertTrue(set.getTotalCost() > 0);
    }

    @Test
    @DisplayName("Test multiple identical vegetables")
    void testMultipleIdenticalVegetables() {
        VegetableSet<Vegetable> set = new VegetableSet<>();
        Vegetable tomato1 = new Tomato(100);
        Vegetable tomato2 = new Tomato(100);
        Vegetable tomato3 = new Tomato(100);
        
        assertTrue(set.add(tomato1));
        assertFalse(set.add(tomato2));
        assertFalse(set.add(tomato3));
        assertEquals(1, set.size());
    }

    @Test
    @DisplayName("Test array operations with null elements")
    void testArrayOperationsWithNull() {
        Vegetable[] arrayWithNull = new Vegetable[populatedSet.size() + 2];
        arrayWithNull[0] = null;
        
        Vegetable[] result = populatedSet.toArray(arrayWithNull);
        assertNull(result[populatedSet.size()], 
            "Element at size index should be null");
    }

    @Test
    @DisplayName("Test calorie range edge cases")
    void testCalorieRangeEdgeCases() throws CalorieRangeException {
        // Test range with exactly matching calories
        VegetableSet<Vegetable> result = populatedSet.findByCalorieRange(41.0, 41.0);
        assertTrue(result.contains(carrot));
        assertEquals(1, result.size());
        
        // Test range with no matching vegetables
        result = populatedSet.findByCalorieRange(1000.0, 2000.0);
        assertEquals(0, result.size());
        
        // Test minimum possible range
        result = populatedSet.findByCalorieRange(0.0, 0.0);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Test set operations with self")
    void testSetOperationsWithSelf() {
        assertTrue(populatedSet.containsAll(populatedSet));
        assertFalse(populatedSet.retainAll(populatedSet));
        assertEquals(3, populatedSet.size());
    }

    @Test
    @DisplayName("Test cost calculations with minimal weight")
    void testCostCalculationsMinimalWeight() {
        VegetableSet<Vegetable> set = new VegetableSet<>();
        double minimalWeight = 0.001; // 1 mg
        Vegetable carrot = new Carrot(minimalWeight);
        set.add(carrot);

        // Calculate expected values
        double expectedCalories = (41.0 * minimalWeight) / 100.0; // Carrot has 41 calories per 100g
        double expectedCost = (1.49 * minimalWeight) / 1000.0;    // Carrot costs $1.49 per kg

        assertEquals(expectedCalories, set.getTotalCalories(), 0.0001,
            "Calories calculation should work with minimal weight");
        assertEquals(expectedCost, set.getTotalCost(), 0.0001,
            "Cost calculation should work with minimal weight");
    }
}

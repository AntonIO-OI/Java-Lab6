package org.example;

import org.example.exception.CalorieRangeException;
import org.example.vegetable.Vegetable;
import java.util.*;

/**
 * A custom Set implementation specifically designed for Vegetable objects.
 * This implementation uses an array-based structure with an initial capacity
 * of 15 elements and grows by 30% when capacity is reached.
 * The set maintains uniqueness of vegetables based on their properties
 * (name, weight, calories, and price). Two vegetables are considered equal
 * if all these properties match within a small epsilon for floating-point values.
 *
 * @param <T> Type parameter that extends Vegetable
 */
public class VegetableSet<T extends Vegetable> implements Set<T> {
    /** Default initial capacity of the internal array */
    private static final int DEFAULT_CAPACITY = 15;
    
    /** Growth factor when resizing the internal array */
    private static final double GROWTH_FACTOR = 1.3;
    
    /** Epsilon value for floating-point comparisons */
    private static final double EPSILON = 0.001;
    
    /** Internal array to store vegetables */
    private T[] elements;
    
    /** Current number of elements in the set */
    private int size;
    
    /** Modification count to track concurrent modifications */
    private int modCount = 0;

    /**
     * Constructs an empty set with the default capacity of 15 elements.
     */
    @SuppressWarnings("unchecked")
    public VegetableSet() {
        this.elements = (T[]) new Vegetable[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Constructs a set containing a single element.
     *
     * @param element The element to be added to the set
     * @throws NullPointerException if the specified element is null
     */
    public VegetableSet(T element) {
        this();
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        add(element);
    }

    /**
     * Constructs a set containing elements from the specified collection.
     *
     * @param collection The collection whose elements are to be placed into this set
     * @throws NullPointerException if the collection or any of its elements is null
     */
    public VegetableSet(Collection<? extends T> collection) {
        this();
        if (collection == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        addAll(collection);
    }

    /**
     * Ensures that the internal array has enough capacity to hold the specified
     * number of elements. If the current capacity is insufficient, grows the array
     * by the growth factor (30%).
     *
     * @param minCapacity The minimum capacity needed
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = (int) (elements.length * GROWTH_FACTOR);
            newCapacity = Math.max(newCapacity, minCapacity);
            T[] newElements = (T[]) new Vegetable[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    /**
     * Checks if two vegetables are equal within the epsilon tolerance.
     * Compares all properties: name, weight, calories, and price.
     * Floating-point comparisons use an epsilon value to handle precision issues.
     *
     * @param v1 First vegetable to compare
     * @param v2 Second vegetable to compare
     * @return true if the vegetables are considered equal, false otherwise
     */
    private boolean areVegetablesEqual(Vegetable v1, Vegetable v2) {
        return v1.getName().equals(v2.getName()) &&
               Math.abs(v1.getWeight() - v2.getWeight()) < EPSILON &&
               Math.abs(v1.getCalories() - v2.getCalories()) < EPSILON &&
               Math.abs(v1.getPrice() - v2.getPrice()) < EPSILON;
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return The number of elements in this set
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if this set contains no elements.
     *
     * @return true if this set contains no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if this set contains the specified element.
     * The comparison is based on vegetable properties (name, weight, calories, price)
     * with floating-point values compared within an epsilon tolerance.
     *
     * @param o Object to be checked for containment in this set
     * @return true if this set contains the specified element, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Vegetable)) {
            return false;
        }
        Vegetable v = (Vegetable) o;
        for (int i = 0; i < size; i++) {
            if (areVegetablesEqual(elements[i], v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an iterator over the elements in this set.
     * The elements are returned in no particular order.
     * The iterator supports element removal.
     *
     * @return An iterator over the elements in this set
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            private int lastRet = -1;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                checkForComodification();
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor;
                return elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                checkForComodification();
                try {
                    VegetableSet.this.fastRemove(lastRet);
                    cursor = lastRet;
                    lastRet = -1;
                    expectedModCount = modCount;
                } catch (IndexOutOfBoundsException e) {
                    throw new ConcurrentModificationException();
                }
            }

            private void checkForComodification() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    /**
     * Returns an array containing all elements in this set.
     * The returned array will be "safe" in that no references to it are maintained by this set.
     *
     * @return An array containing all elements in this set
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Returns an array containing all elements in this set.
     * If the specified array is large enough, it is used; otherwise, 
     * a new array of the same runtime type is allocated.
     *
     * @param <T1> The runtime type of the array to contain the collection
     * @param a The array into which the elements of this set are to be stored
     * @return An array containing all elements in this set
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size) {
            return (T1[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * Uniqueness is determined by comparing all vegetable properties.
     *
     * @param t Element to be added to this set
     * @return true if the set did not already contain the specified element
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(T t) {
        if (t == null) {
            throw new NullPointerException("Cannot add null element");
        }
        if (!contains(t)) {
            ensureCapacity(size + 1);
            elements[size++] = t;
            modCount++;
            return true;
        }
        return false;
    }

    /**
     * Removes the specified element from this set if it is present.
     * Equality is determined by comparing all vegetable properties.
     *
     * @param o Object to be removed from this set
     * @return true if the set contained the specified element
     */
    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Vegetable)) {
            return false;
        }
        Vegetable v = (Vegetable) o;
        for (int i = 0; i < size; i++) {
            if (areVegetablesEqual(elements[i], v)) {
                fastRemove(i);
                modCount++;
                return true;
            }
        }
        return false;
    }

    /**
     * Quickly removes an element at the specified index.
     * Shifts any subsequent elements to the left and sets the last element to null.
     * This method does not check bounds; it is the responsibility of the caller
     * to ensure that index is valid.
     *
     * @param index The index of the element to remove
     */
    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
    }

    /**
     * Returns true if this set contains all elements in the specified collection.
     *
     * @param c Collection to be checked for containment in this set
     * @return true if this set contains all elements in the specified collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all elements in the specified collection to this set if they're not already present.
     *
     * @param c Collection containing elements to be added to this set
     * @return true if this set changed as a result of the call
     * @throws NullPointerException if the specified collection or any of its elements is null
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Retains only the elements in this set that are contained in the specified collection.
     *
     * @param c Collection containing elements to be retained in this set
     * @return true if this set changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                fastRemove(i);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes from this set all of its elements that are contained in the specified collection.
     *
     * @param c Collection containing elements to be removed from this set
     * @return true if this set changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                fastRemove(i);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes all elements from this set.
     * The set will be empty after this call returns.
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Calculates the total calories of all vegetables in the set.
     * The calculation takes into account the weight of each vegetable
     * and its calories per 100g.
     *
     * @return The total calories of all vegetables in the set
     */
    public double getTotalCalories() {
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += elements[i].getTotalCalories();
        }
        return total;
    }

    /**
     * Calculates the total cost of all vegetables in the set.
     * The calculation is based on each vegetable's weight and price per kg,
     * with weight converted from grams to kilograms.
     *
     * @return The total cost in the set's currency
     */
    public double getTotalCost() {
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += (elements[i].getPrice() * elements[i].getWeight()) / 1000.0;
        }
        return total;
    }

    /**
     * Finds all vegetables within a specific calorie range.
     * Creates a new set containing only the vegetables whose calories
     * per 100g fall within the specified range (inclusive).
     *
     * @param minCalories Minimum calories per 100g (inclusive)
     * @param maxCalories Maximum calories per 100g (inclusive)
     * @return A new VegetableSet containing only vegetables within the calorie range
     * @throws CalorieRangeException if minCalories is greater than maxCalories
     */
    public VegetableSet<T> findByCalorieRange(double minCalories, double maxCalories) 
            throws CalorieRangeException {
        if (minCalories > maxCalories) {
            throw new CalorieRangeException("Minimum calories cannot be greater than maximum calories");
        }
        
        VegetableSet<T> result = new VegetableSet<>();
        for (int i = 0; i < size; i++) {
            double calories = elements[i].getCalories();
            if (calories >= minCalories && calories <= maxCalories) {
                result.add(elements[i]);
            }
        }
        return result;
    }

    /**
     * Returns a string representation of this set.
     * The string representation consists of a list of the set's elements
     * enclosed in square brackets ("[]"). Elements are converted to strings
     * using their toString() methods and separated by commas.
     *
     * @return A string representation of this set
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}

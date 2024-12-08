package org.example.vegetable;

/**
 * Represents a Carrot vegetable.
 * Contains 41 calories per 100g.
 */
public class Carrot extends Vegetable {
    /**
     * Creates a new Carrot instance.
     *
     * @param weight   Weight in grams
     */
    public Carrot(double weight) {
        super("Carrot", 41.0, weight, 1.49);
    }
}
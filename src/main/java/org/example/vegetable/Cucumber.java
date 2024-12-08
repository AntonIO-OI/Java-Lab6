package org.example.vegetable;

/**
 * Represents a Cucumber vegetable.
 * Contains 15 calories per 100g.
 */
public class Cucumber extends Vegetable {
    /**
     * Creates a new Cucumber instance.
     *
     * @param weight   Weight in grams
     */
    public Cucumber(double weight) {
        super("Cucumber", 15.0, weight, 1.99);
    }
}
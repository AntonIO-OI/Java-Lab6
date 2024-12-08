package org.example.vegetable;

/**
 * Represents a Lettuce vegetable.
 * Contains 15 calories per 100g.
 */
public class Lettuce extends Vegetable {
    /**
     * Creates a new Lettuce instance.
     *
     * @param weight  Weight in grams
     */
    public Lettuce(double weight) {
        super("Lettuce", 15.0, weight, 2.49);
    }
}
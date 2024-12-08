package org.example.vegetable;

/**
 * Represents a Bell Pepper vegetable.
 * Contains 31 calories per 100g.
 */
public class BellPepper extends Vegetable {
    /**
     * Creates a new Bell Pepper instance.
     *
     * @param weight Weight in grams
     */
    public BellPepper(double weight) {
        super("Bell Pepper", 31.0, weight, 3.99);
    }
}
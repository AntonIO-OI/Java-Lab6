package org.example.vegetable;

/**
 * Represents an Onion vegetable.
 * Contains 40 calories per 100g.
 */
public class Onion extends Vegetable {

    /**
     * Creates a new Onion instance.
     *
     * @param weight Weight in grams
     */
    public Onion(double weight) {
        super("Onion", 40.0, weight, 1.29);
    }
}
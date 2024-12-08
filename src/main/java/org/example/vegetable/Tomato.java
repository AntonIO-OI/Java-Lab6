package org.example.vegetable;

/**
 * Represents a Tomato vegetable.
 * Contains 18 calories per 100g.
 */
public class Tomato extends Vegetable {
    /**
     * Creates a new Tomato instance.
     *
     * @param weight   Weight in grams
     */
    public Tomato(double weight) {
        super("Tomato", 18.0, weight, 2.99);
    }
}
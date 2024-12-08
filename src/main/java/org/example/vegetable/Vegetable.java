package org.example.vegetable;

/**
 * Abstract base class representing a vegetable.
 */
public abstract class Vegetable {
    private final String name;
    private final double calories;
    private final double weight;
    private final double price;

    /**
     * Constructor for Vegetable.
     *
     * @param name     The name of the vegetable
     * @param calories Calories per 100g
     * @param weight   Weight in grams
     * @param price    Price per kg
     */
    public Vegetable(String name, double calories, double weight, double price) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        if (calories < 0) {
            throw new IllegalArgumentException("Calories cannot be negative");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        this.name = name;
        this.calories = calories;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getCalories() {
        return calories;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Calculate total calories for this vegetable based on its weight.
     *
     * @return Total calories
     */
    public double getTotalCalories() {
        return (calories * weight) / 100.0;
    }

    @Override
    public String toString() {
        return String.format("%s (%.1fg, %.1f cal/100g, $%.2f/kg)", 
            name, weight, calories, price);
    }
} 
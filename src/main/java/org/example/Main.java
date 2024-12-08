package org.example;

import org.example.exception.CalorieRangeException;
import org.example.vegetable.*;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        demonstrateConstructors();
        demonstrateSetOperations();
    }

    private static void demonstrateConstructors() {
        System.out.println("\n=== Testing Empty Constructor ===");
        VegetableSet<Vegetable> emptySet = new VegetableSet<>();
        System.out.println("Empty set: " + emptySet);
        System.out.println("Size: " + emptySet.size());

        System.out.println("\n=== Testing Single Element Constructor ===");
        VegetableSet<Vegetable> singleSet = new VegetableSet<>(new Carrot(150));
        System.out.println("Single set: " + singleSet);
        System.out.println("Size: " + singleSet.size());

        System.out.println("\n=== Testing Collection Constructor ===");
        VegetableSet<Vegetable> collectionSet = new VegetableSet<>(Arrays.asList(
            new Carrot(150),
            new Tomato(200),
            new Cucumber(300)
        ));
        System.out.println("Collection set: " + collectionSet);
        System.out.println("Size: " + collectionSet.size());
    }

    private static void demonstrateSetOperations() {
        VegetableSet<Vegetable> salad = createSampleSalad();

        demonstrateDuplicateHandling(salad);
        demonstrateRemoval(salad);
        demonstrateUtilityMethods(salad);
        demonstrateCalorieRangeSearch(salad);
        demonstrateIteration(salad);
    }

    private static VegetableSet<Vegetable> createSampleSalad() {
        System.out.println("\n=== Testing Add Operations ===");
        VegetableSet<Vegetable> salad = new VegetableSet<>();
        salad.add(new Lettuce(100));
        salad.add(new Tomato(150));
        salad.add(new Cucumber(200));
        salad.add(new BellPepper(100));
        salad.add(new Onion(50));
        System.out.println("Salad ingredients: " + salad);
        return salad;
    }

    private static void demonstrateDuplicateHandling(VegetableSet<Vegetable> salad) {
        System.out.println("\n=== Testing Duplicate Handling ===");
        boolean added = salad.add(new Tomato(150));
        System.out.println("Added duplicate tomato? " + added);
        System.out.println("Salad ingredients: " + salad);
    }

    private static void demonstrateRemoval(VegetableSet<Vegetable> salad) {
        System.out.println("\n=== Testing Remove Operations ===");
        boolean removed = salad.remove(new Onion(50));
        System.out.println("Removed onion? " + removed);
        System.out.println("Updated salad: " + salad);
    }

    private static void demonstrateUtilityMethods(VegetableSet<Vegetable> salad) {
        System.out.println("\n=== Testing Utility Methods ===");
        System.out.printf("Total calories: %.2f cal%n", salad.getTotalCalories());
        System.out.printf("Total cost: $%.2f%n", salad.getTotalCost());
    }

    private static void demonstrateCalorieRangeSearch(VegetableSet<Vegetable> salad) {
        System.out.println("\n=== Testing Calorie Range Search ===");
        try {
            VegetableSet<Vegetable> lowCalVegetables = salad.findByCalorieRange(0, 20);
            System.out.println("Low calorie vegetables (0-20 cal/100g): " + lowCalVegetables);
            
            VegetableSet<Vegetable> mediumCalVegetables = salad.findByCalorieRange(20, 35);
            System.out.println("Medium calorie vegetables (20-35 cal/100g): " + mediumCalVegetables);
        } catch (CalorieRangeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateIteration(VegetableSet<Vegetable> salad) {
        System.out.println("\n=== Testing Iterator ===");
        System.out.println("Iterating through salad ingredients:");
        for (Vegetable vegetable : salad) {
            System.out.printf("- %s: %.1f calories per 100g%n", 
                vegetable.getName(), 
                vegetable.getCalories());
        }
    }
}

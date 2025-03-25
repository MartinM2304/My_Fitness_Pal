package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class LogFoodCommand implements Command {
    private final Controller controller;
    private final Scanner scanner;

    public LogFoodCommand(Controller controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public Command execute() {
        Map<Integer, String> foodIds = controller.getFoodIds();
        System.out.println("When (date):");
        String dateStr = scanner.nextLine().trim();
        LocalDate date;
        try {
            date = DateParser.parse(dateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format.");
            return null;
        }

        System.out.println("When (meal):");
        String mealStr = scanner.nextLine().trim();
        if (!mealStr.equalsIgnoreCase("Breakfast") && !mealStr.equalsIgnoreCase("Lunch") &&
                !mealStr.equalsIgnoreCase("Snacks") && !mealStr.equalsIgnoreCase("Dinner")) {
            System.out.println("Invalid meal time. Use: Breakfast, Lunch, Snacks, Dinner");
            return null;
        }

        System.out.println("Which food (food id):");
        String foodIdStr = scanner.nextLine().trim();
        int foodId;
        try {
            foodId = Integer.parseInt(foodIdStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid food ID. Use a number.");
            return null;
        }

        String foodName = foodIds.get(foodId);
        if (foodName == null) {
            System.out.println("Food ID not found.");
            return null;
        }
        Consumable consumable = controller.getItems().get(foodName);
        if (!(consumable instanceof Food food)) {
            System.out.println("Invalid food item.");
            return null;
        }
        System.out.println(foodId + ". " + food);

        System.out.println("(Either)\nNumber of serving(s):");
        String servingsInput = scanner.nextLine().trim();
        int quantity;
        if (!servingsInput.isEmpty()) {
            try {
                int servings = Integer.parseInt(servingsInput);
                quantity = servings * food.getServingSize();
                System.out.printf("%d X %s (%s)\n", servings, food, calculateNutrition(food, quantity));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of servings.");
                return null;
            }
        } else {
            System.out.println("(Or)\nServing size:");
            String sizeInput = scanner.nextLine().trim();
            try {
                quantity = Integer.parseInt(sizeInput);
                System.out.printf("%dg X %s (%s)\n", quantity, food, calculateNutrition(food, quantity));
            } catch (NumberFormatException e) {
                System.out.println("Invalid serving size.");
                return null;
            }
        }
        return this;
    }

    private String calculateNutrition(Food food, int quantity) {
        double factor = (double) quantity / food.getServingSize();
        return String.format("Total: %dg; %d kcal; %.2fg, %.2fg, %.2fg",
                quantity, (int) (food.getCalories() * factor), food.getCarbs() * factor,
                food.getFat() * factor, food.getProtein() * factor);
    }

    @Override
    public String toString() {
        return "log food";
    }
}

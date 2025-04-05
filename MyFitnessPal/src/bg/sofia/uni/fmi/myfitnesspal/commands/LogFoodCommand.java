package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.MealTime;

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
            System.out.println("Invalid date format. Use: YYYY-MM-DD");
            return null;
        }

        System.out.println("When (meal):");
        String mealStr = scanner.nextLine().trim();
        MealTime mealTime;
        try {
            mealTime = MealTime.fromString(mealStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid meal time. Use: Breakfast, Lunch, Snacks, Dinner");
            return null;
        }

        System.out.println("Available foods:");
        if (foodIds.isEmpty()) {
            System.out.println("No foods available.");
            return null;
        }
        for (Map.Entry<Integer, String> entry : foodIds.entrySet()) {
            Consumable food = controller.getItems().get(entry.getValue());
            System.out.println(entry.getKey() + ". " + food);
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

        System.out.println("(Either)\nNumber of serving(s):");
        String servingsInput = scanner.nextLine().trim();
        int quantity;
        int servings;
        if (!servingsInput.isEmpty()) {
            try {
                servings = Integer.parseInt(servingsInput);
                if (servings <= 0) {
                    System.out.println("Number of servings must be positive.");
                    return null;
                }
                quantity = servings * food.getServingSize();
                System.out.printf("%d X %s (%s)\n", servings, food, calculateNutrition(food, quantity));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of servings. Use a number.");
                return null;
            }
        } else {
            System.out.println("(Or)\nServing size:");
            String sizeInput = scanner.nextLine().trim();
            try {
                quantity = Integer.parseInt(sizeInput);
                if (quantity <= 0) {
                    System.out.println("Serving size must be positive.");
                    return null;
                }
                servings = quantity / food.getServingSize(); // Приблизително
                System.out.printf("%dg X %s (%s)\n", quantity, food, calculateNutrition(food, quantity));
            } catch (NumberFormatException e) {
                System.out.println("Invalid serving size. Use a number.");
                return null;
            }
        }

        food.consumpt(date, servings, mealTime);
        System.out.println("Food consumption logged successfully.");
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

    @Override
    public boolean isExitCommand() {
        return false;
    }
}
package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;

import java.util.Map;
import java.util.Scanner;

public class CreateMealCommand implements Command {
    private final Controller controller;
    private final Scanner scanner;

    public CreateMealCommand(Controller controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public Command execute() {
        System.out.println("Meal name:");
        String MealName = scanner.nextLine().trim();
        if (MealName.isEmpty()) {
            System.out.println("Meal name cannot be empty.");
            return null;
        }

        Meal meal = new Meal(MealName);
        Map<Integer, String> foodIds = controller.getFoodIds();
        if (foodIds.isEmpty()) {
            System.out.println("No foods available to add to Meal.");
            return null;
        }

        System.out.println("Available foods:");
        for (Map.Entry<Integer, String> entry : foodIds.entrySet()) {
            Consumable food = controller.getItems().get(entry.getValue());
            if (food != null) {
                System.out.println(entry.getKey() + ". " + food);
            }
        }

        while (true) {
            System.out.println("Which food (food id, or 'done' to finish):");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            int foodId;
            try {
                foodId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid food ID. Use a number or 'done'.");
                continue;
            }

            String foodName = foodIds.get(foodId);
            if (foodName == null) {
                System.out.println("Food ID not found.");
                continue;
            }

            Consumable consumable = controller.getItems().get(foodName);
            if (!(consumable instanceof Food food)) {
                System.out.println("Invalid food item.");
                continue;
            }

            meal.addFood(food);
            System.out.println(food + " added to " + MealName);
        }

        controller.addMeal(meal);
        System.out.println("Meal '" + MealName + "' (ID: " + controller.getCurrentMealId() + ") created successfully!");
        return this;
    }

    @Override
    public String toString() {
        return "create Meal";
    }

    @Override
    public boolean isExitCommand() {
        return false;
    }
}
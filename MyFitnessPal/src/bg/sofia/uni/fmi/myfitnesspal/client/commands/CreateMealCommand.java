package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.util.Map;

public class CreateMealCommand implements Command {
    private final Controller controller;
    private IOStrategy ioStrategy;

    public CreateMealCommand(Controller controller, IOStrategy ioStrategy) {
        this.controller = controller;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {
        ioStrategy.display("Meal name:");
        String mealName = ioStrategy.getUserInput().trim();
        if (mealName.isEmpty()) {
            ioStrategy.display("Meal name cannot be empty.");
            return null;
        }

        Meal meal = new Meal(mealName);
        Map<Integer, String> foodIds = controller.getFoodIds();
        if (foodIds.isEmpty()) {
            ioStrategy.display("No foods available to add to Meal.");
            return null;
        }

        ioStrategy.display("Available foods:");
        controller.displayFoods();

        while (true) {
            ioStrategy.display("Which food (food id, or 'done' to finish):");
            String input = ioStrategy.getUserInput().trim();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            int foodId;
            try {
                foodId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ioStrategy.display("Invalid food ID. Use a number or 'done'.");
                continue;
            }

            String foodName = foodIds.get(foodId);
            if (foodName == null) {
                ioStrategy.display("Food ID not found.");
                continue;
            }

            Consumable consumable = controller.getItems().get(foodName);
            if (!(consumable instanceof Food food)) {
                ioStrategy.display("Invalid food item.");
                continue;
            }

            meal.addFood(food);
            ioStrategy.display(food + " added to " + mealName);
        }

        controller.addMeal(meal);
        ioStrategy.display("Meal '" + mealName
                + "' (ID: " + controller.getCurrentMealId()
                + ") created successfully!");
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

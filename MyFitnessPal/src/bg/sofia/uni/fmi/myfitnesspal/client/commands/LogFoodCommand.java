package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.time.LocalDate;
import java.util.Map;

public class LogFoodCommand implements Command {
    private final Controller controller;
    private final IOStrategy ioStrategy;

    public LogFoodCommand(Controller controller, IOStrategy ioStrategy) {
        this.controller = controller;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {
        Map<Integer, String> foodIds = controller.getFoodIds();
        ioStrategy.display("When (date):");
        String dateStr = ioStrategy.getUserInput().trim();
        LocalDate date;
        try {
            date = DateParser.parse(dateStr);
        } catch (IllegalArgumentException e) {
            ioStrategy.display("Invalid date format. Use: YYYY-MM-DD");
            return null;
        }

        ioStrategy.display("When (meal):");
        String mealStr = ioStrategy.getUserInput().trim();
        MealTime mealTime;
        try {
            mealTime = MealTime.fromString(mealStr);
        } catch (IllegalArgumentException e) {
            ioStrategy.display("Invalid meal time. Use: Breakfast,"
                    + " Lunch, Snacks, Dinner");
            return null;
        }

        ioStrategy.display("Available foods:");
        if (foodIds.isEmpty()) {
            ioStrategy.display("No foods available.");
            return null;
        }
        for (Map.Entry<Integer, String> entry : foodIds.entrySet()) {
            Consumable food = controller.getItems().get(entry.getValue());
            ioStrategy.display(entry.getKey() + ". " + food);
        }

        ioStrategy.display("Which food (food id):");
        String foodIdStr = ioStrategy.getUserInput().trim();
        int foodId;
        try {
            foodId = Integer.parseInt(foodIdStr);
        } catch (NumberFormatException e) {
            ioStrategy.display("Invalid food ID. Use a number.");
            return null;
        }

        String foodName = foodIds.get(foodId);
        if (foodName == null) {
            ioStrategy.display("Food ID not found.");
            return null;
        }
        Consumable consumable = controller.getItems().get(foodName);
        if (!(consumable instanceof Food food)) {
            ioStrategy.display("Invalid food item.");
            return null;
        }

        ioStrategy.display("(Either)\nNumber of serving(s):");
        String servingsInput = ioStrategy.getUserInput().trim();
        int quantity;
        int servings;
        if (!servingsInput.isEmpty()) {
            try {
                servings = Integer.parseInt(servingsInput);
                if (servings <= 0) {
                    ioStrategy.display("Number of servings must be positive.");
                    return null;
                }
                quantity = servings * food.getServingSize();
                ioStrategy.displayFormatted("%d X %s (%s)\n", servings, food,
                        calculateNutrition(food, quantity));
            } catch (NumberFormatException e) {
                ioStrategy.display("Invalid number of servings. Use a number.");
                return null;
            }
        } else {
            ioStrategy.display("(Or)\nServing size:");
            String sizeInput = ioStrategy.getUserInput().trim();
            try {
                quantity = Integer.parseInt(sizeInput);
                if (quantity <= 0) {
                    ioStrategy.display("Serving size must be positive.");
                    return null;
                }
                servings = quantity / food.getServingSize();
                ioStrategy.displayFormatted("%dg X %s (%s)\n", quantity, food,
                        calculateNutrition(food, quantity));
            } catch (NumberFormatException e) {
                ioStrategy.display("Invalid serving size. Use a number.");
                return null;
            }
        }

        food.consumpt(date, servings, mealTime);
        ioStrategy.display("Food consumption logged successfully.");
        return this;
    }

    private String calculateNutrition(Food food, int quantity) {
        double factor = (double) quantity / food.getServingSize();
        return String.format("Total: %dg; %d kcal; %.2fg, %.2fg, %.2fg",
                quantity, (int) (food.getCalories() * factor),
                food.getCarbs() * factor,
                food.getFat() * factor,
                food.getProtein() * factor);
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

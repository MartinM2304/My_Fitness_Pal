package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogRecipeCommand implements Command {
    private final Controller controller;
    private final IOStrategy ioStrategy;

    public LogRecipeCommand(Controller controller, IOStrategy ioStrategy) {
        this.controller = controller;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {
        ioStrategy.display("When (date):");
        String dateStr = ioStrategy.getUserInput();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr.replace("/", "-"));
        } catch (Exception e) {
            throw new IllegalArgumentException("date format is wrong");
        }

        ioStrategy.display("When (meal):");
        String mealTimeStr = ioStrategy.getUserInput();
        MealTime mealTime;
        try {
            mealTime = MealTime.valueOf(mealTimeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("mealtime is wrong");
        }

        ioStrategy.display("Which recipe (recipe id):");
        List<Food> recipes = new ArrayList<>();
        int index = 1;
        for (Consumable consumable : controller.getItems().values()) {
            if (consumable instanceof Food food && food.isRecipe()) {
                ioStrategy.display(
                        String.format("%d. %s", index++, food.toString()));
                recipes.add(food);
            }
        }

        if (recipes.isEmpty()) {
            throw new IllegalArgumentException("recipies is empty wrong");
        }

        String recipeIdStr = ioStrategy.getUserInput();
        int recipeId;
        try {
            recipeId = Integer.parseInt(recipeIdStr) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid recipe id");
        }

        if (recipeId < 0 || recipeId >= recipes.size()) {
            throw new IllegalArgumentException("invalid recipe id");
        }

        Food recipe = recipes.get(recipeId);
        ioStrategy.display("Number of serving(s):");
        String servingsStr = ioStrategy.getUserInput();
        int servings;
        try {
            servings = Integer.parseInt(servingsStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid number of servings");
        }

        recipe.consumpt(date, servings, mealTime);
        ioStrategy.display("Logged: " + recipe.toString());

        return this;
    }

    @Override
    public boolean isExitCommand() {
        return false;
    }
}

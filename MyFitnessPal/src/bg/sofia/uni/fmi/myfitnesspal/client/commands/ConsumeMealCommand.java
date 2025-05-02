package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.time.LocalDate;
import java.util.Map;

public class ConsumeMealCommand implements Command {
    private final Controller controller;
    private IOStrategy ioStrategy;

    public ConsumeMealCommand(Controller controller, IOStrategy strategy) {
        this.controller = controller;
        this.ioStrategy = strategy;
    }

    @Override
    public Command execute() {
        Map<Integer, String> menuIds = controller.getMealIds();
        if (menuIds.isEmpty()) {
            ioStrategy.display("No menus available to consume.");
            return null;
        }

        ioStrategy.display("Available menus:");
        for (Map.Entry<Integer, String> entry : menuIds.entrySet()) {
            ioStrategy.display(entry.getKey() + ". " + entry.getValue());
        }

        ioStrategy.display("Which menu (ID):");
        int menuId;
        try {
            menuId = Integer.parseInt(ioStrategy.getUserInput().trim());
        } catch (NumberFormatException e) {
            ioStrategy.display("Invalid menu ID. Use a number.");
            return null;
        }

        String menuName = menuIds.get(menuId);
        if (menuName == null) {
            ioStrategy.display("Menu ID not found.");
            return null;
        }

        Meal menu = (Meal) controller.getItems().get(menuName);
        if (menu == null) {
            ioStrategy.display("Menu not found.");
            return null;
        }

        ioStrategy.display("When (date, format YYYY-MM-DD or similar):");
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
            ioStrategy.display("Invalid meal time. Use: "
                    + "Breakfast, Lunch, Snacks, Dinner");
            return null;
        }

        for (Food food : menu.getFoods()) {
            ioStrategy.display("Number of servings for " + food + ":");
            int servings;
            try {
                servings = Integer.parseInt(ioStrategy.getUserInput().trim());
                if (servings <= 0) {
                    ioStrategy.display("Number of servings must be positive.");
                    continue;
                }
            } catch (NumberFormatException e) {
                ioStrategy.display("Invalid number of servings. Use a number.");
                continue;
            }

            food.consumpt(date, servings, mealTime);
            int quantity = servings * food.getServingSize();
            ioStrategy.displayFormatted("%dg X %s consumed for %s on %s\n",
                    quantity, food, mealTime, date);
        }

        ioStrategy.display("Menu '" + menuName + "' consumed successfully!");
        return this;
    }

    @Override
    public String toString() {
        return "consume menu";
    }

    @Override
    public boolean isExitCommand() {
        return false;
    }
}

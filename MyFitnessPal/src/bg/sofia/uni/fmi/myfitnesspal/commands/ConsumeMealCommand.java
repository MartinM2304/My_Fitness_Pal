package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class ConsumeMealCommand implements Command {
    private final Controller controller;
    private final Scanner scanner;

    public ConsumeMealCommand(Controller controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public Command execute() {
        Map<Integer, String> menuIds = controller.getMealIds();
        if (menuIds.isEmpty()) {
            System.out.println("No menus available to consume.");
            return null;
        }

        System.out.println("Available menus:");
        for (Map.Entry<Integer, String> entry : menuIds.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }

        System.out.println("Which menu (ID):");
        int menuId;
        try {
            menuId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid menu ID. Use a number.");
            return null;
        }

        String menuName = menuIds.get(menuId);
        if (menuName == null) {
            System.out.println("Menu ID not found.");
            return null;
        }

        Meal menu = (Meal) controller.getItems().get(menuName);
        if (menu == null) {
            System.out.println("Menu not found.");
            return null;
        }

        System.out.println("When (date, format YYYY-MM-DD or similar):");
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

        for (Food food : menu.getFoods()) {
            System.out.println("Number of servings for " + food + ":");
            int servings;
            try {
                servings = Integer.parseInt(scanner.nextLine().trim());
                if (servings <= 0) {
                    System.out.println("Number of servings must be positive.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of servings. Use a number.");
                continue;
            }

            food.consumpt(date, servings, mealTime);
            int quantity = servings * food.getServingSize();
            System.out.printf("%dg X %s consumed for %s on %s\n", quantity, food, mealTime, date);
        }

        System.out.println("Menu '" + menuName + "' consumed successfully!");
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
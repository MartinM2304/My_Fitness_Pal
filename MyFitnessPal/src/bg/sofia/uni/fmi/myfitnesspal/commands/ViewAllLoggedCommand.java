package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ViewAllLoggedCommand implements Command {
    private final Controller controller;
    private final Scanner scanner;

    public ViewAllLoggedCommand(Controller controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    @Override
    public Command execute() {
        System.out.println("When (date):");
        String dateStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("wrong date format");
        }

        System.out.println("When (date):");
        System.out.println("-" + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

        Map<MealTime, List<String>> foodByMealTime = new EnumMap<>(MealTime.class);
        for (MealTime mealTime : MealTime.values()) {
            foodByMealTime.put(mealTime, new ArrayList<>());
        }

        double totalWaterLiters = 0;

        for (Consumable item : controller.getItems().values()) {
            if (item.getConsumptionLog().containsKey(date)) {
                List<ConsumptionEntry> entries = item.getConsumptionLog().get(date);
                if (item instanceof Food food) {
                    entries.stream()
                            .filter(FoodConsumptionEntry.class::isInstance)
                            .map(FoodConsumptionEntry.class::cast)
                            .forEach(entry -> {
                                int servings = entry.getServings();
                                MealTime mealTime = entry.getMealTime();
                                double totalGrams = food.getServingSize() * servings;
                                double totalCalories = food.getCalories() * servings;
                                double totalCarbs = food.getCarbs() * servings;
                                double totalFat = food.getFat() * servings;
                                double totalProtein = food.getProtein() * servings;
                                String foodEntry = String.format(
                                        ">%s (%dg; %.0f kcal; %.2fg, %.2fg, %.2fg)",
                                        food.getName(), (int) totalGrams, totalCalories,
                                        totalCarbs, totalFat, totalProtein);
                                foodByMealTime.get(mealTime).add(foodEntry);
                            });
                } else if (item instanceof Meal meal) {
                    entries.stream()
                            .filter(FoodConsumptionEntry.class::isInstance)
                            .map(FoodConsumptionEntry.class::cast)
                            .forEach(entry -> {
                                MealTime mealTime = entry.getMealTime();
                                List<Food> foods = meal.getFoods();
                                foods.forEach(food -> {
                                    int servings = entry.getServings();
                                    double totalGrams = food.getServingSize() * servings;
                                    double totalCalories = food.getCalories() * servings;
                                    double totalCarbs = food.getCarbs() * servings;
                                    double totalFat = food.getFat() * servings;
                                    double totalProtein = food.getProtein() * servings;
                                    String foodEntry = String.format(
                                            ">%s (%dg; %.0f kcal; %.2fg, %.2fg, %.2fg)",
                                            food.getName(), (int) totalGrams, totalCalories,
                                            totalCarbs, totalFat, totalProtein);
                                    foodByMealTime.get(mealTime).add(foodEntry);
                                });
                            });
                } else if (item instanceof Water) {
                    totalWaterLiters += entries.stream()
                            .filter(WaterConsumptionEntry.class::isInstance)
                            .map(WaterConsumptionEntry.class::cast)
                            .mapToDouble(WaterConsumptionEntry::getQuantity)
                            .sum() / 1000.0;
                }
            }
        }

        for (MealTime mealTime : MealTime.values()) {
            System.out.println(mealTime.name() + ":");
            List<String> foods = foodByMealTime.get(mealTime);
            if (foods.isEmpty()) {
                System.out.println("—");
            } else {
                foods.forEach(System.out::println);
            }
            System.out.println();
        }

        System.out.printf("Water: %.1fL%n", totalWaterLiters);
        return this;
    }

    @Override
    public boolean isExitCommand() {
        return false;
    }

    @Override
    public String toString() {
        return "view allfoods logged";
    }
}
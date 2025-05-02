package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.WaterConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ViewAllLoggedCommand implements Command {
    private final Controller controller;
    private final IOStrategy ioStrategy;

    public ViewAllLoggedCommand(Controller controller, IOStrategy ioStrategy) {
        this.controller = controller;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {
        ioStrategy.display("When (date):");
        String dateStr = ioStrategy.getUserInput();
        LocalDate date;
        try {
            date = DateParser.parse(dateStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("wrong date format");
        }

        Map<MealTime, List<String>> foodByMealTime =
                new EnumMap<>(MealTime.class);
        for (MealTime mealTime : MealTime.values()) {
            foodByMealTime.put(mealTime, new ArrayList<>());
        }

        double totalWaterLiters = 0;

        for (Consumable item : controller.getItems().values()) {
            if (item.getConsumptionLog().containsKey(date)) {
                List<ConsumptionEntry> entries =
                        item.getConsumptionLog().get(date);
                if (item instanceof Food food) {
                    entries.stream()
                            .filter(FoodConsumptionEntry.class::isInstance)
                            .map(FoodConsumptionEntry.class::cast)
                            .forEach(entry -> {
                                int servings = entry.getServings();
                                MealTime mealTime = entry.getMealTime();
                                double totalGrams =
                                        food.getServingSize() * servings;
                                double totalCalories =
                                        food.getCalories() * servings;
                                double totalCarbs = food.getCarbs() * servings;
                                double totalFat = food.getFat() * servings;
                                double totalProtein =
                                        food.getProtein() * servings;
                                String foodEntry = String.format(
                                ">%s (%dg; %.0f kcal; %.2fg, %.2fg, %.2fg)",
                                food.getName(), (int) totalGrams,
                                totalCalories,
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
                                    double totalGrams =
                                            food.getServingSize() * servings;
                                    double totalCalories =
                                            food.getCalories() * servings;
                                    double totalCarbs =
                                            food.getCarbs() * servings;
                                    double totalFat = food.getFat() * servings;
                                    double totalProtein =
                                            food.getProtein() * servings;
                                    String foodEntry = String.format(
                                    ">%s (%dg; %.0f kcal; %.2fg, %.2fg, %.2fg)",
                                    food.getName(), (int) totalGrams,
                                    totalCalories,
                                    totalCarbs, totalFat, totalProtein);
                                    foodByMealTime.get(mealTime).add(foodEntry);
                                });
                            });
                } else if (item instanceof Water) {
                    totalWaterLiters += entries.stream()
                            .filter(WaterConsumptionEntry.class::isInstance)
                            .map(WaterConsumptionEntry.class::cast)
                            .mapToDouble(WaterConsumptionEntry::getQuantity)
                            .sum();
                }
            }
        }

        for (MealTime mealTime : MealTime.values()) {
            ioStrategy.display(mealTime.name() + ":");
            List<String> foods = foodByMealTime.get(mealTime);
            if (foods.isEmpty()) {
                ioStrategy.display("—");
            } else {
                foods.forEach(System.out::println);
            }
            ioStrategy.display();
        }

        ioStrategy.displayFormatted("Water: %.1fL%n", totalWaterLiters);
        return this;
    }

    @Override
    public boolean isExitCommand() {
        return false;
    }

    @Override
    public String toString() {
        return "view all logged";
    }
}

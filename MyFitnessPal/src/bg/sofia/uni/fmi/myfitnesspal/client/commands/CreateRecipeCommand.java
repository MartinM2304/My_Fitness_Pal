package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.util.Map;

public class CreateRecipeCommand implements Command {
    private final Controller controller;
    private final IOStrategy ioStrategy;


    public CreateRecipeCommand(Controller constroller, IOStrategy ioStrategy) {
        this.controller = constroller;
        this.ioStrategy = ioStrategy;
    }


    @Override
    public Command execute() {

        ioStrategy.display("Name:");
        String name = ioStrategy.getUserInput();
        ioStrategy.display("Description (optional):");
        String description = ioStrategy.getUserInput();
        ioStrategy.display("Servings:");
        int servingSize;
        try {
            servingSize = Integer.parseInt(ioStrategy.getUserInput());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid servings");
        }

        Food.Builder recipeBuilder = new Food.Builder(name)
                .description(description.isEmpty() ? "" : description)
                .servingSize(servingSize)
                .isRecipe(true);

        int totalCalories = 0;
        double totalCarbs = 0.0;
        double totalFat = 0.0;
        double totalProtein = 0.0;
        StringBuilder componentsSummary = new StringBuilder();

        while (true) {
            ioStrategy.display("Items:");
            ioStrategy.display("All foods list:");
            Map<Integer, String> foodIds = controller.getFoodIds();
            for (Map.Entry<Integer, String> entry : foodIds.entrySet()) {
                ioStrategy.display(String.format("%d. %s", entry.getKey(),
                        entry.getValue()));
            }

            String foodIdStr = ioStrategy.getUserInput();
            int foodId;
            try {
                foodId = Integer.parseInt(foodIdStr);
            } catch (NumberFormatException e) {
                ioStrategy.display("Invalid food ID");
                continue;
            }

            String foodKey = foodIds.get(foodId);
            if (foodKey == null) {
                ioStrategy.display("Invalid food ID");
                continue;
            }

            Consumable consumable = controller.getItems().get(foodKey);
            if (!(consumable instanceof Food food) || food.isRecipe()) {
                ioStrategy.display(
                "Selected item is not a valid ingredient (cannot use recipes)");
                continue;
            }

            ioStrategy.display("Number of Servings:");
            double servings;
            try {
                servings = Double.parseDouble(ioStrategy.getUserInput());
            } catch (NumberFormatException e) {
                ioStrategy.display("Invalid number of servings");
                continue;
            }

            totalCalories += (int) (food.getCalories() * servings);
            totalCarbs += food.getCarbs() * servings;
            totalFat += food.getFat() * servings;
            totalProtein += food.getProtein() * servings;
            componentsSummary.append(
                    String.format("%.2f x %s\n", servings, food.toString()));

            ioStrategy.display("More? (yes/no)");
            if (!ioStrategy.getUserInput().equalsIgnoreCase("yes")) {
                break;
            }
        }

        Food recipe = recipeBuilder
                .calories(totalCalories)
                .carbs(totalCarbs)
                .fat(totalFat)
                .protein(totalProtein)
                .servingsPerContainer(1)
                .build();

        controller.addFood(recipe);
        ioStrategy.display("Created Recipe: " + recipe.getName());
        ioStrategy.display("From:");
        ioStrategy.display(componentsSummary.toString());
        ioStrategy.display("——————————");
        ioStrategy.display("Serving " + recipe.toString());

        return this;
    }
}

package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;


public class CreateFoodCommand implements Command {
    private IOStrategy ioStrategy;
    private final Controller controller;

    public CreateFoodCommand(Controller controller, IOStrategy strategy) {
        this.ioStrategy = strategy;
        this.controller = controller;
    }

    @Override
    public Command execute() {
        ioStrategy.display("Name:");
        String name = ioStrategy.getUserInput().trim();
        Food.Builder builder = new Food.Builder(name);

        ioStrategy.display("Description (optional):");
        String description = ioStrategy.getUserInput().trim();
        if (!description.isBlank()) {
            builder.description(description);
        }

        try {
            ioStrategy.display("Serving Size (g):");
            builder.servingSize(
                    Integer.parseInt(ioStrategy.getUserInput().trim()));

            ioStrategy.display("Servings per container:");
            builder.servingsPerContainer(Integer.parseInt(
                    ioStrategy.getUserInput().trim()));

            ioStrategy.display("Amount per serving:");
            ioStrategy.display("Calories (kcal):");
            builder.calories(
                    Integer.parseInt(ioStrategy.getUserInput().trim()));

            ioStrategy.display("Carbs (g):");
            builder.carbs(Double.parseDouble(ioStrategy.getUserInput().trim()));

            ioStrategy.display("Fat (g):");
            builder.fat(Double.parseDouble(ioStrategy.getUserInput().trim()));

            ioStrategy.display("Protein (g):");
            builder.protein(
                    Double.parseDouble(ioStrategy.getUserInput().trim()));
        } catch (NumberFormatException e) {
            ioStrategy.display("Invalid input. Please "
                    + "use numbers where required.");
            return null;
        }
        Food food = builder.build();
        controller.addFood(food);
        ioStrategy.display("Food added successfully!");
        return this;
    }

    @Override
    public String toString() {
        return "create food";
    }
}

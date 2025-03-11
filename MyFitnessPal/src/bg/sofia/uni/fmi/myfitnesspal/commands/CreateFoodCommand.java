package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;

import java.util.Scanner;

public class CreateFoodCommand implements Command {
    private final Food food;
    private final Scanner scanner;
    private final Controller controller;

    public CreateFoodCommand(Food food, Scanner scanner, Controller controller) {
        this.food = food;
        this.scanner = scanner;
        this.controller = controller;
    }

    @Override
    public Command execute() {
        System.out.println("Name:");
        String name = scanner.nextLine();

        Food.Builder builder = new Food.Builder(name);

        System.out.println("Description (optional):");
        String description = scanner.nextLine();
        if (!description.isBlank()) {
            builder.description(description);
        }

        System.out.println("Serving Size (g):");
        builder.servingSize(Integer.parseInt(scanner.nextLine()));

        System.out.println("Servings per container:");
        builder.servingsPerContainer(Integer.parseInt(scanner.nextLine()));

        System.out.println("Amount per serving:");
        System.out.println("Calories (kcal):");
        builder.calories(Integer.parseInt(scanner.nextLine()));

        System.out.println("Carbs (g):");
        builder.carbs(Double.parseDouble(scanner.nextLine()));

        System.out.println("Fat (g):");
        builder.fat(Double.parseDouble(scanner.nextLine()));

        System.out.println("Protein (g):");
        builder.protein(Double.parseDouble(scanner.nextLine()));

        Food food = builder.build();
        controller.addFood(food);

        System.out.println("Food added successfully!");
        return this;
    }
}

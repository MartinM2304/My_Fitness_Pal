package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;

import java.util.Scanner;

public class CreateFoodCommand implements Command {
    private final Scanner scanner;
    private final Controller controller;

    public CreateFoodCommand(Scanner scanner, Controller controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    @Override
    public Command execute() {
        System.out.println("Name:");
        String name = scanner.nextLine().trim();
        Food.Builder builder = new Food.Builder(name);

        System.out.println("Description (optional):");
        String description = scanner.nextLine().trim();
        if (!description.isBlank()) {
            builder.description(description);
        }

        try {
            System.out.println("Serving Size (g):");
            builder.servingSize(Integer.parseInt(scanner.nextLine().trim()));

            System.out.println("Servings per container:");
            builder.servingsPerContainer(Integer.parseInt(scanner.nextLine().trim()));

            System.out.println("Amount per serving:");
            System.out.println("Calories (kcal):");
            builder.calories(Integer.parseInt(scanner.nextLine().trim()));

            System.out.println("Carbs (g):");
            builder.carbs(Double.parseDouble(scanner.nextLine().trim()));

            System.out.println("Fat (g):");
            builder.fat(Double.parseDouble(scanner.nextLine().trim()));

            System.out.println("Protein (g):");
            builder.protein(Double.parseDouble(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please use numbers where required.");
            return null;
        }
        Food food = builder.build();
        controller.addFood(food);
        System.out.println("Food added successfully!");
        return this;
    }

    @Override
    public String toString() {
        return "create food";
    }
}

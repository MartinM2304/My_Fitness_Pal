package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();
    private final Controller controller;
    private final CommandValidator commandValidator;

    public CommandFactory(Map<String, Consumable> items, Scanner scanner, Controller controller) {
        this.controller = controller;

        Command drinkWater = new DrinkWaterCommand((Water) items.get("water"), scanner);
        Command checkWater = new CheckWaterCommand((Water) items.get("water"), scanner);

        Command createFood = new CreateFoodCommand((Food) items.get("food"), scanner, controller);
        Command viewAllFood = new ViewAllFoodsCommand(controller);

        commands.put(drinkWater.toString(),drinkWater);
        commands.put(checkWater.toString(),checkWater);
        commands.put(createFood.toString(),createFood);
        commands.put(viewAllFood.toString(),viewAllFood);

        commandValidator=new CommandValidator(commands.keySet());
    }

    public Command getCommand(String commandName) {
        if(!commandValidator.isValidCommand(commandName)){
            throw  new IllegalArgumentException("this is not a valid command");
        }
        String normalizedCommand = commandName.trim().toLowerCase();
        return commands.getOrDefault(normalizedCommand, () -> null);
    }

}

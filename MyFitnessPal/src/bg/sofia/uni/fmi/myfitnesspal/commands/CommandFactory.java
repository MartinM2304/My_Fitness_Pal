package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();
    private final Set<String> validCommands = Set.of("drink water", "check water", "exit");
    private final Controller controller;

    public CommandFactory(Map<String, Consumable> items, Scanner scanner, Controller controller) {
        this.controller = controller;
        if (items.containsKey("water")) {
            commands.put("drink water", new DrinkWaterCommand((Water) items.get("water"), scanner));
            commands.put("check water", new CheckWaterCommand((Water) items.get("water"), scanner));
        }
        if (items.containsKey("food")) {
            commands.put("create food", new CreateFoodCommand((Food) items.get("food"), scanner, controller));
            commands.put("view all foods", new ViewAllFoodsCommand(controller));
        }
        //commands.put("exit", new ExitCommand());
    }

    public Command getCommand(String commandName) {
        String normalizedCommand = commandName.trim().toLowerCase();
        return commands.getOrDefault(normalizedCommand, () -> null);
    }

    public Set<String> getValidCommands() {
        return validCommands;
    }
}

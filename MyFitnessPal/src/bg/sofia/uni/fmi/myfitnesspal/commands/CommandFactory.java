package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.ItemSerializer;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();
    private final Set<String> validCommands = Set.of("drink water", "check water", "exit");

    public CommandFactory(Map<String, Consumable> items, Scanner scanner) {
        if (items.containsKey("water")) {
            commands.put("drink water", new DrinkWaterCommand((Water)items.get("water"), scanner));
            //commands.put("check water", new CheckWaterCommand(items.get("water"), scanner));
        }
        if (items.containsKey("food")) {
            //commands.put("eat food", new EatFoodCommand(items.get("food"), scanner));
            //commands.put("check food", new CheckFoodCommand(items.get("food"), scanner));
        }
        //commands.put("exit", new ExitCommand());
    }

    public Command getCommand(String commandName) {
        String normalizedCommand = commandName.trim().toLowerCase();
        return commands.getOrDefault(normalizedCommand, () -> System.out.println("Invalid command"));
    }

    public Set<String> getValidCommands() {
        return validCommands;
    }
}

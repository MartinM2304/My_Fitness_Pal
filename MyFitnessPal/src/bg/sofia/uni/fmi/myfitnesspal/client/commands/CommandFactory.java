package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();
    private final Controller controller;
    private final CommandValidator commandValidator;

    public CommandFactory(Map<String, Consumable> items, IOStrategy ioStrategy,
                          Controller controller) {
        this.controller = controller;

        Command drinkWater =
                new DrinkWaterCommand((Water) items.get("water"), ioStrategy);
        Command checkWater =
                new CheckWaterCommand((Water) items.get("water"), ioStrategy);
        Command createMenu = new CreateMealCommand(controller, ioStrategy);
        Command consumeMeal = new ConsumeMealCommand(controller, ioStrategy);
        Command createFood = new CreateFoodCommand(controller, ioStrategy);
        Command viewAllFood = new ViewAllFoodsCommand(controller, ioStrategy);
        Command logFood = new LogFoodCommand(controller, ioStrategy);
        Command viewAllLogged =
                new ViewAllLoggedCommand(controller, ioStrategy);
        Command exit = new ExitCommand(ioStrategy);

        commands.put(drinkWater.toString(), drinkWater);
        commands.put(checkWater.toString(), checkWater);
        commands.put(createFood.toString(), createFood);
        commands.put(viewAllFood.toString(), viewAllFood);
        commands.put(logFood.toString(), logFood);
        commands.put(createMenu.toString(), createMenu);
        commands.put(consumeMeal.toString(), consumeMeal);
        commands.put(viewAllLogged.toString(), viewAllLogged);
        commands.put(exit.toString(), exit);

        commandValidator = new CommandValidator(commands.keySet());
    }

    public Command getCommand(String commandName) {
        if (!commandValidator.isValidCommand(commandName)) {
            throw new IllegalArgumentException("this is not a valid command");
        }
        String normalizedCommand = commandName.trim().toLowerCase();
        return commands.getOrDefault(normalizedCommand, () -> null);
    }

}

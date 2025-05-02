package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

public class ViewAllRecipesCommand implements Command {
    private final Controller controller;
    private final IOStrategy ioStrategy;

    public ViewAllRecipesCommand(Controller controller, IOStrategy ioStrategy) {
        this.controller = controller;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {
        int index = 1;
        for (Consumable consumable : controller.getItems().values()) {
            if (consumable instanceof Food food && food.isRecipe()) {
                ioStrategy.display(
                        String.format("%d. %s", index++, food.toString()));
            }
        }
        if (index == 1) {
            ioStrategy.display("No recipes found.");
        }

        return this;
    }

    @Override
    public boolean isExitCommand() {
        return false;
    }
}

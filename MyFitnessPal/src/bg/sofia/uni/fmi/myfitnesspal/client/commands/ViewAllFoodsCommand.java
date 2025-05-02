package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.util.Map;

public class ViewAllFoodsCommand implements Command {

    private Controller controller;
    private final IOStrategy ioStrategy;

    public ViewAllFoodsCommand(Controller controller, IOStrategy ioStrategy) {
        this.controller = controller;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {

        Map<String, Consumable> items = controller.getItems();
        boolean hasFood = false;

        for (Consumable item : items.values()) {
            if (item instanceof Food) {
                hasFood = true;
                ioStrategy.display(item.toString());
            }
        }

        if (!hasFood) {
            ioStrategy.display("No foods added yet.");
            return this;
        }
        return this;
    }

    @Override
    public String toString() {
        return "view all foods";
    }
}

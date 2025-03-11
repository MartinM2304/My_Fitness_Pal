package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;

import java.util.Map;

public class ViewAllFoodsCommand implements Command{

    private Controller controller;

    public ViewAllFoodsCommand(Controller controller) {
        this.controller = controller;
    }

    @Override
    public Command execute() {

        Map<String, Consumable> items = controller.getItems();
        boolean hasFood = false;

        for (Consumable item : items.values()) {
            if (item instanceof Food) {
                hasFood = true;
                System.out.println(item);
            }
        }

        if (!hasFood) {
            System.out.println("No foods added yet.");


            return this;
        }
        return this;
    }
}

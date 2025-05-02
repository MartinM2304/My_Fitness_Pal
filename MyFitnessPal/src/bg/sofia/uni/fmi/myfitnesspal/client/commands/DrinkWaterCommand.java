package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.time.LocalDate;

public class DrinkWaterCommand implements Command {

    private final Water water;
    private IOStrategy ioStrategy;

    public DrinkWaterCommand(Water water, IOStrategy ioStrategy) {
        this.water = water;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {
        ioStrategy.display("When?");
        String stringDate = ioStrategy.getUserInput();
        LocalDate date = DateParser.parse(stringDate);

        ioStrategy.display("How much?");
        String stringQuantity = ioStrategy.getUserInput();
        int quantity = Integer.parseInt(stringQuantity);

        water.consumpt(date, quantity);
        return this;
    }

    @Override
    public String toString() {
        return "drink water";
    }
}

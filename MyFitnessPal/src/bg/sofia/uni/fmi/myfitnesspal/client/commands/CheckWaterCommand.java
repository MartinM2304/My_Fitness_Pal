package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.WaterConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.time.LocalDate;
import java.util.List;

public class CheckWaterCommand implements Command {
    private final Water water;
    private final IOStrategy ioStrategy;

    public CheckWaterCommand(Water water, IOStrategy strategy) {
        this.water = water;
        this.ioStrategy = strategy;
    }

    @Override
    public Command execute() {
        ioStrategy.display("When?");
        String stringDate = ioStrategy.getUserInput();
        LocalDate date;
        try {
            date = DateParser.parse(stringDate);
        } catch (IllegalArgumentException e) {
            ioStrategy.display("Invalid date format. Use: YYYY-MM-DD");
            return null;
        }

        List<ConsumptionEntry> consumptionForDate;
        try {
            consumptionForDate = water.getConsumptionForDate(date);
        } catch (IllegalArgumentException e) {
            ioStrategy.display("You didn't drink any water then.");
            return null;
        }

        for (ConsumptionEntry consumption : consumptionForDate) {
            if (consumption instanceof WaterConsumptionEntry waterEntry) {
                ioStrategy.display(waterEntry.getQuantity() + " ml");
            } else {
                ioStrategy.display(
                        "Unexpected consumption entry type for water.");
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "check water";
    }

    @Override
    public boolean isExitCommand() {
        return false; // Добавено, ако Command интерфейсът го изисква
    }
}

package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CheckWaterCommand implements Command {
    private final Water water;
    private final Scanner scanner;

    public CheckWaterCommand(Water water, Scanner scanner) {
        this.water = water;
        this.scanner = scanner;
    }

    @Override
    public Command execute() {
        System.out.println("When?");
        String stringDate = scanner.nextLine();
        LocalDate date;
        try {
            date = DateParser.parse(stringDate);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use: YYYY-MM-DD");
            return null;
        }

        List<ConsumptionEntry> consumptionForDate;
        try {
            consumptionForDate = water.getConsumptionForDate(date);
        } catch (IllegalArgumentException e) {
            System.out.println("You didn't drink any water then.");
            return null;
        }

        for (ConsumptionEntry consumption : consumptionForDate) {
            if (consumption instanceof WaterConsumptionEntry waterEntry) {
                System.out.println(waterEntry.getQuantity() + " ml");
            } else {
                System.out.println("Unexpected consumption entry type for water.");
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
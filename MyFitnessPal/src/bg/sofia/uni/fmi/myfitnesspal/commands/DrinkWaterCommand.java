package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

import java.time.LocalDate;
import java.util.Scanner;

public class DrinkWaterCommand implements Command {

    private final Water water;
    private final Scanner scanner;

    public DrinkWaterCommand(Water water, Scanner scanner) {
        this.water = water;
        this.scanner = scanner;
    }

    @Override
    public Command execute() {
        System.out.println("When?");
        String stringDate = scanner.nextLine();
        LocalDate date = DateParser.parse(stringDate);

        System.out.println("How much?");
        String stringQuantity = scanner.nextLine();
        int quantity = Integer.parseInt(stringQuantity);

        water.consumpt(date, quantity);
        return this;
    }

    @Override
    public String toString() {
        return "drink water";
    }
}

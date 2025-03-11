package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CheckWaterCommand implements Command{
    private final Water water;
    private final Scanner scanner;

    public CheckWaterCommand(Water water, Scanner scanner){
        this.water=water;
        this.scanner=scanner;
    }

    @Override
    public Command execute(){
        System.out.println("When?");
        String stringDate=scanner.nextLine();
        LocalDate date= DateParser.parse(stringDate);

        List<Integer> consumptionForDate=water.getConsumptionForDate(date);
        for(int consumption:consumptionForDate){
            System.out.print(consumption);
            System.out.print(" ");
        }
        return this;
    }
}

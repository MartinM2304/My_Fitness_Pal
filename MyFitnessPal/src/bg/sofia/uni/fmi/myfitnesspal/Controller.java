package bg.sofia.uni.fmi.myfitnesspal;

import bg.sofia.uni.fmi.myfitnesspal.commands.Command;
import bg.sofia.uni.fmi.myfitnesspal.commands.CommandFactory;
import bg.sofia.uni.fmi.myfitnesspal.commands.CommandValidator;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.ItemSerializer;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Controller {
    public static final String FILE_NAME="storage.txt";
    private  Map<String, Consumable> items=new HashMap<>();
    //private CommandValidator commandValidator=new CommandValidator();
    private CommandFactory commandFactory;
    private final Scanner sc= new Scanner(System.in);
    private ItemSerializer serializer;


    public Controller(){
        initController();
    }

    public Command executeCommand(String command){
        Command executedCommand= commandFactory.getCommand(command);
        if(executedCommand.isExitCommand()){
            serializer.saveData();
        }
        executedCommand.execute();
        return executedCommand;
    }

    private void initController(){
        items.put("water",new Water());

        commandFactory=new CommandFactory(items,sc,this);
        serializer=new ItemSerializer(FILE_NAME, new HashSet<>(items.values()));
    }

    public void addFood(Food food) {
        items.put(food.toString(), food);
    }

    public Map<String,Consumable> getItems(){
        return items;
    }
}

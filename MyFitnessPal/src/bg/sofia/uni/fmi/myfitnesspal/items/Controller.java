package bg.sofia.uni.fmi.myfitnesspal.items;

import bg.sofia.uni.fmi.myfitnesspal.commands.Command;
import bg.sofia.uni.fmi.myfitnesspal.commands.CommandFactory;
import bg.sofia.uni.fmi.myfitnesspal.commands.CommandValidator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Controller {
    public static final String FILE_NAME="storage.txt";
    private  Map<String,Consumable> items=new HashMap<>();
    private CommandValidator commandValidator=new CommandValidator();
    private CommandFactory commandFactory;
    private final Scanner sc= new Scanner(System.in);
    private ItemSerializer serializer;


    public Controller(){
        initController();
    }

    public Command executeCommand(String command){
        if(!commandValidator.isValidCommand(command)){
            throw  new IllegalArgumentException("this is not a valid command");
        }
        Command executedCommand= commandFactory.getCommand(command);
        if(executedCommand.isExitCommand()){
            serializer.saveData();
        }
        return executedCommand;
    }

    private void initController(){
        items.put("water",new Water());

        commandFactory=new CommandFactory(items,sc);
        serializer=new ItemSerializer(FILE_NAME, new HashSet<>(items.values()));
    }
}

package bg.sofia.uni.fmi.myfitnesspal.commands;

import java.util.HashSet;
import java.util.Set;

public class CommandValidator {
    private Set<String> validCommands=new HashSet();

    public CommandValidator(){
        initCommandValidator();
    }

    public boolean isValidCommand(String command){
        command.toLowerCase();
        return validCommands.contains(command);
    }
    private void initCommandValidator(){
        validCommands.add("drink water");
        validCommands.add("check water");
    }
}

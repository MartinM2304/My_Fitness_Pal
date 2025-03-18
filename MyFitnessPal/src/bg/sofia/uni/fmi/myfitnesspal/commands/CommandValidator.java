package bg.sofia.uni.fmi.myfitnesspal.commands;

import java.util.HashSet;
import java.util.Set;

public class CommandValidator {
    private Set<String> validCommands;

    public CommandValidator(Set<String>validCommands){
        //initCommandValidator();
        this.validCommands=validCommands;
    }

    public boolean isValidCommand(String command){
        command.toLowerCase();
        return validCommands.contains(command);
    }
    private void initCommandValidator(){
        validCommands.add("drink water");
        validCommands.add("check water");
        validCommands.add("create food");
        validCommands.add("view all foods");
        validCommands.add("exit");
    }
}

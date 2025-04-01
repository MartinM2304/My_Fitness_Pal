package bg.sofia.uni.fmi.myfitnesspal.commands;

import java.util.Set;

public class CommandValidator {
    private Set<String> validCommands;

    public CommandValidator(Set<String> validCommands) {
        this.validCommands = validCommands;
    }

    public boolean isValidCommand(String command) {
        command = command.toLowerCase().trim();
        return validCommands.contains(command);
    }
}

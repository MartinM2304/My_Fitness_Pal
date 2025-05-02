package bg.sofia.uni.fmi.myfitnesspal.client.commands;

public interface Command {
    Command execute();

    default boolean isExitCommand() {
        return false;
    }
}

package bg.sofia.uni.fmi.myfitnesspal.commands;

public interface Command {
    Command execute();

    default boolean isExitCommand(){
        return false;
    }
}

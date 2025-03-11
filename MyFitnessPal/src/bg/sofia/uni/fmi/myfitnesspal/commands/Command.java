package bg.sofia.uni.fmi.myfitnesspal.commands;

public interface Command {
    void execute();

    default boolean isExitCommand(){
        return false;
    }
}

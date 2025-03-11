package bg.sofia.uni.fmi.myfitnesspal.commands;

public class ExitCommand implements Command{

    @Override
    public Command execute() {
        System.out.println("Exiting the application...");
        System.exit(0);
        return this;
    }

    @Override
    public boolean isExitCommand(){
        return true;
    }
}

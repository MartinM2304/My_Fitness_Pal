package bg.sofia.uni.fmi.myfitnesspal.commands;

public class ExitCommand implements Command{

    @Override
    public void execute() {
        System.out.println("Exiting the application...");
        System.exit(0);
    }

    @Override
    public boolean isExitCommand(){
        return true;
    }
}

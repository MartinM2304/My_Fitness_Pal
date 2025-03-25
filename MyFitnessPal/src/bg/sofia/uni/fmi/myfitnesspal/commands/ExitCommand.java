package bg.sofia.uni.fmi.myfitnesspal.commands;

public class ExitCommand implements Command{

    @Override
    public Command execute() {
        exitApplication();
        return this;
    }

    @Override
    public boolean isExitCommand(){
        return true;
    }

    @Override
    public String toString() {
        return "exit";
    }

    //in order to be testable
    protected void exitApplication() {
        System.out.println("Exiting the application...");
        System.exit(0);
    }
}

package bg.sofia.uni.fmi.myfitnesspal.client.commands;

import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

public class ExitCommand implements Command {

    private final IOStrategy ioStrategy;

    public ExitCommand(IOStrategy ioStrategy) {
        this.ioStrategy = ioStrategy;
    }

    @Override
    public Command execute() {
        exitApplication();
        return this;
    }

    @Override
    public boolean isExitCommand() {
        return true;
    }

    @Override
    public String toString() {
        return "exit";
    }

    protected void exitApplication() {
        ioStrategy.display("Exiting the application...");
        System.exit(0);
    }
}

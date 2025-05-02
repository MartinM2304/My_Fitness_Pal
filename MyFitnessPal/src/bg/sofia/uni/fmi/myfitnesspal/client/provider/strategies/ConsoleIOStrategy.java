package bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies;

import bg.sofia.uni.fmi.myfitnesspal.client.provider.ConsoleInputProvider;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.ConsoleOutputProvider;

import java.io.IOException;

public class ConsoleIOStrategy implements IOStrategy {
    ConsoleInputProvider inputProvider;
    ConsoleOutputProvider outputProvider;

    ConsoleIOStrategy() {
        inputProvider = new ConsoleInputProvider();
        outputProvider = new ConsoleOutputProvider();
    }

    @Override
    public void display(String message) {
        outputProvider.println(message);
    }

    @Override
    public void display() {
        outputProvider.println();
    }

    @Override
    public void displayFormatted(String format, Object... args) {
        outputProvider.printf(format, args);
    }

    @Override
    public String getUserInput(){
        return inputProvider.readLine();
    }

    @Override
    public void close(){
        inputProvider.close();
    }
}

package bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies;

import java.io.IOException;

public class IOStrategiesFactory {
    public static IOStrategy getIOStrategy(String type){
        IOStrategy result;
        result = switch (type.trim().toLowerCase()) {
            case "console" -> new ConsoleIOStrategy();

            case "socket" -> new SocketIOStrategy();

            default ->
                    throw new IllegalArgumentException("IO type is not valid");
        };

        return result;
    }
}

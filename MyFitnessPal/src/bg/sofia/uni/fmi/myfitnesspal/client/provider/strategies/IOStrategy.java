package bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies;

import java.io.IOException;

public interface IOStrategy extends  AutoCloseable{
    void display(String message);

    void display();

    void displayFormatted(String format, Object... args);

    String getUserInput();

    @Override
    void close()throws Exception;
}

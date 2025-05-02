package bg.sofia.uni.fmi.myfitnesspal.client.provider;

import java.util.Scanner;

public class ConsoleInputProvider implements InputProvider {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public String readLine() {
        return sc.nextLine();
    }

    @Override
    public boolean hasNextLine() {
        return sc.hasNextLine();
    }

    @Override
    public void close() {
        sc.close();
    }
}

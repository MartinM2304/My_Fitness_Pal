package bg.sofia.uni.fmi.myfitnesspal.client.provider;


public class ConsoleOutputProvider implements OutputProvider {
    @Override
    public void print(String string) {
        System.out.print(string);
    }

    @Override
    public void println(String string) {
        System.out.println(string);
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}

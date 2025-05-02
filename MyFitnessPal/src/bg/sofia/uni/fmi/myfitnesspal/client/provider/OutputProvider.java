package bg.sofia.uni.fmi.myfitnesspal.client.provider;

public interface OutputProvider {
    void print(String string);
    void println(String string);
    void println();
    void printf(String format, Object... args);
}

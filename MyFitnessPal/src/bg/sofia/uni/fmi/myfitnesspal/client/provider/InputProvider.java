package bg.sofia.uni.fmi.myfitnesspal.client.provider;

public interface InputProvider {
    String readLine();
    boolean hasNextLine();
    void close();
}

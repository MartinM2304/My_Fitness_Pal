import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategiesFactory;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

public class Main {
    public Main() {
    } // totaly wrong, pls remove from checkstyle!!!

    public static void main(String[] args) {
        IOStrategy ioStrategy= IOStrategiesFactory.getIOStrategy("console");
        Controller controller = new Controller("default",ioStrategy);
        controller.start();
    }
}

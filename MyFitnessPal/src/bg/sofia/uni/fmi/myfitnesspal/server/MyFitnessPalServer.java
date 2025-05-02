package bg.sofia.uni.fmi.myfitnesspal.server;

import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyFitnessPalServer {
    private ServerSocket serverSocket;
    private int port;
    private ExecutorService executorService;

    public MyFitnessPalServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        executorService = Executors.newCachedThreadPool();
        serverSocket = new ServerSocket(port);

        while (!serverSocket.isClosed()) {
            Socket client = serverSocket.accept();
            executorService.submit(new ClientHandler(client));
        }
    }

    public void stop() {
        executorService.shutdown();
        try {
            serverSocket.close();
        }catch (IOException e){
            System.err.println("Error closing server"+e.getMessage());
        }
    }

    public void main(){
        MyFitnessPalServer server = new MyFitnessPalServer(8080);
        try {
            server.start();
        }catch (IOException e){
            System.err.println("Error in server" + e.getMessage());
        }finally {
            server.stop();
        }
    }

}

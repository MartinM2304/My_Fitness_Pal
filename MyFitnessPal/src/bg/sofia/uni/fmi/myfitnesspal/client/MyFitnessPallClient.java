package bg.sofia.uni.fmi.myfitnesspal.client;

import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategiesFactory;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;

import java.io.IOException;
import java.net.Socket;

public class MyFitnessPallClient {
    private final String host;
    private final int port;
    private String nickname;

    public MyFitnessPallClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void main() {
        MyFitnessPallClient client = new MyFitnessPallClient("localhost", 8080);
        client.start();
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             IOStrategy socketIO = IOStrategiesFactory.getIOStrategy(
                     "socket");
             IOStrategy clientIO = IOStrategiesFactory.getIOStrategy(
                     "console")) {
            authenticateUser(socketIO,clientIO);

            while (true){
                String serverMessage = socketIO.getUserInput();
                if(serverMessage==null){
                    break;
                }
                if(serverMessage.equals("> ")){
                    String command = clientIO.getUserInput();
                    socketIO.display(command);
                    if(command.equals("exit")){
                        break;
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error with communication with socket"+e.getMessage());
        } catch (Exception e) {
            System.err.println("cant close the io"+e.getMessage());
        }
    }

    public String authenticateUser(IOStrategy socketIO, IOStrategy clientIO){
        String serverMessage =socketIO.getUserInput();
        clientIO.display(serverMessage);

        String nicknamePrompt = socketIO.getUserInput();
        clientIO.display(nicknamePrompt);

        String nickname = clientIO.getUserInput();
        socketIO.display(nickname);

        //add password if needed;

        this.nickname = nickname;
        return this.nickname;
    }
}

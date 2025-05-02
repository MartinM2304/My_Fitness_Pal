package bg.sofia.uni.fmi.myfitnesspal.server;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.commands.Command;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategiesFactory;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.SocketIOStrategy;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Controller controller;
    IOStrategy ioStrategy;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        IOStrategy ioStrategy = IOStrategiesFactory.getIOStrategy("socket");
        ((SocketIOStrategy) ioStrategy).addSocket(socket);
    }


    @Override
    public void run() {

        String nickname =authenticateUser();
        try {
            while (true){
                ioStrategy.display("> ");
                String command = ioStrategy.getUserInput();
                if (command == null || command.trim().isEmpty()) continue;
                Command executedCommand = controller.executeCommand(command);
                if (executedCommand != null && executedCommand.isExitCommand()) {
                    break;
                }
            }
        }catch (RuntimeException e){
            ioStrategy.display("Error in server:"+ e.getMessage());
        }finally {
            closeIO();
            closeSocket();
        }
    }

    public String authenticateUser(){
        ioStrategy.display("Welcome to MyFitnessPal Server");
        ioStrategy.display("Please enter nickname");

        String nickname = ioStrategy.getUserInput();
        //add password validation if needed

        return nickname;
    }

    private void closeIO(){
        try {
            ioStrategy.close();
        } catch (Exception e) {
            System.err.println("Error closing IO strategy: " + e.getMessage());
        }
    }

    private void closeSocket(){
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            ioStrategy.display("Error closing socket: " + e.getMessage());
        }
    }
}

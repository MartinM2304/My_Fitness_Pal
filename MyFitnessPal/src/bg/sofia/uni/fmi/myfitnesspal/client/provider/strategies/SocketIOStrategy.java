package bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketIOStrategy implements IOStrategy{
    private PrintWriter out;
    private BufferedReader in;


    @Override
    public void display(String message) {
        out.println(message);
    }

    @Override
    public void display() {
        out.println();
    }

    @Override
    public void displayFormatted(String format, Object... args) {
        out.println(String.format(format, args));
    }

    @Override
    public String getUserInput(){
        String input;
        try {
            input = in.readLine();
        }catch (IOException e){
            throw  new RuntimeException("Input from socket is invalid"+e.getMessage());
        }
        return input != null ? input : "";
    }

    @Override
    public void close() {
        out.close();
        try {
            in.close();
        }catch (IOException e){
            System.err.println("Error closing input "+e.getMessage());
        }
    }

    public void addSocket(Socket socket){
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            throw new RuntimeException("Error with initializing input and output of socket"+e.getMessage());
        }
    }
}

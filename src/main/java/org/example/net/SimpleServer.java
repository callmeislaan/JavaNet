package org.example.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 1234);
        serverSocket.bind(serverAddress);
        System.out.println("Server is running. Waiting for client...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message = reader.readLine();
            if (message != null) {
                System.out.println("Client says: " + message);
            } else {
                System.out.println("Client disconnected.");
            }
            System.out.println("Client says: " + message);
        }
    }
}

class SimpleClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 1234);
        socket.connect(serverAddress);
        String message = "Hello from client!";
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(message.getBytes());
        System.out.println("Message sent to server: " + message);
    }
}

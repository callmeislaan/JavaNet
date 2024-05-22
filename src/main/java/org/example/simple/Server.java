package org.example.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

/**
 * @author phuocht3
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
        serverSocket.bind(socketAddress);
        System.out.println("Server is listening on port 6868");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected to client");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String clientInput = input.readLine();
            System.out.println(clientInput);

            output.println("Hello from server");
        }
    }
}

package org.example.square;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, 6868);
            serverSocket.bind(socketAddress);
            System.out.println("Server is listening on port 6868");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Connected to client");

                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                    String clientInput;
                    while ((clientInput = input.readLine()) != null) {
                        int number = Integer.parseInt(clientInput);
                        if (number == 0) {
                            System.out.println("Client requested to close connection.");
                            break;
                        }

                        int square = number * number;
                        output.println(square);
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

}

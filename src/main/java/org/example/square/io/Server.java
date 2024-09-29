package org.example.square.io;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            System.out.println("Thread: " + Thread.currentThread().getName());
            serverSocket = new ServerSocket();
            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
            serverSocket.bind(socketAddress);
            while (true) {
                Socket clientSocket = serverSocket.accept();

                new Thread(() -> {
                    try {
                        System.out.println("Thread: " + Thread.currentThread().getName());
                        System.out.println("Connected to client: " + clientSocket);
                        // Connected to client: Socket[addr=/127.0.0.1,port=57884,localport=8080]

                        InputStream in = clientSocket.getInputStream();
                        OutputStream out = clientSocket.getOutputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        PrintWriter writer = new PrintWriter(out);

                        String clientInput;
                        while ((clientInput = reader.readLine()) != null) { // Cho phép gọi nhiều món
                            int number = Integer.parseInt(clientInput);
                            System.out.println("client input: " + clientInput);
                            if (number == 0) {
                                System.out.println("Client requested to close connection.");
                                break;
                            }

                            int square = number * number;
                            writer.printf("%d square is %d", number, square);
                            writer.println();
                            writer.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
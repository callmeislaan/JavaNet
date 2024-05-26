package org.example.performance;

import java.io.*;
import java.net.*;

public class Blocking {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
            serverSocket.bind(socketAddress);
            System.out.println("server is running on port 8080");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(5000);
                handle(clientSocket);
            }

        }
    }

    private static void handle(Socket clientSocket) throws IOException {
        System.out.println(Thread.currentThread().getName() + " - accepted client: " + clientSocket);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            builder.append(line).append("\n");
        }
        String header = builder.toString();

        String responseBody = "<html><body><h1>Hello, World!</h1></body></html>";
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Length: " + responseBody.length() + "\r\n");
        writer.write("Content-Type: text/html\r\n");
        writer.write("\r\n");
        writer.write(responseBody);
        writer.flush();
        clientSocket.close();
    }
}

package org.example.net;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(socketAddress);
        while (true) {
            Socket accepted = serverSocket.accept();
            System.out.println("New client connected");

            InputStream inputStream = accepted.getInputStream();
            System.out.println(inputStream.read());

            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder receivedData = new StringBuilder();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                receivedData.append(new String(buffer, 0, bytesRead));
            }
            String fullData = receivedData.toString();
            System.out.println("Data: " + fullData);

            OutputStream outputStream = accepted.getOutputStream();
            outputStream.write("Hello".getBytes());
            if (!accepted.getKeepAlive()) {
                accepted.close();
            }
        }
    }
}

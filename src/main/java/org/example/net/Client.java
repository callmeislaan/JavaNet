package org.example.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
        Socket socket = new Socket();
        socket.bind(socketAddress);
        socket.setKeepAlive(true);
        socket.connect(socketAddress, 1000);
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder receivedData = new StringBuilder();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            receivedData.append(new String(buffer, 0, bytesRead));
        }
        String fullData = receivedData.toString();
        System.out.println("Data: " + fullData);
    }
}
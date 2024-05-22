package org.example.simple;

import java.io.*;
import java.net.*;

/**
 * @author phuocht3
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        InetAddress localhost = InetAddress.getByName("localhost");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(localhost, 8080);
        socket.connect(inetSocketAddress, 1000);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        output.println("Hello from client");

        String response = input.readLine();
        System.out.println("Server response: " + response);


    }
}

package org.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

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

        output.println("GET / HTTP/1.1");
        output.println("Host: localhost");
        output.println("Connection: close");
        output.println("Hello from client");
        output.println();

        String response = input.readLine();
        System.out.println("Server response: " + response);


    }
}

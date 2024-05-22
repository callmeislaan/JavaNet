package org.example.square;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            InetAddress localhost = InetAddress.getByName("localhost");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(localhost, 6868);
            socket.connect(inetSocketAddress);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String userInputStr;
            do {
                System.out.print("Enter a number (0 to exit): ");
                userInputStr = userInput.readLine();
                output.println(userInputStr);

                if (!userInputStr.equals("0")) {
                    String response = input.readLine();
                    System.out.println("Server response: " + response);
                }
            } while (!userInputStr.equals("0"));
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}
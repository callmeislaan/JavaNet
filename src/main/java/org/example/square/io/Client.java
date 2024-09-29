package org.example.square.io;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket();
            InetAddress localhost = InetAddress.getByName("localhost");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(localhost, 8080);

            socket.connect(inetSocketAddress);
            System.out.println("Client info: " + socket);
            // Client info: Socket[addr=localhost/127.0.0.1,port=8080,localport=57884]

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            PrintWriter writer = new PrintWriter(out);

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) { // cho phép gọi nhiều món
                System.out.print("Enter a number (0 to exit): ");
                String userInputStr = consoleReader.readLine();
                if (userInputStr.equals("0")) {
                    System.out.println("Close connection");
                    break;
                }
                writer.println(userInputStr);
                writer.flush();

                String response = reader.readLine();
                System.out.println("Server response: " + response);
            }


        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
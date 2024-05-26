package org.example.performance;

import java.io.*;
import java.net.*;

public class BlockingKeepAlive {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            try {
                System.out.println("Accepted: " + socket);
                processClient(socket);
            } catch (IOException e) {
                System.err.println("Error processing client request: " + e.getMessage());
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.err.println("Error closing socket: " + ex.getMessage());
                }
            }

        }
    }

    private static void processClient(Socket socket) throws IOException {
        socket.setSoTimeout(5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            String line;
            boolean keepAlive;
            do {
                keepAlive = false;
                StringBuilder request = new StringBuilder();
                while (true) {
                    line = in.readLine();
                    if (line == null || line.isEmpty()) break;
                    request.append(line + "\n");
                    if (line.toLowerCase().contains("connection: keep-alive")) {
                        keepAlive = true;
                    }
                }
                if (request.length() == 0) break; // End processing if no data received
                // Response logic here
                String responseBody = "<html><body><h1>Hello, World!</h1></body></html>";
                out.write("HTTP/1.1 200 OK\r\n");
                out.write("Content-Length: " + responseBody.length() + "\r\n");
                out.write("Content-Type: text/html\r\n");
                String connection = "Connection: " + (keepAlive ? "keep-alive" : "close") + "\r\n";
                out.write(connection);
                out.write("\r\n");
                out.write(responseBody);
                out.flush();

                // If keep-alive, read the next request; otherwise, exit loop
            } while (keepAlive && !socket.isClosed());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
            if (!socket.isClosed()) {
                socket.close();
            }
        }
    }

}

package org.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author phuocht3
 */
public class Server {
    public static void main(String[] args) throws IOException {
        Executor executor = Executors.newFixedThreadPool(8);
        ServerSocket serverSocket = new ServerSocket();
        InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
        serverSocket.bind(socketAddress);
        System.out.println("Server is listening on port 6868");

        while (true) {
            Socket socket = serverSocket.accept();

            executor.execute(() -> {
                System.out.println("Connected to client: " + socket.toString());

                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                PrintWriter output = null;
                try {
                    output = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String htmlResponse = "<html><body><h1>Hello from server</h1></body></html>";
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + htmlResponse.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        htmlResponse;
                output.print(response);
                output.flush();
            });
//            System.out.println("Connected to client: " + socket.toString());
//
//            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

//            StringBuilder requestBuilder = new StringBuilder();
//            String line;
//            int contentLength = 0;
//            boolean isChunked = false;
//            boolean isPost = false;
//            String first = input.readLine();
//            while ((line = input.readLine()) != null && !line.isEmpty()) {
//                requestBuilder.append(line).append("\r\n");
//                if (line.startsWith("Content-Length:")) {
//                    contentLength = Integer.parseInt(line.split(" ")[1]);
//                }
//                if (line.startsWith("Transfer-Encoding:") && line.contains("chunked")) {
//                    isChunked = true;
//                }
//            }
//
//            StringBuilder bodyBuilder = new StringBuilder();
//            if (isChunked) {
//                while (true) {
//                    line = input.readLine();
//                    int chunkSize = Integer.parseInt(line, 16); // chunk size in hex
//                    if (chunkSize == 0) {
//                        break;
//                    }
//                    char[] chunk = new char[chunkSize];
//                    input.read(chunk, 0, chunkSize);
//                    bodyBuilder.append(chunk);
//                    input.readLine(); // read the trailing \r\n
//                }
//            } else if (contentLength > 0) {
//                char[] body = new char[contentLength];
//                input.read(body, 0, contentLength);
//                bodyBuilder.append(body);
//            }

//            String request = requestBuilder.toString();
//            String body = bodyBuilder.toString();
//            System.out.println("Request: ");
//            System.out.println(request);
//            System.out.println("Body: ");
//            System.out.println(body);

//            String htmlResponse = "<html><body><h1>Hello from server</h1></body></html>";
//            String response = "HTTP/1.1 200 OK\r\n" +
//                    "Content-Type: text/html\r\n" +
//                    "Content-Length: " + htmlResponse.length() + "\r\n" +
//                    "Connection: close\r\n" +
//                    "\r\n" +
//                    htmlResponse;
//            output.print(response);
//            output.flush();
        }
    }
}

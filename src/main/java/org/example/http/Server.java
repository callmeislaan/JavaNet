package org.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
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



    private static HttpRequest parseHttp(String request) {
        HttpRequest httpRequest = new HttpRequest();
        String[] requestLines = request.split("\r\n");

        // parse header
        // dòng đầu tiên là request-line
        String[] requestLine = requestLines[0].split(" ");
        httpRequest.method = requestLine[0];
        httpRequest.path = requestLine[1];
        httpRequest.version = requestLine[2];

        // các dòng tiếp theo là request header
        int i = 1;
        while (i < requestLines.length) {
            String line = requestLines[i++];
            if (line.isEmpty()) {
                break;
            }
            String[] header = line.split(": "); // KEY: VALUE
            httpRequest.headers.put(header[0], header[1]);
        }

        // parse body
        if (httpRequest.headers.containsKey("Transfer-Encoding") &&
                httpRequest.headers.get("Transfer-Encoding").equalsIgnoreCase("chunked")) {
            StringBuilder bodyBuilder = new StringBuilder();
            // trường hợp chunked, ta parse từng chunk một
            while (i < requestLines.length) {
                String line = requestLines[i++];
                int chunkSize = Integer.parseInt(line, 16); // parse int từ cơ số 16 (hex)
                if (chunkSize == 0) {
                    break;
                }
                while (chunkSize > 0 && i < requestLines.length) {
                    line = requestLines[i++];
                    int len = Math.min(chunkSize, line.length());
                    bodyBuilder.append(line, 0, len);
                    chunkSize -= len;
                }
                i++;
            }
            httpRequest.body = bodyBuilder.toString();
        } else if (httpRequest.headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(httpRequest.headers.get("Content-Length"));
            StringBuilder bodyBuilder = new StringBuilder();
            while (bodyBuilder.length() < contentLength && i < requestLines.length) {
                bodyBuilder.append(requestLines[i]);
                bodyBuilder.append("\r\n");
                i++;
            }
            httpRequest.body = bodyBuilder.toString().trim();
        }
        return httpRequest;
    }

    private static final class HttpRequest {
        public String method;
        public String path;
        public String version;
        public Map<String, String> headers = new HashMap<>();
        public String body;
    }
}

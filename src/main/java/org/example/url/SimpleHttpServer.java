package org.example.url;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;

public class SimpleHttpServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            System.out.println("HTTP Server is listening on port 80");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Connected to client");

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

                    // Read the request line
                    String requestLine = in.readLine();
                    if (requestLine != null) {
                        System.out.println("Request: " + requestLine);

                        // Tokenize the request
                        StringTokenizer tokenizer = new StringTokenizer(requestLine);
                        String method = tokenizer.nextToken();
                        String url = tokenizer.nextToken();

                        // Parse the URL
                        URL parsedUrl = new URL("http://localhost:80" + url);
                        System.out.println("Parsed URL: " + parsedUrl);

                        // Generate response
                        String response = generateHttpResponse(parsedUrl);

                        // Send response
                        out.print(response);
                        out.flush();
                    }

                    in.close();
                    out.close();
                } catch (IOException e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    private static String generateHttpResponse(URL url) {
        StringBuilder responseBuilder = new StringBuilder();

        responseBuilder.append("HTTP/1.1 200 OK\r\n");
        responseBuilder.append("Content-Type: text/html\r\n");
        responseBuilder.append("\r\n");
        responseBuilder.append("<html><body>");
        responseBuilder.append("<h1>Simple HTTP Server</h1>");
        responseBuilder.append("<p>URL: ").append(url).append("</p>");
        responseBuilder.append("<p>Protocol: ").append(url.getProtocol()).append("</p>");
        responseBuilder.append("<p>Host: ").append(url.getHost()).append("</p>");
        responseBuilder.append("<p>Port: ").append(url.getPort()).append("</p>");
        responseBuilder.append("<p>Path: ").append(url.getPath()).append("</p>");
        responseBuilder.append("<p>Query: ").append(url.getQuery()).append("</p>");
        responseBuilder.append("</body></html>");

        return responseBuilder.toString();
    }
}
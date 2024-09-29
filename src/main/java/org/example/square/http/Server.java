package org.example.square.http;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Thread: " + Thread.currentThread().getName());
        InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(socketAddress);
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server is running on port 8080");
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    handleAccept(key, selector);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
                keyIterator.remove();
            }

        }
    }

    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        System.out.println("Thread: " + Thread.currentThread().getName());
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("accepted " + serverSocketChannel);
    }


    private static void handleRead(SelectionKey key) throws IOException {
        System.out.println("Thread: " + Thread.currentThread().getName());
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        int read;
        while ((read = clientSocketChannel.read(byteBuffer)) != -1) {
            if (read == 0) {
                break;
            }
            byteBuffer.flip();
            String clientInput = new String(byteBuffer.array(), 0, byteBuffer.limit()).trim();
            System.out.println(clientInput);
            String response;
            HttpRequest httpRequest = parseHttp(clientInput);
            URI uri = URI.create(httpRequest.uri);
            if ("GET".equals(httpRequest.method) && "/square".equals(uri.getPath())) {
                // uri.query: number=2
                int number = Integer.parseInt(uri.getQuery().split("=")[1]);
                int square = number * number;
                String responseBody = String.format("<html>\r\n" +
                        "<body>\r\n" +
                        "<h1>Square application</h1>\r\n" +
                        "<p>%d square is %d</p>\r\n" +
                        "</body>\r\n" +
                        "</html>\r\n", number, square);
                response = String.format("HTTP/1.1 200 OK\r\n" +
                        "Content-Length: %d\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Connection: Closed\r\n" +
                        "\r\n" +
                        "%s", responseBody.getBytes().length, responseBody);
            } else {
                String responseBody = "<html>\r\n" +
                        "<body>\r\n" +
                        "<h1>Square application</h1>\r\n" +
                        "<p>Bad Request</p>\r\n" +
                        "</body>\r\n" +
                        "</html>\r\n";
                response = String.format("HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Length: %d\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Connection: Closed\r\n" +
                        "\r\n" +
                        "%s", responseBody.getBytes().length, responseBody);
            }


            byteBuffer.clear();
            byteBuffer.put(response.getBytes());

            byteBuffer.flip();
            clientSocketChannel.write(byteBuffer);

            byteBuffer.clear();
        }
        clientSocketChannel.close();
    }

    private static HttpRequest parseHttp(String request) {
        HttpRequest httpRequest = new HttpRequest();
        String[] requestLines = request.split("\r\n");

        // parse header
        // dòng đầu tiên là request-line
        String[] requestLine = requestLines[0].split(" ");
        httpRequest.method = requestLine[0];
        httpRequest.uri = requestLine[1];
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
        return httpRequest;
    }


    private static final class HttpRequest {
        public String method;
        public String uri;
        public String version;
        public Map<String, String> headers = new HashMap<>();
        public String body;
    }
}
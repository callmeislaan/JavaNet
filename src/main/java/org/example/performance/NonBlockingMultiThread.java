package org.example.performance;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NonBlockingMultiThread {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(8);

    public static void main(String[] args) throws IOException {
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

                if (!key.isValid()) {
                    keyIterator.remove();
                    continue;
                }

                if (key.isAcceptable()) {
                    handleAccept(key, selector);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
                keyIterator.remove();
            }

        }
    }

    private static void handleAccept(SelectionKey key, Selector selector) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("accepted " + serverSocketChannel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    private static void handleRead(SelectionKey key) {
        executorService.submit(() -> {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                int bytesRead;
                synchronized (clientChannel) {
                    if (!clientChannel.isOpen()) {
                        return;
                    }
                    bytesRead = clientChannel.read(buffer);
                }
                if (bytesRead == -1) {
                    clientChannel.close();
                    return;
                }
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String request = new String(bytes);

                String responseBody = "<html><body><h1>Hello, World!</h1></body></html>";
                String httpResponse = "HTTP/1.1 200 OK\r\n"
                        + "Content-Length: " + responseBody.length() + "\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + responseBody;

                ByteBuffer responseBuffer = ByteBuffer.wrap(httpResponse.getBytes());
                synchronized (clientChannel) {
                    if (clientChannel.isOpen()) {
                        clientChannel.write(responseBuffer);
                        clientChannel.close();
                    }
                }
                key.cancel();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    clientChannel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

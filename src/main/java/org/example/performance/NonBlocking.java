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

public class NonBlocking {
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
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("accepted " + serverSocketChannel);
    }


    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        StringBuilder stringBuilderRequest = new StringBuilder();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(buffer) != -1) {
            buffer.flip();
            if (!buffer.hasRemaining()) {
                break;
            }
            while (buffer.hasRemaining()) {
                stringBuilderRequest.append((char) buffer.get());
            }
            buffer.clear();
        }

        StringBuilder stringBuilder = new StringBuilder();
        String responseBody = "<html><body><h1>Hello, World!</h1></body></html>";
        stringBuilder.append("HTTP/1.1 200 OK\r\n");
        stringBuilder.append("Content-Length: ").append(responseBody.length()).append("\r\n");
        stringBuilder.append("Content-Type: text/html\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append(responseBody);

        ByteBuffer responseBuffer = ByteBuffer.wrap(stringBuilder.toString().getBytes());
        while (responseBuffer.hasRemaining()) {
            socketChannel.write(responseBuffer);
        }
        socketChannel.close();
    }

}

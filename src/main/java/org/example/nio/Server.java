package org.example.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
            serverSocketChannel.bind(socketAddress);
            serverSocketChannel.configureBlocking(false);
            while (true) {
                SocketChannel clientSocketChannel = serverSocketChannel.accept();
                if (clientSocketChannel == null) {
                    continue;
                }
                clientSocketChannel.configureBlocking(false);

                System.out.println("Connected to client: " + clientSocketChannel);

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                while (clientSocketChannel.read(byteBuffer) != -1) { // Cho phép gọi nhiều món
                    byteBuffer.flip();
                    String clientInput = new String(byteBuffer.array(), 0, byteBuffer.limit()).trim();

                    int number = Integer.parseInt(clientInput);
                    System.out.println("client input: " + clientInput);
                    if (number == 0) {
                        System.out.println("Client requested to close connection.");
                        break;
                    }

                    int square = number * number;
                    String response = String.format("%d square is %d\r\n", number, square);

                    byteBuffer.clear();
                    byteBuffer.put(response.getBytes());

                    byteBuffer.flip();
                    clientSocketChannel.write(byteBuffer);

                    byteBuffer.clear();

                }
            }
        } finally {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
        }
    }
}
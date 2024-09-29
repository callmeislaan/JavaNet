package org.example.square.nio;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = null;
        try {
            System.out.println("Thread: " + Thread.currentThread().getName());
            serverSocketChannel = ServerSocketChannel.open();
            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, 8080);
            serverSocketChannel.bind(socketAddress);
            while (true) {
                SocketChannel clientSocketChannel = serverSocketChannel.accept();

                new Thread(() -> {
                    try {
                        System.out.println("Thread: " + Thread.currentThread().getName());
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } finally {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
        }
    }
}
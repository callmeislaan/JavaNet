package org.example.square.nio;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            InetAddress localhost = InetAddress.getByName("localhost");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(localhost, 8080);

            socketChannel.connect(inetSocketAddress);
            System.out.println("Client info: " + socketChannel);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Enter a number (0 to exit): ");
                String userInputStr = consoleReader.readLine();
                if (userInputStr.equals("0")) {
                    System.out.println("Close connection");
                    break;
                }

                byteBuffer.put(userInputStr.getBytes());

                byteBuffer.flip();
                socketChannel.write(byteBuffer);

                byteBuffer.clear();
                socketChannel.read(byteBuffer);

                byteBuffer.flip();
                System.out.println("Server response: " + new String(byteBuffer.array(), 0, byteBuffer.limit()).trim());

                byteBuffer.clear();
            }


        } finally {
            if (socketChannel != null) {
                socketChannel.close();
            }
        }
    }
}
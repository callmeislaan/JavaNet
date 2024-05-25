package org.example.udp;

import java.io.IOException;
import java.net.*;

/**
 * @author phuocht3
 */
public class Client {
    public static void main(String[] args) {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            InetAddress inetAddress = InetAddress.getByName("localhost");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, 4321);

            byte[] sendBuffer = "Hello from client 123".getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, socketAddress);

            datagramSocket.send(datagramPacket);

            byte[] receiveBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            datagramSocket.receive(responsePacket);

            String receivedData = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Received: " + receivedData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

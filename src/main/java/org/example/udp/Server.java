package org.example.udp;

import java.io.IOException;
import java.net.*;

/**
 * @author phuocht3
 */
public class Server {

    public static void main(String[] args) {
        try {
            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, 4321);
            try (DatagramSocket datagramSocket = new DatagramSocket(socketAddress)) {
                System.out.println("server is running...");
                while (true) {
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    datagramSocket.receive(datagramPacket);

                    String receivedData = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    System.out.println("Received: " + receivedData);

                    byte[] responseBuffer = ("Acknowledged " + receivedData).getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, datagramPacket.getSocketAddress());
                    datagramSocket.send(responsePacket);

                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

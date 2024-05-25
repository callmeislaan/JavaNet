package org.example.binary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author phuocht3
 */
public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 12345);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {

            output.write(new byte[]{0x01});  // Sending a request code to server

            byte statusCode = input.readByte();  // Read status code from server
            if (statusCode == 0x00) {  // Check if response is successful
                int length = input.readInt();  // Read length of the incoming data
                byte[] data = new byte[length];
                input.readFully(data);  // Read the data from the server
                System.out.println("Server says: " + new String(data));
            }
        }
    }
}

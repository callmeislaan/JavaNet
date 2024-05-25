package org.example.binary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author phuocht3
 */
public class Server {
    /**
     * Request Message: Instead of a method (GET, POST), the client sends a fixed-length binary code representing the action. For example, 01 for request data.
     * Response Message: The server sends back a status code followed by data. For example, 00 for success followed by the message.
     * */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);  // Listen on port 12345
        System.out.println("Server started");

        try (Socket clientSocket = serverSocket.accept();
             DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

            byte[] requestCode = new byte[2];
            input.readFully(requestCode);  // Read request code from client

            if (requestCode[0] == 0x01) {  // Simple check for request type
                System.out.println("Received request for data");

                byte[] responseData = "hello from server".getBytes();
                output.writeByte(0x00);  // Status code for success
                output.writeInt(responseData.length);  // Length of the response data
                output.write(responseData);  // Actual response data
            }
        }
        System.out.println("Server stopped");
    }
}

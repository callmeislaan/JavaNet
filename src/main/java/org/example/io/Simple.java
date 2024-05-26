package org.example.io;

import java.io.*;
import java.util.Arrays;

public class Simple {
    public static void main(String[] args) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("C:\\Users\\truon\\Downloads\\abc.txt"))) {
            byte[] bytes = "abeơ".getBytes();
            System.out.println(Arrays.toString(bytes));
            outputStream.write(bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

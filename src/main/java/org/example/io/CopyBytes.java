package org.example.io;

import java.io.*;
import java.nio.charset.Charset;

public class CopyBytes {
    public static void main(String[] args) throws IOException {

        System.out.println("Default Charset: " + Charset.defaultCharset());
        InputStream in = null;

        try {
            in = new FileInputStream("D:\\Java\\JavaNet\\src\\main\\java\\org\\example\\io\\input.txt");
            int c;

            while ((c = in.read()) != -1) { // c == -1 có nghĩa là hổng còn gì để đọc nha
                String binaryString = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
                System.out.println("Byte: " + binaryString + " - UInt8: " + c + " - Char: " + (char) c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
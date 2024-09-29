package org.example.io;

import java.io.*;
import java.nio.charset.Charset;

public class CopyCharacters {
    public static void main(String[] args) throws IOException {

        System.out.println("Default Charset: " + Charset.defaultCharset());
        Reader in = null;

        try {
            in = new FileReader("D:\\Java\\JavaNet\\src\\main\\java\\org\\example\\io\\input.txt");

            int c;

            while ((c = in.read()) != -1) { // byteRead == -1 có nghĩa là hổng còn gì để đọc nha
                String binaryString = String.format("%16s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
                System.out.println("Byte: " + binaryString + " - Int: " + c + " - Char: " + (char) c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
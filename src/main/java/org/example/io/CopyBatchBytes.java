package org.example.io;

import java.io.*;

public class CopyBatchBytes {
    public static void main(String[] args) throws IOException {

        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream("D:\\Java\\JavaNet\\src\\main\\java\\org\\example\\io\\input.txt");
            out = new FileOutputStream("D:\\Java\\JavaNet\\src\\main\\java\\org\\example\\io\\output.txt");

            int capacity = 1024;
            byte[] buffer = new byte[capacity];
            int byteRead;

            while ((byteRead = in.read(buffer)) != -1) { // byteRead == -1 có nghĩa là hổng còn gì để đọc nha
                out.write(buffer, 0, byteRead);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
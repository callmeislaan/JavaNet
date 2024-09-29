package org.example.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ReadCharacter {
    public static void main(String[] args) throws IOException {


        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
// ghi
        String writeData = new String("This is input data".getBytes(), StandardCharsets.UTF_8);
        byteBuffer.put(writeData .getBytes());

        byteBuffer.flip();

// đọc
        String readData = new String(byteBuffer.array(), StandardCharsets.UTF_8).trim();
        System.out.println(readData );
    }
}

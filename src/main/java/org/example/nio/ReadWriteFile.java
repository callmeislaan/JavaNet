package org.example.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ReadWriteFile {
    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\truon\\Downloads\\abc.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2);
            while (fileChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }
                byteBuffer.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

package org.example.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CopyBytes {
    public static void main(String[] args) throws IOException {
        Path source1 = Paths.get("src/main/java/org/example/nio/source1.txt");
        Path source2 = Paths.get("src/main/java/org/example/nio/source2.txt");
        FileChannel channel1 = null;
        FileChannel channel2 = null;
        try {
            channel1 = FileChannel.open(source1, StandardOpenOption.READ);
            channel2 = FileChannel.open(source2, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            ByteBuffer buffer = ByteBuffer.allocate(3);
            while (channel1.read(buffer) > 0) {
                buffer.flip();
                channel2.write(buffer);
                buffer.flip();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel1 != null) {
                channel1.close();
            }
            if (channel2 != null) {
                channel2.close();
            }
        }
    }
}

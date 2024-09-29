package org.example.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CopyBytes2Buffers {
    public static void main(String[] args) throws IOException {
        Path source1 = Paths.get("src/main/java/org/example/nio/source1.txt");
        Path source2 = Paths.get("src/main/java/org/example/nio/source2.txt");
        FileChannel channel1 = null;
        FileChannel channel2 = null;
        try {
            channel1 = FileChannel.open(source1, StandardOpenOption.READ);
            channel2 = FileChannel.open(source2, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            ByteBuffer buffer1 = ByteBuffer.allocate(3);
            ByteBuffer buffer2 = ByteBuffer.allocate(6);
            while (channel1.read(buffer1) > 0) {
                buffer1.flip();
                buffer2.put(buffer1);
                if (!buffer2.hasRemaining()) { // nếu thuyền 2 không còn trống = thuyền 2 đã đầy
                    buffer2.flip();
                    channel2.write(buffer2);
                    buffer2.flip();
                }
                buffer1.flip();
            }
            // vận chuyển dữ liệu còn lại nếu có
            // ví dụ thuyền 2 chưa đầy nhưng nguồn 1 đã hết hàng
            buffer2.flip();
            channel2.write(buffer2);
            buffer2.flip();

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

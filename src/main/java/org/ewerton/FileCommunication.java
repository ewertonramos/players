package org.ewerton;



import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;

/**
 * The communication file where the messages will be read and written.
 */
public class FileCommunication implements Communication {

    private final String filePath;
    private RandomAccessFile raf;
    private FileChannel channel;

    public FileCommunication(String filePath) {
        this.filePath = filePath;
    }

    public void init() throws IOException {
        File file = new File(filePath);
        file.delete();
        file.createNewFile();
        raf = new RandomAccessFile(file, "rwd");
        channel = raf.getChannel();
    }

    public void shutdown() throws IOException {
        File file = new File(filePath);
        file.deleteOnExit();
        raf.close();
    }

    @Override
    public void sendMessage(String message) throws IOException {
        FileLock lock = channel.tryLock(0, 1000, false);
        channel.truncate(0);
        channel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
        lock.release();
    }

    @Override
    public String receiveMessage() throws IOException {
        FileLock lock = channel.tryLock(0, 1000, true);
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
        channel.read(byteBuffer, 0);
        lock.release();
        return new String(byteBuffer.array(), StandardCharsets.UTF_8);
    }
}

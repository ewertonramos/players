package org.ewerton;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * File that control the turn for sending messages.
 */
public class FileTurnHolder implements TurnHolder {

    private final String filePath;
    private final int[] playersId;
    private RandomAccessFile raf;
    private FileChannel channel;
    private FileLock lock;
    private Map<Integer, Integer> turnOrder;

    public FileTurnHolder(String filePath, int[] playersId) {
        this.filePath = filePath;
        this.playersId = playersId;
    }

    public void init() throws IOException {
        File file = new File(filePath);
        file.delete();
        file.createNewFile();
        raf = new RandomAccessFile(file, "rwd");
        channel = raf.getChannel();
        turnOrder = new HashMap<>();
        for (int i = 0; i < playersId.length; i++) {
            int nextTurn = i + 1 < playersId.length ? playersId[i + 1] : playersId[0];
            turnOrder.put(playersId[i], nextTurn);
        }
    }

    public void shutDown() throws IOException {
        File file = new File(filePath);
        file.delete();
        raf.close();
    }

    @Override
    public int getCurrentTurn() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
        channel.read(byteBuffer, 0);
        String playerIdString = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        return !"".equals(playerIdString) ? Integer.parseInt(playerIdString) : -1;
    }

    @Override
    public void setCurrentTurn(int playerId) throws IOException {
        channel.truncate(0);
        ByteBuffer byteBuffer = ByteBuffer.wrap(Integer.valueOf(playerId).toString().getBytes());
        channel.write(byteBuffer);
    }

    @Override
    public void changeTurn() throws IOException {
        Integer nextTurn = turnOrder.get(getCurrentTurn());
        setCurrentTurn(nextTurn);
    }

    @Override
    public boolean lockTurn() throws IOException {
        try {
            lock = channel.lock();
        } catch (OverlappingFileLockException e) {
            return false;
        }
        return lock != null;
    }

    @Override
    public void releaseTurn() throws IOException {
        lock.release();
    }

}

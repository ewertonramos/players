package org.ewerton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.ewerton.ArgumentReader.COMMUNICATION_FILE_NAME;
import static org.ewerton.ArgumentReader.TURN_FILE_NAME;

/**
 * Main class that execute the program.
 */
public class MainApp {

    public static void main(String[] args) throws Exception {

        ArgumentReader argumentReader = new ArgumentReader();
        argumentReader.processArgs(args);

        int[] ids = argumentReader.getIds();
        int[] totalIds = argumentReader.getTotalIds();
        boolean initiator = argumentReader.isInitiator();
        String message = argumentReader.getMessage();
        int delay = argumentReader.getDelay();


        ExecutorService executorService = Executors.newFixedThreadPool(ids.length);

        FileCommunication communication = new FileCommunication(COMMUNICATION_FILE_NAME);
        FileTurnHolder turn = new FileTurnHolder(TURN_FILE_NAME, totalIds);

        communication.init();
        turn.init();

        for (int i = 0; i < ids.length; i++) {
            Player.PlayerOptions options = new Player.PlayerOptions(initiator && i == 0, ids[i], message, delay);
            executorService.execute(new Player(options, communication, turn));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.exit(1);
        }

        communication.shutdown();
        turn.shutDown();

    }
}

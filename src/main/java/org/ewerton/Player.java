package org.ewerton;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Send and receive messages to other player.
 */
public class Player implements Runnable {


    private final PlayerOptions options;
    private final Communication communication;
    private final TurnHolder turn;

    private int messageCount;

    public Player(PlayerOptions options, Communication communication, TurnHolder turn) {
        this.options = options;
        this.communication = communication;
        this.turn = turn;
        messageCount = 0;
    }


    @Override
    public void run() {
        try {
            execute();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error accessing file.");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void execute() throws IOException, InterruptedException {
        Thread.currentThread().setName("Player-" + options.id);
        boolean started = false;
        while (messageCount < 10 && !Thread.currentThread().isInterrupted()) {
            TimeUnit.SECONDS.sleep(options.delay);
            if (options.initiator && !started) {
                if (turn.lockTurn()) {
                    turn.setCurrentTurn(options.id);
                    communication.sendMessage(options.message);
                    turn.changeTurn();
                    turn.releaseTurn();
                    started = true;
                }
            }

            if (turn.lockTurn()) {
                if (turn.getCurrentTurn() == options.id) {

                    String message = communication.receiveMessage();
                    System.out.println(Thread.currentThread().getName()+ " " + message);
                    communication.sendMessage(options.message + ++messageCount);

                    turn.changeTurn();

                }
                turn.releaseTurn();
            }
        }
    }

    public static class PlayerOptions {

        private final int id;
        private final String message;
        private final boolean initiator;
        private final int delay;

        public PlayerOptions(boolean initiator, int id, String message, int delay) {
            this.id = id;
            this.message = message;
            this.initiator = initiator;
            this.delay = delay;
        }
    }
}

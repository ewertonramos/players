package org.ewerton;

/**
 * Process the arguments received by the main App.
 */
public class ArgumentReader {
    public static final String COMMUNICATION_FILE_NAME = "0-communication.txt";
    public static final String TURN_FILE_NAME = "0-turn.txt";

    private int[] ids;
    private int[] totalIds;
    private boolean initiator;
    private int delay;

    public void processArgs(String[] args) {
        try {
            initiator = false;
            for (int i = 0; i < args.length; i++) {
                switch (i) {
                    case 0:
                        totalIds = convertCommaSeparatedString(args[i]);
                        break;
                    case 1:
                        initiator = Boolean.parseBoolean(args[i]);
                        break;
                    case 2:
                        ids = convertCommaSeparatedString(args[i]);
                        break;
                    case 3:
                        delay = Integer.parseInt(args[i]);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Fall back to default options.");
            setDefault();
        }
    }

    private int[] convertCommaSeparatedString(String arg) {
        String[] split = arg.split(",");
        int[] ids = new int[split.length];
        for (int j = 0; j < split.length; j++) {
            ids[j] = Integer.parseInt(split[j]);
        }
        return ids;
    }

    private void setDefault() {
        ids = new int[]{1, 2};
        totalIds = ids;
        initiator = true;
        delay = 0;
    }

    public int[] getIds() {
        return ids;
    }

    public int[] getTotalIds() {
        return totalIds;
    }

    public boolean isInitiator() {
        return initiator;
    }

    public String getMessage() {
        return "hello ";
    }

    public int getDelay() {
        return delay;
    }
}

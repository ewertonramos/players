package org.ewerton;

import java.io.IOException;

public interface Communication {

    void sendMessage(String message) throws IOException;

    String receiveMessage() throws IOException;
}

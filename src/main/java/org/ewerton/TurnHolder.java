package org.ewerton;

import java.io.IOException;

public interface TurnHolder {

    int getCurrentTurn() throws IOException;

    void setCurrentTurn(int playerId) throws IOException;

    void changeTurn() throws IOException;

    boolean lockTurn() throws IOException;

    void releaseTurn() throws IOException;
}

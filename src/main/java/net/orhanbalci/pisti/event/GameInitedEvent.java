package net.orhanbalci.pisti.event;

import java.util.UUID;

public class GameInitedEvent extends GameEvent {

    public GameInitedEvent(UUID gameId) {
        super(gameId);
    }

    public UUID getGameId() {
        return gameId;
    }
}
package net.orhanbalci.pisti.event;

import java.util.UUID;

public abstract class GameEvent {
    private final UUID eventId = UUID.randomUUID();
    protected UUID gameId;

    public GameEvent(UUID gameId){
        this.gameId = gameId;
    }

    public UUID getEventId() {
        return eventId;
    }
    
}
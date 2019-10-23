package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;

public class PlayerSeatedEvent extends GameEvent {

    private List<UUID> players;

    public PlayerSeatedEvent(UUID gameId, List<UUID> players) {
        super(gameId);
        this.players = players;
    }

    public UUID getGameId() {
        return gameId;
    }

    public List<UUID> getPlayers() {
        return players;
    }

}
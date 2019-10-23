package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import net.orhanbalci.pisti.Card;

public class TurnChangedEvent extends GameEvent {
    private UUID nextPlayerId;
    private List<Card> centerPile;

    public TurnChangedEvent(UUID gameId, UUID nextPlayerId, List<Card> centerPile) {
        super(gameId);
        this.nextPlayerId = nextPlayerId;
        this.centerPile = centerPile;
    }

    public UUID getNextPlayerId() {
        return nextPlayerId;
    }

    public List<Card> getCenterPile() {
        return centerPile;
    }

}
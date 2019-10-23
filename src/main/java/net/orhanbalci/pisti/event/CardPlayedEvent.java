package net.orhanbalci.pisti.event;

import java.util.UUID;

import net.orhanbalci.pisti.Card;

public class CardPlayedEvent extends GameEvent {
    private UUID playerId;
    private Card playedCard;

    public CardPlayedEvent(UUID gameId, UUID playerId, Card playedCard) {
        super(gameId);
        this.playerId = playerId;
        this.playedCard = playedCard;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Card getCard() {
        return playedCard;
    }

}
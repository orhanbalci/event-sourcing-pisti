package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import net.orhanbalci.pisti.Card;
import net.orhanbalci.pisti.PointType;

public class PointScoredEvent extends GameEvent {
    private UUID playerId;
    private List<Card> cardsCleared;
    private List<PointType> points;

    public PointScoredEvent(UUID gameId, UUID playerId, List<Card> cardsCleared, List<PointType> points) {
        super(gameId);
        this.playerId = playerId;
        this.cardsCleared = cardsCleared;
        this.points = points;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public List<Card> getCardsCleared() {
        return cardsCleared;
    }

    public List<PointType> getPoints() {
        return points;
    }

}
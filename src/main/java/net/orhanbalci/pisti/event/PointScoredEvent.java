package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import io.vavr.control.Either;
import net.orhanbalci.pisti.Card;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.PointType;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

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

    @Override
    public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {

        return String.format("PointScoredEvent(%s %s (%s) (%s))", getGameId(), getPlayerId(),
                getCardsCleared().foldLeft("", (xs, x) -> String.format("%s,%s", xs, x)),
                getPoints().foldLeft("", (xs, x) -> String.format("%s,%s", xs, x)));
    }

}
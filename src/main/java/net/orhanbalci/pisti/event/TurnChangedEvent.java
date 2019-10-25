package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import io.vavr.control.Either;
import net.orhanbalci.pisti.Card;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

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

    @Override
    public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
        return v.visit(this);
    }


    @Override
    public String toString() {

        return String.format("TurnChangedEvent(%s %s (%s))", getGameId(), getNextPlayerId(),
                getCenterPile().foldLeft("", (xs, x) -> String.format("%s,%s", xs, x)));
               
    }

}
package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.control.Either;
import net.orhanbalci.pisti.Card;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

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

    @Override
    public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
        return v.visit(this);
    }

    @Override
    public String toString(){
        return String.format("CardPlayedEvent(%s %s %s)", getGameId(), getPlayerId(), getCard());
    }

}
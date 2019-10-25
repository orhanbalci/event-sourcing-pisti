package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import io.vavr.control.Either;
import net.orhanbalci.pisti.Card;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public class CardsWonEvent extends GameEvent {
    private UUID playerIdWon;
    private List<Card> centerPile;

    public CardsWonEvent(UUID gameId,UUID playerIdWon, List<Card> centerPile){
        super(gameId);
        this.playerIdWon = playerIdWon;
        this.centerPile = centerPile;
    }

    public UUID getPlayerIdWon() {
        return playerIdWon;
    }

    public List<Card> getCenterPile(){
        return centerPile;
    }

    @Override
    public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
        return v.visit(this);
    }

    @Override
    public String toString(){
        return String.format("CardsWonEvent(%s %s %s)", getGameId(), getPlayerIdWon(), getCenterPile());
    }

}
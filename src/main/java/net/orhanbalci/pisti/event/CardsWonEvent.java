package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import net.orhanbalci.pisti.Card;

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

}
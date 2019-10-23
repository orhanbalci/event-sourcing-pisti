package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import io.vavr.control.Option;
import net.orhanbalci.pisti.Card;

public class CardsDealedEvent extends GameEvent {
    private List<Card> cards;
    private Option<UUID> playerId;

    public CardsDealedEvent(UUID gameId ,Option<UUID> playerId, List<Card> cards) {
        super(gameId);
        this.playerId = playerId;
        this.cards = cards;
    }

    public Option<UUID> getPlayer() {
        return playerId;
    }

    public List<Card> getCards() {
        return cards;
    }

}
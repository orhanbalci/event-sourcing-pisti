package net.orhanbalci.pisti.command;

import java.util.UUID;

import net.orhanbalci.pisti.Card;

public class PlayCardCommand extends Command {
    private UUID fromPlayer;
    private Card card;

    public PlayCardCommand(UUID gameId, UUID playerId, Card card) {
        super(gameId);
        this.fromPlayer = playerId;
        this.card = card;
    }

    public UUID getPlayerId() {
        return fromPlayer;
    }

    public Card getCard() {
        return card;
    }

}
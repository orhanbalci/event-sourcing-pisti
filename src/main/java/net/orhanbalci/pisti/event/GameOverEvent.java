package net.orhanbalci.pisti.event;

import java.util.UUID;

public class GameOverEvent extends GameEvent {
    private UUID winnerPlayerId;

    public GameOverEvent(UUID gameId, UUID winnerPlayerId) {
        super(gameId);
        this.winnerPlayerId = winnerPlayerId;
    }

    public UUID getWinnerPlayer() {
        return winnerPlayerId;
    }
}
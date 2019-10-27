package net.orhanbalci.pisti.player;

import java.util.UUID;

import io.vavr.control.Option;
import net.orhanbalci.pisti.event.CardPlayedEvent;
import net.orhanbalci.pisti.event.CardsDealedEvent;
import net.orhanbalci.pisti.event.CardsExhaustedEvent;
import net.orhanbalci.pisti.event.CardsWonEvent;
import net.orhanbalci.pisti.event.GameInitedEvent;
import net.orhanbalci.pisti.event.GameOverEvent;
import net.orhanbalci.pisti.event.PlayerSeatedEvent;
import net.orhanbalci.pisti.event.PointScoredEvent;
import net.orhanbalci.pisti.event.TurnChangedEvent;

public abstract class PlayerAgent {
    protected UUID id;
    protected Option<UUID> gameId = Option.none();

    

    public abstract void handle(CardPlayedEvent event);
    public abstract void handle(CardsDealedEvent event);
    public abstract void handle(CardsExhaustedEvent event);
    public abstract void handle(CardsWonEvent event);
    public abstract void handle(GameInitedEvent event);
    public abstract void handle(GameOverEvent event);
    public abstract void handle(PlayerSeatedEvent event);
    public abstract void handle(PointScoredEvent event);
    public abstract void handle(TurnChangedEvent event);

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Option<UUID> getGameId() {
        return gameId;
    }

    public void setGameId(Option<UUID> gameId) {
        this.gameId = gameId;
    }

    public void restart(){
        gameId = Option.none();
    }
}
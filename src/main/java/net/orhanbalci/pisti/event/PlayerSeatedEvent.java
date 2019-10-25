package net.orhanbalci.pisti.event;

import java.util.UUID;

import io.vavr.collection.List;
import io.vavr.control.Either;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public class PlayerSeatedEvent extends GameEvent {

    private List<UUID> players;

    public PlayerSeatedEvent(UUID gameId, List<UUID> players) {
        super(gameId);
        this.players = players;
    }

    public UUID getGameId() {
        return gameId;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    @Override
    public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {

        return String.format("PlayerSeatedEvent(%s (%s) )", getGameId(),
                getPlayers().foldLeft("", (xs, x) -> String.format("%s,%s", xs, x)));
    }

}
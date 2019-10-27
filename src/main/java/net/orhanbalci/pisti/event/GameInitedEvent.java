package net.orhanbalci.pisti.event;

import io.vavr.control.Either;
import java.util.UUID;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public class GameInitedEvent extends GameEvent {

  public GameInitedEvent(UUID gameId) {
    super(gameId);
  }

  public UUID getGameId() {
    return gameId;
  }

  @Override
  public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
    return v.visit(this);
  }

  @Override
  public String toString() {
    return String.format("GameInitedEvent(%s)", getGameId());
  }
}

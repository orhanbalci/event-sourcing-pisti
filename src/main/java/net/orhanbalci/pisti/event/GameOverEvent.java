package net.orhanbalci.pisti.event;

import io.vavr.control.Either;
import java.util.UUID;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public class GameOverEvent extends GameEvent {
  private UUID winnerPlayerId;

  public GameOverEvent(UUID gameId, UUID winnerPlayerId) {
    super(gameId);
    this.winnerPlayerId = winnerPlayerId;
  }

  public UUID getWinnerPlayer() {
    return winnerPlayerId;
  }

  @Override
  public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
    return v.visit(this);
  }

  @Override
  public String toString() {
    return String.format("GameOverEvent(%s %s)", getGameId(), getWinnerPlayer());
  }
}

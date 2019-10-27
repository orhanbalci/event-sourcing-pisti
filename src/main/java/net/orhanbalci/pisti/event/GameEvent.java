package net.orhanbalci.pisti.event;

import java.util.UUID;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public abstract class GameEvent implements Visitable<ValidationResult, GameState> {
  private final UUID eventId = UUID.randomUUID();
  protected UUID gameId;

  public GameEvent(UUID gameId) {
    this.gameId = gameId;
  }

  public UUID getEventId() {
    return eventId;
  }

  public UUID getGameId() {
    return gameId;
  }
}

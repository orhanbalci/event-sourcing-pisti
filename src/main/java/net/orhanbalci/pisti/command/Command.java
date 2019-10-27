package net.orhanbalci.pisti.command;

import java.util.UUID;

public abstract class Command {
  private final UUID commandId = UUID.randomUUID();
  private UUID gameId;

  public Command(UUID gameId) {
    this.gameId = gameId;
  }

  public UUID getCommandId() {
    return commandId;
  }

  public UUID getGameId() {
    return gameId;
  }
}

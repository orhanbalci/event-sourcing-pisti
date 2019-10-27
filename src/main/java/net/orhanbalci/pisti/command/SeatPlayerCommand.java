package net.orhanbalci.pisti.command;

import java.util.UUID;

public class SeatPlayerCommand extends Command {
  private UUID playerId;

  public SeatPlayerCommand(UUID gameId, UUID playerId) {
    super(gameId);
    this.playerId = playerId;
  }

  public UUID getPlayerId() {
    return playerId;
  }
}

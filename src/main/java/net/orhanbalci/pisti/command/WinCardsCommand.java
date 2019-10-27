package net.orhanbalci.pisti.command;

import java.util.UUID;

public class WinCardsCommand extends Command {
  public UUID playerId;

  public WinCardsCommand(UUID gameId, UUID playerId) {
    super(gameId);
    this.playerId = playerId;
  }

  public UUID getPlayerId() {
    return playerId;
  }
}

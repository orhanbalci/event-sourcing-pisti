package net.orhanbalci.pisti.command;

import java.util.UUID;

public class InitGameCommand extends Command {

  public InitGameCommand(UUID gameId) {
    super(gameId);
  }
}

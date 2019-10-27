package net.orhanbalci.pisti.command;

import java.util.UUID;

public class ChangeTurnCommand extends Command {

    public ChangeTurnCommand(UUID gameId) {
        super(gameId);
    }
}
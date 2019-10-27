package net.orhanbalci.pisti.command;

import java.util.UUID;

public class DealCardsCommand extends Command {
    private boolean dealCenter;

    public DealCardsCommand(UUID gameId, boolean dealCenter) {
        super(gameId);
        this.dealCenter = dealCenter;
        // TODO Auto-generated constructor stub
    }

    public boolean getDealCenter(){
        return dealCenter;
    }

}
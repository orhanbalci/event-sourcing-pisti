package net.orhanbalci.pisti.event;

import java.util.UUID;

public class CardsExhaustedEvent extends GameEvent {

    public CardsExhaustedEvent(UUID gameId){
        super(gameId);
    }

}
package net.orhanbalci.pisti;

import com.google.common.eventbus.Subscribe;

import net.orhanbalci.pisti.event.EventStorage;
import net.orhanbalci.pisti.event.GameEvent;

public class EventStore {

    public EventStore(){

    }

    @Subscribe
    public void handleEventStorage(GameEvent a){
        System.out.println(String.format("Event Store => %s",a));
    }
}
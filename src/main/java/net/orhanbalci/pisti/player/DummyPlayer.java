package net.orhanbalci.pisti.player;

import java.util.UUID;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import io.vavr.control.Option;
import net.orhanbalci.pisti.event.CardPlayedEvent;
import net.orhanbalci.pisti.event.CardsDealedEvent;
import net.orhanbalci.pisti.event.CardsExhaustedEvent;
import net.orhanbalci.pisti.event.CardsWonEvent;
import net.orhanbalci.pisti.event.GameInitedEvent;
import net.orhanbalci.pisti.event.GameOverEvent;
import net.orhanbalci.pisti.event.PlayerSeatedEvent;
import net.orhanbalci.pisti.event.PointScoredEvent;
import net.orhanbalci.pisti.event.TurnChangedEvent;

public class DummyPlayer extends PlayerAgent {

    private EventBus commandBus;


    public DummyPlayer(EventBus cb, UUID id) {
        this.commandBus = cb;
        this.id = id;
    }

    
    @Override
    @Subscribe
    public void handle(CardPlayedEvent event) {

    }

    @Override
    @Subscribe
    public void handle(CardsDealedEvent event) {

    }

    @Override
    @Subscribe
    public void handle(CardsExhaustedEvent event) {

    }

   
    @Override
    @Subscribe
    public void handle(GameInitedEvent event) {
        if(gameId.isDefined()){
            System.out.println("Player => Game already inited for player " + id);
        }else{
            gameId = Option.of(event.getGameId());
        }
    }

    @Override
    @Subscribe
    public void handle(GameOverEvent event) {

    }

    @Override
    @Subscribe
    public void handle(PlayerSeatedEvent event) {

    }

    @Override
    @Subscribe
    public void handle(PointScoredEvent event) {

    }

    @Override
    @Subscribe
    public void handle(TurnChangedEvent event) {

    }

    @Override
    @Subscribe
    public void handle(CardsWonEvent event) {

    }

}
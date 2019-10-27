package net.orhanbalci.pisti.player;

import java.util.UUID;

import com.google.common.eventbus.EventBus;

import io.vavr.collection.List;
import io.vavr.control.Option;
import net.orhanbalci.pisti.Card;
import net.orhanbalci.pisti.CardNumber;
import net.orhanbalci.pisti.command.PlayCardCommand;
import net.orhanbalci.pisti.event.CardPlayedEvent;
import net.orhanbalci.pisti.event.CardsDealedEvent;
import net.orhanbalci.pisti.event.CardsExhaustedEvent;
import net.orhanbalci.pisti.event.CardsWonEvent;
import net.orhanbalci.pisti.event.GameInitedEvent;
import net.orhanbalci.pisti.event.GameOverEvent;
import net.orhanbalci.pisti.event.PlayerSeatedEvent;
import net.orhanbalci.pisti.event.PointScoredEvent;
import net.orhanbalci.pisti.event.TurnChangedEvent;

public class SmartPlayer extends PlayerAgent {

    private List<Card> playedCards = List.empty();
    private EventBus commandBus;
    private UUID id;
    private Option<List<Card>> cards = Option.none();


    public SmartPlayer(EventBus cb, UUID id){
        this.commandBus = cb;
        this.id = id;
    }
    @Override
    public void handle(CardPlayedEvent event) {
        if (event.getGameId() == getGameId().getOrNull() && event.getPlayerId() == getId()) {
            cards = cards.map(c -> c.remove(event.getCard()));
        }

        if(event.getGameId() == getGameId().getOrNull()){
            playedCards = playedCards.append(event.getCard());
        }

    }

    @Override
    public void handle(CardsDealedEvent event) {
        if (event.getGameId() == getGameId().getOrNull() && event.getPlayer().getOrNull() == getId()) {
            if (cards.getOrElse(List.empty()).isEmpty()) {
                cards = Option.of(event.getCards());
            } else {
                System.out.println("Player cards is not empty can not deal");
            }
        }

    }

    @Override
    public void handle(CardsExhaustedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(CardsWonEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(GameInitedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(GameOverEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(PlayerSeatedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(PointScoredEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(TurnChangedEvent event) {
        if (event.getGameId() == getGameId().getOrNull() && event.getNextPlayerId() == getId()) {
            if(event.getCenterPile().isEmpty()){
                commandBus.post(new PlayCardCommand(getGameId().getOrNull(), getId(),
                        cards.getOrElse(List.empty()).shuffle().head()));
            }
            else if (cards.getOrElse(List.empty()).exists(c -> c.getNumber() == event.getCenterPile().last().getNumber())) {
                commandBus.post(new PlayCardCommand(getGameId().getOrNull(), getId(), cards.getOrElse(List.empty())
                        .find(c -> c.getNumber() == event.getCenterPile().last().getNumber()).getOrNull()));
            } else if (cards.getOrElse(List.empty()).exists(c -> c.getNumber() == CardNumber.JACK)) {
                commandBus.post(new PlayCardCommand(getGameId().getOrNull(), getId(),
                        cards.getOrElse(List.empty()).find(c -> c.getNumber() == CardNumber.JACK).getOrNull()));
            } else {
                commandBus.post(new PlayCardCommand(getGameId().getOrNull(), getId(),
                        cards.getOrElse(List.empty()).shuffle().head()));
            }
        }

    }

    @Override
    public void restart(){
        super.restart();
        cards = Option.none();
    }

}
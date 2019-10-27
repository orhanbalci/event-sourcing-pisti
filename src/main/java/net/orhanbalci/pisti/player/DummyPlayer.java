package net.orhanbalci.pisti.player;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.UUID;
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

public class DummyPlayer extends PlayerAgent {

  private EventBus commandBus;
  private Option<List<Card>> cards = Option.none();

  public DummyPlayer(EventBus cb, UUID id) {
    this.commandBus = cb;
    this.id = id;
  }

  @Override
  @Subscribe
  public void handle(CardPlayedEvent event) {
    if (event.getGameId() == getGameId().getOrNull() && event.getPlayerId() == getId()) {
      cards = cards.map(c -> c.remove(event.getCard()));
    }
  }

  @Override
  @Subscribe
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
  @Subscribe
  public void handle(CardsExhaustedEvent event) {}

  @Override
  @Subscribe
  public void handle(GameInitedEvent event) {
    if (gameId.isDefined()) {
      System.out.println("Player => Game already inited for player " + id);
    } else {
      gameId = Option.of(event.getGameId());
    }
  }

  @Override
  @Subscribe
  public void handle(GameOverEvent event) {}

  @Override
  @Subscribe
  public void handle(PlayerSeatedEvent event) {}

  @Override
  @Subscribe
  public void handle(PointScoredEvent event) {}

  @Override
  @Subscribe
  public void handle(TurnChangedEvent event) {
    if (event.getGameId() == getGameId().getOrNull() && event.getNextPlayerId() == getId()) {
      if (event.getCenterPile().isEmpty()) {
        commandBus.post(
            new PlayCardCommand(
                getGameId().getOrNull(), getId(), cards.getOrElse(List.empty()).shuffle().head()));
      } else if (cards
          .getOrElse(List.empty())
          .exists(c -> c.getNumber() == event.getCenterPile().last().getNumber())) {
        commandBus.post(
            new PlayCardCommand(
                getGameId().getOrNull(),
                getId(),
                cards
                    .getOrElse(List.empty())
                    .find(c -> c.getNumber() == event.getCenterPile().last().getNumber())
                    .getOrNull()));
      } else if (cards.getOrElse(List.empty()).exists(c -> c.getNumber() == CardNumber.JACK)) {
        commandBus.post(
            new PlayCardCommand(
                getGameId().getOrNull(),
                getId(),
                cards
                    .getOrElse(List.empty())
                    .find(c -> c.getNumber() == CardNumber.JACK)
                    .getOrNull()));
      } else {
        commandBus.post(
            new PlayCardCommand(
                getGameId().getOrNull(), getId(), cards.getOrElse(List.empty()).shuffle().head()));
      }
    }
  }

  @Override
  @Subscribe
  public void handle(CardsWonEvent event) {}

  @Override
  public void restart() {
    super.restart();
    cards = Option.none();
  }
}

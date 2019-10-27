package net.orhanbalci.pisti;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import net.orhanbalci.pisti.command.*;
import net.orhanbalci.pisti.event.CardPlayedEvent;
import net.orhanbalci.pisti.event.CardsWonEvent;
import net.orhanbalci.pisti.event.GameEvent;
import net.orhanbalci.pisti.event.GameInitedEvent;
import net.orhanbalci.pisti.event.GameOverEvent;
import net.orhanbalci.pisti.event.PlayerSeatedEvent;
import net.orhanbalci.pisti.event.PointScoredEvent;
import net.orhanbalci.pisti.player.PlayerAgent;
import net.orhanbalci.pisti.player.SmartPlayer;

public class Game {

  private EventBus commandBus = new EventBus("GameCommandBus");
  private EventBus eventBus = new EventBus("GameEventBus");
  private EventBus eventStoreBus = new EventBus("GameEventStoreBus");
  private EventBus scoreBus;
  private GameState gs = new GameState();
  private PlayerAgent[] agents = new PlayerAgent[4];
  private EventStore es = new EventStore();

  private int gameCount = 1;
  private String dummyPlayerClass = "";
  private String smartPlayerClass = "";

  public Game(EventBus scoreBus, int gameCount, String dummyPlayerClass, String smartPlayerClass) {
    this.gameCount = gameCount;
    this.dummyPlayerClass = dummyPlayerClass;
    this.smartPlayerClass = smartPlayerClass;
    this.scoreBus = scoreBus;
  }

  public Game() {}

  public void restartGame() {
    gs = new GameState();
    for (PlayerAgent playerAgent : agents) {
      playerAgent.restart();
    }
    publishCommand(new InitGameCommand(UUID.randomUUID()));
  }

  public void startGame() {
    try {
      agents[0] =
          constructPlayer(
              dummyPlayerClass, eventBus, UUID.randomUUID()); // new DummyPlayer(commandBus,
      // UUID.randomUUID());
      agents[1] =
          constructPlayer(
              smartPlayerClass, eventBus, UUID.randomUUID()); // new DummyPlayer(commandBus,
      // UUID.randomUUID());
      agents[2] =
          constructPlayer(
              dummyPlayerClass, eventBus, UUID.randomUUID()); // new DummyPlayer(commandBus,
      // UUID.randomUUID());
      agents[3] = constructPlayer(smartPlayerClass, eventBus, UUID.randomUUID());
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      e.printStackTrace();
    } // new DummyPlayer(commandBus, UUID.randomUUID());
    for (PlayerAgent agent : agents) {
      eventBus.register(agent);
    }

    commandBus.register(this);
    eventBus.register(this);
    eventStoreBus.register(es);

    publishCommand(new InitGameCommand(UUID.randomUUID()));
  }

  public PlayerAgent constructPlayer(String className, EventBus eb, UUID gameId)
      throws ClassNotFoundException, NoSuchMethodException, SecurityException,
          InstantiationException, IllegalAccessException, IllegalArgumentException,
          InvocationTargetException {
    Class<?> cl = Class.forName(className);
    Constructor<?> cons = cl.getConstructor(EventBus.class, UUID.class);
    return (PlayerAgent) cons.newInstance(eb, gameId);
  }

  @Subscribe
  public void handleCommand(InitGameCommand initGameCommand) {
    // System.out.println("Game => HandleCommand called " + initGameCommand.toString());
    handleUnpublishedEvents(gs.handleCommand(initGameCommand));
  }

  @Subscribe
  public void handleCommand(SeatPlayerCommand seatPlayer) {
    // System.out.println("Game => HandleCommand called " + seatPlayer.toString());
    handleUnpublishedEvents(gs.handleCommand(seatPlayer));
  }

  @Subscribe
  public void handleCommand(DealCardsCommand dealCards) {
    // System.out.println("Game => HandleCommand called " + dealCards.toString());
    handleUnpublishedEvents(gs.handleCommand(dealCards));
  }

  @Subscribe
  public void handleCommand(PlayCardCommand playCard) {
    // System.out.println("Game => HandleCommand called " + playCard);
    handleUnpublishedEvents(gs.handleCommand(playCard));
  }

  @Subscribe
  public void handleCommand(WinCardsCommand winCards) {
    // System.out.println("Game => HandleCommand called " + winCards);
    handleUnpublishedEvents(gs.handleCommand(winCards));
  }

  @Subscribe
  public void handleCommand(ChangeTurnCommand changeTurn) {
    // System.out.println("Game => HandleCommand called " + changeTurn);
    handleUnpublishedEvents(gs.handleCommand(changeTurn));
  }

  @Subscribe
  public void handleCommand(ScorePointCommand scorePoint) {
    // System.out.println("Game => HandleCommand called " + scorePoint);
    handleUnpublishedEvents(gs.handleCommand(scorePoint));
  }

  @Subscribe
  public void handleCommand(GameOverCommand gameOver) {
    // System.out.println("Game => HandleCommand called " + gameOver);
    handleUnpublishedEvents(gs.handleCommand(gameOver));
  }

  @Subscribe
  public void handleEvent(GameOverEvent gameOver) {
    String winnerType = "";
    for (PlayerAgent playerAgent : agents) {
      if (playerAgent.getId() == gameOver.getWinnerPlayer()) {
        if (playerAgent instanceof SmartPlayer) {
          winnerType = "SMART";
        } else {
          winnerType = "DUMMY";
        }
      }
    }
    gameOver.setWinnerType(winnerType);
    scoreBus.post(gameOver);
    gameCount -= 1;

    if (gameCount > 0) {
      restartGame();
    }
  }

  @Subscribe
  public void handleEvent(GameInitedEvent gameInited) {
    // System.out.println("Game => HandleEvent called " + gameInited);
    if (gs.getPlayers().getOrElse(List.empty()).isEmpty()) {
      publishCommand(new SeatPlayerCommand(gs.getGameId().get(), agents[0].getId()));
    }
  }

  @Subscribe
  public void handleEvent(PlayerSeatedEvent playerSeated) {
    // System.out.println("Game => HandleEvent called " + playerSeated);
    // masada eksik oyuncu varsa tamamla
    if (gs.getPlayers().getOrElse(List.empty()).length() < 4) {
      publishCommand(
          new SeatPlayerCommand(
              gs.getGameId().get(),
              agents[gs.getPlayers().getOrElse(List.empty()).length()].getId()));
    }
    // kartlari dagit
    else {
      publishCommand(new DealCardsCommand(gs.getGameId().get(), true));
    }
  }

  @Subscribe
  public void handleEvent(CardsWonEvent cardsWon) {
    List<PointType> points = List.of();
    var cards = cardsWon.getCenterPile();
    if (cards.length() == 2
        && cards.get(0).getNumber() == CardNumber.JACK
        && cards.get(1).getNumber() == CardNumber.JACK) {
      points = points.append(PointType.DOUBLE_PISTI);
    }

    if (cards.length() == 2 && cards.get(0).getNumber() == cards.get(1).getNumber()) {
      points = points.append(PointType.PISTI);
    }

    int numberOfAces = cards.filter(c -> c.getNumber() == CardNumber.ACE).length();
    for (int i = 0; i < numberOfAces; i++) {
      points = points.append(PointType.ACE);
    }

    int numberOfJacks = cards.filter(c -> c.getNumber() == CardNumber.JACK).length();
    for (int i = 0; i < numberOfJacks; i++) {
      points = points.append(PointType.JACK);
    }

    int twoOfClubs =
        cards
            .filter(c -> c.getNumber() == CardNumber.TWO && c.getType() == CardType.CLUBS)
            .length();
    for (int i = 0; i < twoOfClubs; i++) {
      points = points.append(PointType.TWO_OF_CLUBS);
    }

    int tenOfDiamonds =
        cards
            .filter(c -> c.getNumber() == CardNumber.TEN && c.getType() == CardType.DIAMONDS)
            .length();
    for (int i = 0; i < tenOfDiamonds; i++) {
      points = points.append(PointType.TEN_OF_DIAMONDS);
    }

    publishCommand(new ScorePointCommand(cardsWon.getGameId(), cardsWon.getPlayerIdWon(), points));
  }

  @Subscribe
  public void handleEvent(CardPlayedEvent cardPlayed) {

    if (gs.getCenterPile().getOrElse(List.empty()).length() > 1
        && gs.getCenterPile().getOrElse(List.empty()).last().getNumber() == CardNumber.JACK) {
      publishCommand(new WinCardsCommand(cardPlayed.getGameId(), cardPlayed.getPlayerId()));
      return;
    }

    if (gs.getCenterPile().getOrElse(List.empty()).length() > 1) {
      var lastTwo = gs.getCenterPile().getOrElse(List.empty()).takeRight(2);
      if (lastTwo.get(0).getNumber() == lastTwo.get(1).getNumber()) {
        publishCommand(new WinCardsCommand(cardPlayed.getGameId(), cardPlayed.getPlayerId()));
        return;
      }
    }

    if (gs.getCards().getOrElse(HashMap.empty()).exists((k) -> k._2.length() > 0)) {
      publishCommand(new ChangeTurnCommand(gs.getGameId().getOrNull()));
    } else {
      if (gs.getUndealedCards().getOrElse(List.empty()).isEmpty()) {
        if (gs.getCenterPile().getOrElse(List.empty()).length() > 0) {
          publishCommand(
              new WinCardsCommand(gs.getGameId().getOrNull(), gs.getLastWonPlayer().getOrNull()));
        } else {
          var cardCount =
              gs.getCardsWon()
                  .getOrElse(HashMap.empty())
                  .mapValues(l -> l.map(k -> k.length()).foldLeft(0, (b, i) -> b + i));

          var champ = cardCount.toList().sortBy(t -> t._2).reverse().head();
          publishCommand(
              new ScorePointCommand(
                  gs.getGameId().getOrNull(), champ._1, List.of(PointType.MAJORITY)));
          // publishCommand(new GameOverCommand(gs.getGameId().getOrNull()));
        }
      } else {
        publishCommand(new DealCardsCommand(gs.getGameId().getOrNull(), false));
      }
    }
  }

  @Subscribe
  public void handleEvent(PointScoredEvent pointScored) {
    // oyuncularin herhangi birinde kart varsa
    if (gs.getCards().getOrElse(HashMap.empty()).exists((k) -> k._2.length() > 0)) {
      publishCommand(new ChangeTurnCommand(gs.getGameId().getOrNull()));
    } else {
      if (gs.getUndealedCards().getOrElse(List.empty()).isEmpty()) {
        if (gs.getCenterPile().getOrElse(List.empty()).length() > 0) {
          publishCommand(
              new WinCardsCommand(gs.getGameId().getOrNull(), gs.getLastWonPlayer().getOrNull()));
        } else {
          if (!pointScored.getPoints().contains(PointType.MAJORITY)) {
            var cardCount =
                gs.getCardsWon()
                    .getOrElse(HashMap.empty())
                    .mapValues(l -> l.map(k -> k.length()).foldLeft(0, (b, i) -> b + i));

            var champ = cardCount.toList().sortBy(t -> t._2).reverse().head();
            publishCommand(
                new ScorePointCommand(
                    gs.getGameId().getOrNull(), champ._1, List.of(PointType.MAJORITY)));

          } else {
            publishCommand(new GameOverCommand(gs.getGameId().getOrNull()));
          }
        }
      } else {
        publishCommand(new DealCardsCommand(gs.getGameId().getOrNull(), false));
      }
    }
  }

  public void handleUnpublishedEvents(GameState newState) {
    for (GameEvent var : newState.getUnpublishedEvents().getOrElse(List.empty())) {
      gs = var.allowVisit(gs).getOrElse(gs).markAsPublished();
      // store event
      storeEvent(var);
      publishEvent(var);
    }
  }

  private void storeEvent(GameEvent ge) {
    eventStoreBus.post(ge);
  }

  private void publishEvent(GameEvent ge) {
    eventBus.post(ge);
  }

  private void publishCommand(Command command) {
    commandBus.post(command);
  }
}

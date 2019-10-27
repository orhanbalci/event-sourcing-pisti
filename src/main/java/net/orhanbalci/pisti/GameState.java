package net.orhanbalci.pisti;

import static io.vavr.API.*;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.UUID;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;
import net.orhanbalci.pisti.command.*;
import net.orhanbalci.pisti.event.*;

public class GameState implements Visitor<ValidationResult, GameState> {
  private Option<UUID> gameId = Option.none();
  private Option<List<UUID>> players = Option.none();
  private Option<HashMap<UUID, List<Card>>> cards = Option.none();
  private Option<List<Card>> centerPile = Option.none();
  private Option<UUID> currentTurnPlayer = Option.none();
  private Option<List<Card>> undealedCards = Option.none();
  private Option<List<GameEvent>> unpublishedEvents = Option.none();
  private Option<HashMap<UUID, List<List<Card>>>> cardsWon = Option.none();
  private Option<HashMap<UUID, List<PointType>>> pointsWon = Option.none();
  private Option<UUID> lastWonPlayer = Option.none();

  public GameState() {}

  private static GameState from(GameState other) {
    return new GameState()
        .withGameId(other.getGameId().getOrNull())
        .withPlayers(other.getPlayers().getOrElse(List.empty()))
        .withCards(other.getCards().getOrElse(HashMap.empty()))
        .withCenterPile(other.getCenterPile().getOrElse(List.empty()))
        .withCurrentTurnPlayer(other.getCurrentTurnPlayer().getOrNull())
        .withUndealedCards(other.getUndealedCards().getOrElse(List.empty()))
        .withPointsWon(other.getPointsWon().getOrElse(HashMap.empty()))
        .withPlayerWon(other.getLastWonPlayer().getOrNull())
        .withUnpublishedEvents(other.getUnpublishedEvents().getOrElse(List.empty()));
  }

  public GameState withGameId(UUID gameId) {
    this.gameId = Option.of(gameId);
    return this;
  }

  public GameState withPlayers(List<UUID> players) {
    this.players = Option.of(players);
    return this;
  }

  public GameState withCards(HashMap<UUID, List<Card>> cards) {
    this.cards = Option.of(cards);
    return this;
  }

  public GameState withCenterPile(List<Card> centerPile) {
    this.centerPile = Option.of(centerPile);
    return this;
  }

  public GameState withCurrentTurnPlayer(UUID currentTurnPlayer) {
    this.currentTurnPlayer = Option.of(currentTurnPlayer);
    return this;
  }

  public GameState withUndealedCards(List<Card> undealedCards) {
    this.undealedCards = Option.of(undealedCards);
    return this;
  }

  public GameState withCardsWon(HashMap<UUID, List<List<Card>>> cardsWon) {
    this.cardsWon = Option.of(cardsWon);
    return this;
  }

  public GameState withPointsWon(HashMap<UUID, List<PointType>> pointsWon) {
    this.pointsWon = Option.of(pointsWon);
    return this;
  }

  public GameState withUnpublishedEvents(List<GameEvent> unpublishedEvents) {
    this.unpublishedEvents = Option.of(unpublishedEvents);
    return this;
  }

  public GameState withPlayerWon(UUID playerWon) {
    this.lastWonPlayer = Option.of(playerWon);
    return this;
  }

  public Option<UUID> getGameId() {
    return gameId;
  }

  public Option<List<GameEvent>> getUnpublishedEvents() {
    return unpublishedEvents;
  }

  public Option<List<UUID>> getPlayers() {
    return players;
  }

  public Option<List<Card>> getUndealedCards() {
    return undealedCards;
  }

  public Option<HashMap<UUID, List<Card>>> getCards() {
    return cards;
  }

  public Option<List<Card>> getCenterPile() {
    return centerPile;
  }

  public Option<UUID> getCurrentTurnPlayer() {
    return currentTurnPlayer;
  }

  public Option<HashMap<UUID, List<List<Card>>>> getCardsWon() {
    return cardsWon;
  }

  public Option<HashMap<UUID, List<PointType>>> getPointsWon() {
    return pointsWon;
  }

  public Option<UUID> getLastWonPlayer() {
    return lastWonPlayer;
  }

  public Either<ValidationResult, GameState> applyEvent(GameInitedEvent gameInited) {
    return Match(GameStateValidator.validateGameUnited(this))
        .of(
            Case(
                $(ValidationResult.Succesfull),
                Either.right(
                    GameState.from(this)
                        .withGameId(gameInited.getGameId())
                        .withUndealedCards(Deck.getDeck())
                        .withUnpublishedEvents(
                            getUnpublishedEvents().getOrElse(List.empty()).append(gameInited)))),
            Case($(), (err) -> Either.left(err)));
  }

  public Either<ValidationResult, GameState> applyEvent(CardPlayedEvent cardPlayed) {
    return Match(
            GameStateValidator.validateCardOnPlayer(
                this, cardPlayed.getPlayerId(), cardPlayed.getCard()))
        .of(
            Case(
                $(ValidationResult.Succesfull),
                Either.right(
                    GameState.from(this)
                        .withCenterPile(
                            this.getCenterPile()
                                .getOrElse(List.empty())
                                .append(cardPlayed.getCard()))
                        .withCards(
                            this.getCards()
                                .getOrElse(HashMap.empty())
                                .put(
                                    cardPlayed.getPlayerId(),
                                    this.getCards()
                                        .getOrElse(HashMap.empty())
                                        .get(cardPlayed.getPlayerId())
                                        .getOrElse(List.empty())
                                        .remove(cardPlayed.getCard())))
                        .withUnpublishedEvents(
                            this.getUnpublishedEvents()
                                .getOrElse(List.empty())
                                .append(cardPlayed)))),
            Case($(), (err) -> Either.left(err)));
  }

  public Either<ValidationResult, GameState> applyEvent(CardsDealedEvent cardsDealed) {
    // player id verilmemisse center pile dagitim yapiliyordur
    if (cardsDealed.getPlayer().isEmpty()) {
      return Match(GameStateValidator.validateCenterPileEmpty(this))
          .of(
              Case(
                  $(ValidationResult.Succesfull),
                  Either.right(
                      GameState.from(this)
                          .withCenterPile(List.ofAll(cardsDealed.getCards()))
                          .withUndealedCards(
                              this.getUndealedCards()
                                  .getOrElse(List.empty())
                                  .removeAll(cardsDealed.getCards()))
                          .withUnpublishedEvents(
                              getUnpublishedEvents().getOrElse(List.empty()).append(cardsDealed)))),
              Case($(), (err) -> Either.left(err)));
    } else {
      return Match(
              GameStateValidator.validatePlayerPileEmpty(this, cardsDealed.getPlayer().getOrNull()))
          .of(
              Case(
                  $(ValidationResult.Succesfull),
                  Either.right(
                      GameState.from(this)
                          .withCards(
                              this.getCards()
                                  .getOrElse(HashMap.empty())
                                  .put(cardsDealed.getPlayer().getOrNull(), cardsDealed.getCards()))
                          .withUndealedCards(
                              this.getUndealedCards()
                                  .getOrElse(List.empty())
                                  .removeAll(cardsDealed.getCards()))
                          .withUnpublishedEvents(
                              getUnpublishedEvents().getOrElse(List.empty()).append(cardsDealed)))),
              Case($(), (err) -> Either.left(err)));
    }
  }

  public Either<ValidationResult, GameState> applyEvent(CardsExhaustedEvent cardsExhausted) {
    return Either.right(
        GameState.from(this)
            .withUnpublishedEvents(
                getUnpublishedEvents().getOrElse(List.empty()).append(cardsExhausted)));
  }

  public Either<ValidationResult, GameState> applyEvent(CardsWonEvent cardWonEvent) {
    return Either.right(
        GameState.from(this)
            .withCenterPile(null)
            .withCardsWon(
                this.getCardsWon()
                    .getOrElse(HashMap.empty())
                    .put(
                        cardWonEvent.getPlayerIdWon(),
                        this.getCardsWon()
                            .getOrElse(HashMap.empty())
                            .getOrElse(cardWonEvent.getPlayerIdWon(), List.empty())
                            .append(cardWonEvent.getCenterPile())))
            .withPlayerWon(cardWonEvent.getPlayerIdWon())
            .withUnpublishedEvents(
                getUnpublishedEvents().getOrElse(List.empty()).append(cardWonEvent)));
  }

  public Either<ValidationResult, GameState> applyEvent(GameOverEvent gameOver) {
    return Either.right(
        GameState.from(this)
            .withUnpublishedEvents(
                getUnpublishedEvents().getOrElse(List.empty()).append(gameOver)));
  }

  public Either<ValidationResult, GameState> applyEvent(PlayerSeatedEvent playerSeated) {
    return Match(GameStateValidator.validateEnoughSpaceForPlayer(this))
        .of(
            Case(
                $(ValidationResult.Succesfull),
                Either.right(
                    GameState.from(this)
                        .withPlayers(playerSeated.getPlayers())
                        .withUnpublishedEvents(
                            this.getUnpublishedEvents()
                                .getOrElse(List.empty())
                                .append(playerSeated)))),
            Case($(), (err) -> Either.left(err)));
  }

  public Either<ValidationResult, GameState> applyEvent(PointScoredEvent pointsScored) {
    return Either.right(
        GameState.from(this)
            .withPointsWon(
                this.getPointsWon()
                    .getOrElse(HashMap.empty())
                    .put(
                        pointsScored.getPlayerId(),
                        this.getPointsWon()
                            .getOrElse(HashMap.empty())
                            .getOrElse(pointsScored.getPlayerId(), List.empty())
                            .appendAll(pointsScored.getPoints())))
            .withPlayerWon(pointsScored.getPlayerId())
            .withUnpublishedEvents(
                getUnpublishedEvents().getOrElse(List.empty()).append(pointsScored)));
  }

  public Either<ValidationResult, GameState> applyEvent(TurnChangedEvent turnChanged) {
    return Either.right(
        GameState.from(this)
            .withCurrentTurnPlayer(turnChanged.getNextPlayerId())
            .withUnpublishedEvents(
                getUnpublishedEvents().getOrElse(List.empty()).append(turnChanged)));
  }

  public Either<ValidationResult, GameState> applyEvents(List<GameEvent> events) {
    if (events.isEmpty()) return Either.right(this);

    return events.head().allowVisit(this).flatMap(gs -> gs.applyEvents(events.pop()));
  }

  public GameState handleCommand(WinCardsCommand winCards) {
    CardsWonEvent cardsWon =
        new CardsWonEvent(
            getGameId().getOrNull(),
            winCards.getPlayerId(),
            getCenterPile().getOrElse(List.empty()));
    return applyEvent(cardsWon)
        .getOrElseGet(
            l -> {
              System.out.println("Error in handleCommand WinCardsCommand " + l);
              return this;
            });
  }

  public GameState handleCommand(PlayCardCommand playCard) {
    CardPlayedEvent cardPlayed =
        new CardPlayedEvent(getGameId().getOrNull(), playCard.getPlayerId(), playCard.getCard());

    return applyEvent(cardPlayed)
        .getOrElseGet(
            l -> {
              System.out.println("Error in handleCommand PlayCardCommand " + l);
              return this;
            });
  }

  public GameState handleCommand(InitGameCommand initGame) {
    GameInitedEvent initedEvent = new GameInitedEvent(initGame.getGameId());
    return applyEvent(initedEvent)
        .getOrElseGet(
            l -> {
              System.out.println("Error in handleCommand InitGameCommand " + l);
              return this;
            });
  }

  public GameState handleCommand(SeatPlayerCommand seatPlayer) {
    PlayerSeatedEvent playerSeatedEvent =
        new PlayerSeatedEvent(
            seatPlayer.getGameId(),
            getPlayers().getOrElse(List.empty()).append(seatPlayer.getPlayerId()));
    return applyEvent(playerSeatedEvent)
        .getOrElseGet(
            l -> {
              System.out.println("Error in handleCommand SeatPlayerCommand " + l);
              return this;
            });
  }

  public GameState handleCommand(DealCardsCommand dealCards) {

    if (dealCards.getDealCenter()) {
      CardsDealedEvent centerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.none(),
              this.getUndealedCards().getOrElse(List.empty()).take(4));

      CardsDealedEvent firstPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(this.getPlayers().getOrElse(List.empty()).get(3)),
              this.getUndealedCards().getOrElse(List.empty()).take(8).takeRight(4));

      CardsDealedEvent secondPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(this.getPlayers().getOrElse(List.empty()).get(2)),
              this.getUndealedCards().getOrElse(List.empty()).take(12).takeRight(4));

      CardsDealedEvent thirdPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(this.getPlayers().getOrElse(List.empty()).get(1)),
              this.getUndealedCards().getOrElse(List.empty()).take(16).takeRight(4));

      CardsDealedEvent fourthPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(this.getPlayers().getOrElse(List.empty()).get(0)),
              this.getUndealedCards().getOrElse(List.empty()).take(20).takeRight(4));

      TurnChangedEvent turnChanged =
          new TurnChangedEvent(
              getGameId().getOrNull(),
              getNextPlayer(),
              this.getUndealedCards().getOrElse(List.empty()).take(4));

      return applyEvents(
              List.of(
                  centerDeal,
                  firstPlayerDeal,
                  secondPlayerDeal,
                  thirdPlayerDeal,
                  fourthPlayerDeal,
                  turnChanged))
          .getOrElseGet(
              l -> {
                System.out.println("Error in handleCommand DealCardsCommand " + l);
                return this;
              });

    } else {

      int currentPlayerIndex =
          getPlayers().getOrElse(List.empty()).indexOf(getCurrentTurnPlayer().getOrNull());

      CardsDealedEvent firstPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(
                  this.getPlayers().getOrElse(List.empty()).get((currentPlayerIndex + 1) % 4)),
              this.getUndealedCards().getOrElse(List.empty()).take(4).takeRight(4));

      CardsDealedEvent secondPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(
                  this.getPlayers().getOrElse(List.empty()).get((currentPlayerIndex + 2) % 4)),
              this.getUndealedCards().getOrElse(List.empty()).take(8).takeRight(4));

      CardsDealedEvent thirdPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(
                  this.getPlayers().getOrElse(List.empty()).get((currentPlayerIndex + 3) % 4)),
              this.getUndealedCards().getOrElse(List.empty()).take(12).takeRight(4));

      CardsDealedEvent fourthPlayerDeal =
          new CardsDealedEvent(
              dealCards.getGameId(),
              Option.of(this.getPlayers().getOrElse(List.empty()).get(currentPlayerIndex)),
              this.getUndealedCards().getOrElse(List.empty()).take(16).takeRight(4));

      TurnChangedEvent turnChanged =
          new TurnChangedEvent(
              getGameId().getOrNull(), getNextPlayer(), getCenterPile().getOrElse(List.empty()));

      return applyEvents(
              List.of(
                  firstPlayerDeal,
                  secondPlayerDeal,
                  thirdPlayerDeal,
                  fourthPlayerDeal,
                  turnChanged))
          .getOrElseGet(
              l -> {
                System.out.println("Error in handleCommand DealCardsCommand " + l);
                return this;
              });
    }
  }

  public GameState handleCommand(ChangeTurnCommand changeTurn) {
    TurnChangedEvent turnChanged =
        new TurnChangedEvent(
            changeTurn.getGameId(), getNextPlayer(), getCenterPile().getOrElse(List.empty()));
    return applyEvent(turnChanged)
        .getOrElseGet(
            l -> {
              System.out.println("Error in handleCommand ChangeTurnCommand " + l);
              return this;
            });
  }

  public GameState handleCommand(ScorePointCommand scorePoint) {
    PointScoredEvent pointsScored =
        new PointScoredEvent(
            scorePoint.getGameId(), scorePoint.getPlayer(), scorePoint.getPoints());
    return applyEvent(pointsScored)
        .getOrElseGet(
            l -> {
              System.out.println("Error in handleCommand ScorePointCommand " + l);
              return this;
            });
  }

  public GameState handleCommand(GameOverCommand gameOver) {
    var a =
        pointsWon
            .getOrElse(HashMap.empty())
            .mapValues(l -> l.foldLeft(0, (y, b) -> y + b.getPoints()));
    var winner = a.maxBy(t -> t._2);
    GameOverEvent gameOverEvent = new GameOverEvent(getGameId().getOrNull(), winner.getOrNull()._1);
    return applyEvent(gameOverEvent)
        .getOrElseGet(
            l -> {
              System.out.println("Error in handleCommand GameOverCommand " + l);
              return this;
            });
  }

  public GameState markAsPublished() {
    return GameState.from(this).withUnpublishedEvents(null);
  }

  public UUID getNextPlayer() {
    if (currentTurnPlayer.isEmpty()) {
      return players.getOrElse(List.empty()).get(0);
    } else {
      var index = players.getOrElse(List.empty()).indexOf(currentTurnPlayer.getOrNull());
      var nextIndex = (index + 1) % 4;
      return players.getOrElse(List.empty()).get(nextIndex);
    }
  }

  public UUID getPreviousPlayer() {
    if (currentTurnPlayer.isEmpty()) {
      return players.getOrElse(List.empty()).get(0);
    } else {
      var index = players.getOrElse(List.empty()).indexOf(currentTurnPlayer.getOrNull());
      var nextIndex = (index + 3) % 4;
      return players.getOrElse(List.empty()).get(nextIndex);
    }
  }

  @Override
  public Either<ValidationResult, GameState> visit(CardPlayedEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(CardsDealedEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(CardsExhaustedEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(CardsWonEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(GameInitedEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(GameOverEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(PlayerSeatedEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(PointScoredEvent event) {
    return this.applyEvent(event);
  }

  @Override
  public Either<ValidationResult, GameState> visit(TurnChangedEvent event) {
    return this.applyEvent(event);
  }
}

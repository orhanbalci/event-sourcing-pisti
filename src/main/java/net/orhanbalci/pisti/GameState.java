package net.orhanbalci.pisti;

import java.util.UUID;

import io.vavr.API.Match;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Option.Some;
import net.orhanbalci.pisti.command.PlayCardCommand;
import net.orhanbalci.pisti.event.CardPlayedEvent;
import net.orhanbalci.pisti.event.CardsDealedEvent;
import net.orhanbalci.pisti.event.CardsExhaustedEvent;
import net.orhanbalci.pisti.event.CardsWonEvent;
import net.orhanbalci.pisti.event.GameEvent;
import net.orhanbalci.pisti.event.GameInitedEvent;
import net.orhanbalci.pisti.event.GameOverEvent;
import net.orhanbalci.pisti.event.PlayerSeatedEvent;
import net.orhanbalci.pisti.event.PointScoredEvent;
import net.orhanbalci.pisti.event.TurnChangedEvent;
import static io.vavr.API.*;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public class GameState {
    private Option<UUID> gameId = Option.none();
    private Option<List<UUID>> players = Option.none();
    private Option<HashMap<UUID, List<Card>>> cards = Option.none();
    private Option<List<Card>> centerPile = Option.none();
    private Option<UUID> currentTurnPlayer = Option.none();
    private Option<List<Card>> undealedCards = Option.none();
    private Option<List<GameEvent>> unpublishedEvents = Option.none();
    private Option<HashMap<UUID, List<List<Card>>>> cardsWon = Option.none();

    public GameState() {

    }

    public static GameState from(GameState other) {
        return new GameState().withGameId(other.getGameId().getOrNull())
                .withPlayers(other.getPlayers().getOrElse(List.empty()))
                .withCards(other.getCards().getOrElse(HashMap.empty()))
                .withCenterPile(other.getCenterPile().getOrElse(List.empty()))
                .withCurrentTurnPlayer(other.getCurrentTurnPlayer().getOrNull())
                .withUndealedCards(other.getUndealedCards().getOrElse(List.empty()))
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

    public GameState withCardsWon(HashMap<UUID, List<List<Card>>> cardsWon){
        this.cardsWon = Option.of(cardsWon);
        return this;
    }

    public GameState withUnpublishedEvents(List<GameEvent> unpublishedEvents) {
        this.unpublishedEvents = Option.of(unpublishedEvents);
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

    public Option<HashMap<UUID,List<List<Card>>>> getCardsWon(){
        return cardsWon;
    }

    public Either<ValidationResult, GameState> applyEvent(GameInitedEvent gameInited) {
        return Match(GameStateValidator.validateGameUnited(this)).of(Case($(ValidationResult.Succesfull),
                Either.right(GameState.from(this).withGameId(gameInited.getGameId()).withUndealedCards(Deck.getDeck())
                        .withUnpublishedEvents(getUnpublishedEvents().getOrElse(List.empty()).append(gameInited)))),
                Case($(), (err) -> Either.left(err)));
    }

    public Either<ValidationResult, GameState> applyEvent(CardPlayedEvent cardPlayed) {
        return Match(GameStateValidator.validateCardOnPlayer(this, cardPlayed.getPlayerId(), cardPlayed.getCard())).of(
            Case($(ValidationResult.Succesfull), Either.right(
                GameState.from(this)
                    .withCenterPile(this.getCenterPile().getOrElse(List.empty()).append(cardPlayed.getCard()))
                    .withCards(this.getCards().getOrElse(HashMap.empty()).replaceValue(cardPlayed.getPlayerId(), 
                        this.getCards().getOrElse(HashMap.empty()).get(cardPlayed.getPlayerId()).getOrElse(List.empty()).append(cardPlayed.getCard())
                    ))
                    .withUnpublishedEvents(this.getUnpublishedEvents().getOrElse(List.empty()).append(cardPlayed))

            )),
            Case($(), (err) -> Either.left(err))
        );        
    }

    public Either<ValidationResult, GameState> applyEvent(CardsDealedEvent cardsDealed) {
        // player id verilmemisse center pile dagitim yapiliyordur
        if (cardsDealed.getPlayer().isDefined()) {
            return Match(GameStateValidator.validateCenterPileEmpty(this)).of(

                    Case($(ValidationResult.Succesfull),
                            Either.right(GameState.from(this).withCenterPile(List.ofAll(cardsDealed.getCards()))
                                    .withUndealedCards(this.getUndealedCards().getOrElse(List.empty())
                                            .removeAll(cardsDealed.getCards()))
                                    .withUnpublishedEvents(
                                            getUnpublishedEvents().getOrElse(List.empty()).append(cardsDealed)))),

                    Case($(), (err) -> Either.left(err)));
        } else {
            return Match(GameStateValidator.validatePlayerPileEmpty(this, cardsDealed.getPlayer().getOrNull())).of(
                    Case($(ValidationResult.Succesfull), Either.right(GameState.from(this)
                            .withCards(this.getCards().getOrElse(HashMap.empty())
                                    .replaceValue(cardsDealed.getPlayer().getOrNull(), cardsDealed.getCards()))
                            .withUndealedCards(
                                    this.getUndealedCards().getOrElse(List.empty()).removeAll(cardsDealed.getCards()))
                            .withUnpublishedEvents(
                                    getUnpublishedEvents().getOrElse(List.empty()).append(cardsDealed)))),

                    Case($(), (err) -> Either.left(err)));
        }
    }

    public GameState applyEvent(CardsExhaustedEvent cardsExhausted) {
        return this;
    }

    public Either<ValidationResult,GameState> applyEvent(CardsWonEvent cardWonEvent) {
        return Either.right(GameState.from(this)
            .withCenterPile(null)
            .withCardsWon(this.getCardsWon()
                                .getOrElse(HashMap.empty())
                                .put(cardWonEvent.getPlayerIdWon(), this.getCardsWon()
                                .getOrElse(HashMap.empty()).getOrElse(cardWonEvent.getPlayerIdWon(), List.empty()).append(cardWonEvent.getCenterPile())))           
      
            );

    }

    public GameState applyEvent(GameOverEvent gameOver) {
        return null;

    }

    public GameState applyEvent(PlayerSeatedEvent playerSeated) {
        return null;

    }

    public GameState applyEvent(PointScoredEvent pointsScored) {
        return null;

    }

    public GameState applyEvent(TurnChangedEvent turnChanged) {
        return null;

    }

    private List<GameEvent> handleCommandInner(PlayCardCommand playCard) {
        return null;
    }

    public GameState handleCommand(PlayCardCommand playCard) {
        return null;

    }

    public GameState markAsPublished() {
        return null;
    }

}
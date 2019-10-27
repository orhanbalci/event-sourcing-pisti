package net.orhanbalci.pisti.event;

import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.UUID;
import net.orhanbalci.pisti.Card;
import net.orhanbalci.pisti.GameState;
import net.orhanbalci.pisti.GameStateValidator.ValidationResult;

public class CardsDealedEvent extends GameEvent {
  private List<Card> cards;
  private Option<UUID> playerId;

  public CardsDealedEvent(UUID gameId, Option<UUID> playerId, List<Card> cards) {
    super(gameId);
    this.playerId = playerId;
    this.cards = cards;
  }

  public Option<UUID> getPlayer() {
    return playerId;
  }

  public List<Card> getCards() {
    return cards;
  }

  @Override
  public Either<ValidationResult, GameState> allowVisit(Visitor<ValidationResult, GameState> v) {
    return v.visit(this);
  }

  @Override
  public String toString() {
    return String.format("CardsDealed(%s %s %s)", getGameId(), getPlayer(), getCards());
  }
}

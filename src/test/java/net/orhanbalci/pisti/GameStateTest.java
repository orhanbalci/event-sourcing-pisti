package net.orhanbalci.pisti;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.UUID;
import net.orhanbalci.pisti.event.CardsWonEvent;
import net.orhanbalci.pisti.event.GameInitedEvent;
import org.junit.jupiter.api.Test;

public class GameStateTest {
  @Test
  public void testGameInitedEvent() {
    GameState st = new GameState();
    var gameId = UUID.randomUUID();
    var newState = st.applyEvent(new GameInitedEvent(gameId));
    assertEquals(true, newState.get().getGameId().isDefined());
    assertEquals(gameId, newState.get().getGameId().getOrNull());
  }

  @Test
  public void testCardsWonEvent() {
    var gameId = UUID.randomUUID();
    GameState st =
        new GameState()
            .withGameId(gameId)
            .withCenterPile(List.of(Deck.AceOfClubs, Deck.AceOfDiamonds));

    var playerIdwon = UUID.randomUUID();

    var newState =
        st.applyEvent(
            new CardsWonEvent(gameId, playerIdwon, List.of(Deck.AceOfClubs, Deck.AceOfDiamonds)));

    assertTrue(newState.isRight());
    assertEquals(Option.none(), newState.get().getCenterPile());
    assertArrayEquals(
        List.of(Deck.AceOfClubs, Deck.AceOfDiamonds).toJavaArray(),
        newState
            .get()
            .getCardsWon()
            .getOrNull()
            .getOrElse(playerIdwon, List.empty())
            .head()
            .toJavaArray());
  }
}

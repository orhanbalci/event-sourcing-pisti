package net.orhanbalci.pisti;

import com.google.common.eventbus.Subscribe;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import java.util.UUID;
import net.orhanbalci.pisti.event.GameOverEvent;

public class Dashboard {
  private HashMap<UUID, Integer> winCounts = HashMap.empty();

  public void printDashBoard() {
    List<Tuple2<UUID, Integer>> winCountsSorted =
        winCounts.iterator().toList().sortBy(t -> t._2).reverse();
    for (int i = 0; i < winCountsSorted.length(); i++) {
      System.out.println(
          String.format(
              "%d | %s | %d ", i + 1, winCountsSorted.get(i)._1, winCountsSorted.get(i)._2));
    }
  }

  @Subscribe
  public void handleEvent(GameOverEvent gameOver) {
    winCounts =
        winCounts.put(
            gameOver.getWinnerPlayer(), winCounts.getOrElse(gameOver.getWinnerPlayer(), 0) + 1);
  }
}

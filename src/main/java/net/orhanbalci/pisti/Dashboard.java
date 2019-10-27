package net.orhanbalci.pisti;

import com.google.common.eventbus.Subscribe;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import net.orhanbalci.pisti.event.GameOverEvent;

public class Dashboard {
  private HashMap<String, Integer> winCounts = HashMap.empty();

  public void printDashBoard() {
    List<Tuple2<String, Integer>> winCountsSorted =
        winCounts.iterator().toList().sortBy(t -> t._2).reverse();
    for (int i = 0; i < winCountsSorted.length(); i++) {
      System.out.println(
          String.format(
              "%d | %s | %d ", i + 1, winCountsSorted.get(i)._1, winCountsSorted.get(i)._2));
    }
  }

  @Subscribe
  public void handleEvent(GameOverEvent gameOver) {
    String key = gameOver.getWinnerPlayer() + gameOver.getWinnerType();
    winCounts = winCounts.put(key, winCounts.getOrElse(key, 0) + 1);
  }
}

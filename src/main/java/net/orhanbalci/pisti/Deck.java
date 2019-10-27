package net.orhanbalci.pisti;

import io.vavr.collection.List;
import java.util.Arrays;

public class Deck {
  public static final Card AceOfSpades = new Card(CardNumber.ACE, CardType.SPADES);
  public static final Card KingOfSpades = new Card(CardNumber.KING, CardType.SPADES);
  public static final Card QueenOfSpades = new Card(CardNumber.QUEEN, CardType.SPADES);
  public static final Card JackOfSpades = new Card(CardNumber.JACK, CardType.SPADES);
  public static final Card TenOfSpades = new Card(CardNumber.TEN, CardType.SPADES);
  public static final Card NineOfSpades = new Card(CardNumber.NINE, CardType.SPADES);
  public static final Card EightOfSpades = new Card(CardNumber.EIGHT, CardType.SPADES);
  public static final Card SevenOfSpades = new Card(CardNumber.SEVEN, CardType.SPADES);
  public static final Card SixOfSpades = new Card(CardNumber.SIX, CardType.SPADES);
  public static final Card FiveOfSpades = new Card(CardNumber.FIVE, CardType.SPADES);
  public static final Card FourOfSpades = new Card(CardNumber.FOUR, CardType.SPADES);
  public static final Card ThreeOfSpades = new Card(CardNumber.THREE, CardType.SPADES);
  public static final Card TwoOfSpades = new Card(CardNumber.TWO, CardType.SPADES);

  public static final Card AceOfHearts = new Card(CardNumber.ACE, CardType.HEARTS);
  public static final Card KingOfHearts = new Card(CardNumber.KING, CardType.HEARTS);
  public static final Card QueenOfHearts = new Card(CardNumber.QUEEN, CardType.HEARTS);
  public static final Card JackOfHearts = new Card(CardNumber.JACK, CardType.HEARTS);
  public static final Card TenOfHearts = new Card(CardNumber.TEN, CardType.HEARTS);
  public static final Card NineOfHearts = new Card(CardNumber.NINE, CardType.HEARTS);
  public static final Card EightOfHearts = new Card(CardNumber.EIGHT, CardType.HEARTS);
  public static final Card SevenOfHearts = new Card(CardNumber.SEVEN, CardType.HEARTS);
  public static final Card SixOfHearts = new Card(CardNumber.SIX, CardType.HEARTS);
  public static final Card FiveOfHearts = new Card(CardNumber.FIVE, CardType.HEARTS);
  public static final Card FourOfHearts = new Card(CardNumber.FOUR, CardType.HEARTS);
  public static final Card ThreeOfHearts = new Card(CardNumber.THREE, CardType.HEARTS);
  public static final Card TwoOfHearts = new Card(CardNumber.TWO, CardType.HEARTS);

  public static final Card AceOfDiamonds = new Card(CardNumber.ACE, CardType.DIAMONDS);
  public static final Card KingOfDiamonds = new Card(CardNumber.KING, CardType.DIAMONDS);
  public static final Card QueenOfDiamonds = new Card(CardNumber.QUEEN, CardType.DIAMONDS);
  public static final Card JackOfDiamonds = new Card(CardNumber.JACK, CardType.DIAMONDS);
  public static final Card TenOfDiamonds = new Card(CardNumber.TEN, CardType.DIAMONDS);
  public static final Card NineOfDiamonds = new Card(CardNumber.NINE, CardType.DIAMONDS);
  public static final Card EightOfDiamonds = new Card(CardNumber.EIGHT, CardType.DIAMONDS);
  public static final Card SevenOfDiamonds = new Card(CardNumber.SEVEN, CardType.DIAMONDS);
  public static final Card SixOfDiamonds = new Card(CardNumber.SIX, CardType.DIAMONDS);
  public static final Card FiveOfDiamonds = new Card(CardNumber.FIVE, CardType.DIAMONDS);
  public static final Card FourOfDiamonds = new Card(CardNumber.FOUR, CardType.DIAMONDS);
  public static final Card ThreeOfDiamonds = new Card(CardNumber.THREE, CardType.DIAMONDS);
  public static final Card TwoOfDiamonds = new Card(CardNumber.TWO, CardType.DIAMONDS);

  public static final Card AceOfClubs = new Card(CardNumber.ACE, CardType.CLUBS);
  public static final Card KingOfClubs = new Card(CardNumber.KING, CardType.CLUBS);
  public static final Card QueenOfClubs = new Card(CardNumber.QUEEN, CardType.CLUBS);
  public static final Card JackOfClubs = new Card(CardNumber.JACK, CardType.CLUBS);
  public static final Card TenOfClubs = new Card(CardNumber.TEN, CardType.CLUBS);
  public static final Card NineOfClubs = new Card(CardNumber.NINE, CardType.CLUBS);
  public static final Card EightOfClubs = new Card(CardNumber.EIGHT, CardType.CLUBS);
  public static final Card SevenOfClubs = new Card(CardNumber.SEVEN, CardType.CLUBS);
  public static final Card SixOfClubs = new Card(CardNumber.SIX, CardType.CLUBS);
  public static final Card FiveOfClubs = new Card(CardNumber.FIVE, CardType.CLUBS);
  public static final Card FourOfClubs = new Card(CardNumber.FOUR, CardType.CLUBS);
  public static final Card ThreeOfClubs = new Card(CardNumber.THREE, CardType.CLUBS);
  public static final Card TwoOfClubs = new Card(CardNumber.TWO, CardType.CLUBS);

  private static final List<Card> deck =
      List.ofAll(
          Arrays.asList(
              AceOfSpades,
              KingOfSpades,
              QueenOfSpades,
              JackOfSpades,
              TenOfSpades,
              NineOfSpades,
              EightOfSpades,
              SevenOfSpades,
              SixOfSpades,
              FiveOfSpades,
              FourOfSpades,
              ThreeOfSpades,
              TwoOfSpades,
              AceOfHearts,
              KingOfHearts,
              QueenOfHearts,
              JackOfHearts,
              TenOfHearts,
              NineOfHearts,
              EightOfHearts,
              SevenOfHearts,
              SixOfHearts,
              FiveOfHearts,
              FourOfHearts,
              ThreeOfHearts,
              TwoOfHearts,
              AceOfDiamonds,
              KingOfDiamonds,
              QueenOfDiamonds,
              JackOfDiamonds,
              TenOfDiamonds,
              NineOfDiamonds,
              EightOfDiamonds,
              SevenOfDiamonds,
              SixOfDiamonds,
              FiveOfDiamonds,
              FourOfDiamonds,
              ThreeOfDiamonds,
              TwoOfDiamonds,
              AceOfClubs,
              KingOfClubs,
              QueenOfClubs,
              JackOfClubs,
              TenOfClubs,
              NineOfClubs,
              EightOfClubs,
              SevenOfClubs,
              SixOfClubs,
              FiveOfClubs,
              FourOfClubs,
              ThreeOfClubs,
              TwoOfClubs));

  public static List<Card> getDeck() {
    return List.ofAll(deck).shuffle();
  }
}

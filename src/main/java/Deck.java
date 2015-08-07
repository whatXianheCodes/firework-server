import com.google.common.collect.Lists;
import util.MersenneTwisterFast;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author xianhehuang@gmail.com
 */
public class Deck {
  private static Logger LOGGER = Logger.getLogger(Deck.class.getName());
  private static MersenneTwisterFast rand = new MersenneTwisterFast(System.currentTimeMillis());

  public List<Card> deck = Lists.newArrayList();

  public Deck () {
    createDeck();
  }

  private void createDeck () {
    for (int duplicateRef = 0; duplicateRef < 2; duplicateRef ++) {
      for (int cardValueRef = 0; cardValueRef < Card.CardValue.values().length; cardValueRef++) {
        for (int cardColorRef = 0; cardColorRef < Card.CardColor.values().length; cardColorRef++) {
          Card card = new Card(Card.CardValue.values()[cardValueRef], Card.CardColor.values()[cardColorRef],
              calculateId(duplicateRef, cardValueRef, cardColorRef));
          LOGGER.info("card id" + card.getCardId());
          deck.add(card);
        }
      }
    }
  }

  public int calculateId(int duplicateRef, int cardValueRef, int cardColorRef) {
    return (duplicateRef *  Card.CardValue.values().length * Card.CardColor.values().length) + ((cardValueRef *
        Card.CardColor.values().length) + cardColorRef);
  }

  public Card randomizedDraw () {
    if (deck.size() == 0) {
      LOGGER.info("THERE ARE NO CARDS LEFT");
      return null;
    }
    int randomCardIndex = rand.nextInt(deck.size());
    Card card = deck.get(randomCardIndex);
    deck.remove(randomCardIndex);
    return card;
  }

  public int getDeckSize() {
    return deck.size();
  }

  public void printDeck () {
    LOGGER.info("Card deck has the size of " + deck.size());
    for (Card card : deck) {
      LOGGER.info(card.getValue() + " of " + card.getCardColor());
    }
  }

  public void newDeck () {
    deck.clear();
    createDeck();
  }
}

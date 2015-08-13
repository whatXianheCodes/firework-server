package com.xianheh.game.cardutil;

import com.xianheh.game.util.MersenneTwisterFast;
import com.google.common.collect.Lists;

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
      for (int cardValueRef = 1; cardValueRef <= 5; cardValueRef++) {
        for (int cardColorRef = 0; cardColorRef < Card.CardColor.values().length; cardColorRef++) {
          Card card = new Card(cardValueRef, Card.CardColor.values()[cardColorRef]);
          deck.add(card);
        }
      }
    }
  }

  public Card randomizedDraw () {
    if (deck.size() == 0) {
      LOGGER.info("THERE ARE NO CARDS LEFT");
      return null;
    }
    int randomCardIndex = rand.nextInt(deck.size());
    Card card = deck.get(randomCardIndex);
    card.setCardId(deck.size()-1);
    deck.remove(randomCardIndex);
    return card;
  }

  public int getSize() {
    return deck.size();
  }

  public List<Card> getDeck() {
    return this.deck;
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

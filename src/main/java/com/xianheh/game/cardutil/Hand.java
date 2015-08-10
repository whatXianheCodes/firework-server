package com.xianheh.game.cardutil;

import com.google.gson.annotations.SerializedName;

import java.util.logging.Logger;

/**
 * @author xianhehuang@gmail.com
 */
public class Hand {

  private static final int MAX_HAND = 4;
  private static Logger LOGGER = Logger.getLogger(Hand.class.getName());

  private Card[] hand;
  @SerializedName("player_id")
  private int playerId;

  public Hand(Deck deck, int playerId) {
    hand = new Card[MAX_HAND];
    for (int cardCount = 0; cardCount < MAX_HAND; cardCount++) {
      hand[cardCount] = deck.randomizedDraw();
    }
    this.playerId = playerId;
  }

  public Card getCard(int index) {
    return hand[index];
  }

  public boolean replace(Card card, Deck deck) {
    Card currentCard;
    for (int cardRef = 0; cardRef < MAX_HAND; cardRef++) {
      currentCard = hand[cardRef];
      if (currentCard.getCardColor() == card.getCardColor() && currentCard.getCardId() == currentCard.getCardId() &&
          currentCard.getValue() == card.getValue()) {
        hand[cardRef] = deck.randomizedDraw();
        return true;
      }
    }
    return false;
  }

  public void printHand () {
    for (Card card : hand) {
      LOGGER.info(card.getValue() + " of " + card.getCardColor());
    }
  }

  public int getPlayerId() {
    return playerId;
  }

  public void setPlayerId(int playerId) {
    this.playerId = playerId;
  }
}

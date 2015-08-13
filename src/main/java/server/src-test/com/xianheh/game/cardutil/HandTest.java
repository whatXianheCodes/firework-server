package com.xianheh.game.cardutil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xianhehuang@gmail.com
 */
public class HandTest {
  @Test
  public void testReplaceHand() {
    Deck deck = new Deck();
    Hand hand = new Hand(deck, 1);
    Assert.assertEquals(50 - Hand.MAX_HAND, deck.getSize());
    Card cardToBeReplaced = hand.getCard(0);
    hand.replace(cardToBeReplaced, deck);
    Assert.assertEquals(50 - Hand.MAX_HAND - 1, deck.getSize());
    for (int i = 0; i < Hand.MAX_HAND; i++) {
      Card currentCardInHand = hand.getCard(i);
      if (currentCardInHand.getCardId() == cardToBeReplaced.getCardId()) {
        Assert.fail();
      }
    }
  }
}

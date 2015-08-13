package com.xianheh.game.cardutil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xianhehuang@gmail.com
 */
public class DeckTest {

  @Test
  public void testRandomizedDraw() {
    int blueColor = 10;
    int yellowColor = 10;
    int redColor = 10;
    int greenColor = 10;
    int whiteColor = 10;
    int oneValue = 10;
    int twoValue = 10;
    int threeValue = 10;
    int fourValue = 10;
    int fiveValue = 10;

    Card[] cards = new Card[50];
    Deck deck = new Deck();
    Assert.assertEquals(50, deck.getSize());
    for (int i = 0; i < 50; i++) {
      Card card = deck.randomizedDraw();
      switch (card.getCardColor()) {
        case RED:
          redColor--;
          break;
        case BLUE:
          blueColor--;
          break;
        case YELLOW:
          yellowColor--;
          break;
        case GREEN:
          greenColor--;
          break;
        case WHITE:
          whiteColor--;
          break;
      }

      switch (card.getValue()) {
        case 1:
          oneValue--;
          break;
        case 2:
          twoValue--;
          break;
        case 3:
          threeValue--;
          break;
        case 4:
          fourValue--;
          break;
        case 5:
          fiveValue--;
          break;
      }

      cards[card.getCardId()] = card;
    }

    for (int i = 0; i < 50; i++) {
      if (cards[i] == null) {
        Assert.fail();
      }
    }
    Assert.assertEquals(0, blueColor);
    Assert.assertEquals(0, greenColor);
    Assert.assertEquals(0, redColor);
    Assert.assertEquals(0, whiteColor);
    Assert.assertEquals(0, yellowColor);
    Assert.assertEquals(0, oneValue);
    Assert.assertEquals(0, twoValue);
    Assert.assertEquals(0, threeValue);
    Assert.assertEquals(0, fourValue);
    Assert.assertEquals(0, fiveValue);
  }

  @Test
  public void testNewDeck() {
    Deck deck = new Deck();
    Assert.assertEquals(50, deck.getSize());
    for (int i = 0; i < 50; i++) {
      deck.randomizedDraw();
    }
    Assert.assertEquals(0, deck.getSize());
    deck.newDeck();
    Assert.assertEquals(50, deck.getSize());
  }
}

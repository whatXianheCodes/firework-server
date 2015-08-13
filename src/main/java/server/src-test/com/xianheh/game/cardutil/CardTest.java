package com.xianheh.game.cardutil;

import org.junit.Assert;
import org.junit.Test;
import com.xianheh.game.cardutil.Card.CardColor;

/**
 * @author xianhehuang@gmail.com
 */
public class CardTest {

  @Test
  public void testGetCardColor() {
    Card card = new Card(1, CardColor.BLUE);
    Assert.assertEquals(CardColor.BLUE, card.getCardColor());
  }
}

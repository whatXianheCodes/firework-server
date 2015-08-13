package com.xianheh.game.cardutil.gamesetup;

import com.xianheh.game.cardutil.Card;
import com.xianheh.game.cardutil.Hand;
import com.xianheh.game.exception.PlayException;
import com.xianheh.game.gamesetup.Table;
import com.xianheh.game.setting.GameConfigurable;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xianhehuang@gmail.com
 */
public class TableTest {
  @Test
  public void testInsertCard() throws PlayException {
    Table table = new Table();
    Assert.assertEquals(50 - GameConfigurable.MAX_PLAYERS * Hand.MAX_HAND, table.getDeck().getSize());
    Card card = table.getHand(0).getCard(0);
    table.insertCard(card, 0);
    for (int i = 0; i < Hand.MAX_HAND; i++) {
      if (table.getHand(0).getCard(i).getCardId() == card.getCardId()) {
        Assert.fail();
      }
    }
    Assert.assertEquals(card.getValue(), table.getCardValue(card.getCardColor()));
  }

  @Test
  public void testInvalidInsertCard() {
    Table table = new Table();
    Assert.assertEquals(50 - GameConfigurable.MAX_PLAYERS * Hand.MAX_HAND, table.getDeck().getSize());
    Card card = table.getHand(1).getCard(0);

    try {
      table.insertCard(card, 0);
      Assert.fail();
    } catch (PlayException e) {
    }
  }

  @Test
  public void testTurnIncrement() {
    Table table = new Table();
    Assert.assertEquals(50 - GameConfigurable.MAX_PLAYERS * Hand.MAX_HAND, table.getDeck().getSize());
    Assert.assertEquals(GameConfigurable.MAX_HINT, table.getHints());
    Assert.assertEquals(GameConfigurable.MAX_LIVES, table.getLives());
    Assert.assertEquals(GameConfigurable.MAX_PLAYERS, table.getTurnsLeft());

    for (int i = 0; i < GameConfigurable.MAX_PLAYERS; i++) {
      Assert.assertEquals(i, table.getTurn());
      table.advanceTurn();
    }
    Assert.assertEquals(0, table.getTurn());
  }

  @Test
  public void testGameOver() {
    Table table = new Table();
    Assert.assertEquals(50 - GameConfigurable.MAX_PLAYERS * Hand.MAX_HAND, table.getDeck().getSize());
    Assert.assertEquals(GameConfigurable.MAX_HINT, table.getHints());
    Assert.assertEquals(GameConfigurable.MAX_LIVES, table.getLives());
    Assert.assertEquals(GameConfigurable.MAX_PLAYERS, table.getTurnsLeft());

    for (int i = 0; i < GameConfigurable.MAX_LIVES; i++) {
      Assert.assertEquals(GameConfigurable.MAX_PLAYERS, table.getTurnsLeft());
      table.decrementLives();
      table.advanceTurn();
    }

    for (int i = 1; i < GameConfigurable.MAX_LIVES; i++) {
      Assert.assertEquals(GameConfigurable.MAX_PLAYERS - i, table.getTurnsLeft());
      table.advanceTurn();
    }
  }
}

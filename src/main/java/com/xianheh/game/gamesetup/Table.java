package com.xianheh.game.gamesetup;

import com.google.common.collect.Maps;
import com.xianheh.game.cardutil.Card;
import com.xianheh.game.cardutil.Card.CardColor;
import com.xianheh.game.cardutil.Deck;
import com.xianheh.game.cardutil.Hand;
import com.xianheh.game.exception.PlayException;
import com.xianheh.game.response.ResponseCode;
import com.xianheh.game.setting.GameConfigurable;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author xianhehuang@gmail.com
 */
public class Table {

  private static Logger LOGGER = Logger.getLogger(Table.class.getName());

  private Hand[] hands;
  private Map<Card.CardColor, Card> table;
  private Deck deck;
  private int lives;
  private int hints;
  private int playerID;
  private int turn;
  private int turnsLeft;

  public Table () {
    turn = 0;
    turnsLeft = GameConfigurable.MAX_PLAYERS;
    lives = 3;
    hints = GameConfigurable.MAX_HINT;
    playerID = 0;
    deck = new Deck();
    hands = new Hand[GameConfigurable.MAX_PLAYERS];
    table = Maps.newHashMap();

    for (int playerRef = 0; playerRef < GameConfigurable.MAX_PLAYERS; playerRef++) {
      hands[playerRef] = new Hand(deck, playerRef);
      LOGGER.info(playerRef + "th player");
      hands[playerRef].printHand();
    }
    LOGGER.info(deck.getDeckSize() + "cards left");
  }

  public Hand[] getHands() {
    return hands;
  }

  public Map<Card.CardColor, Card> getTable() {
    return table;
  }

  public Deck getDeck() {
    return deck;
  }

  public void insertCard(Card card, int playerId) throws PlayException {
    if (!hands[playerId].replace(card, getDeck())) {
      throw new PlayException(ResponseCode.ErrorResponse.INVALID_MOVE);
    }
    table.put(card.getCardColor(), card);
  }

  public int getCardValue(CardColor cardColor){
    return table.get(cardColor).getValue();
  }

  public Card getCard(Card.CardColor cardColor) {
    return table.get(cardColor);
  }

  public int getLives() {
    return lives;
  }

  public void decrementLives() {
    lives--;
  }

  public int getHints() {
    return hints;
  }

  public void incrementHints() {
    hints++;
  }

  public int getPlayerID() {
    return playerID;
  }

  public void incrementPlayerID() {
    playerID++;
  }

  public int getTurn() {
    return turn;
  }

  public void incrementTurn() {
    turn++;
  }

  public void advanceTurn() {
    turn = (turn + 1) % GameConfigurable.MAX_PLAYERS;
    if (lives == 0) {
      if (turnsLeft == 0) {
        LOGGER.info("GAME OVER");
      }
      else {
        turnsLeft--;
      }
    }
  }
}

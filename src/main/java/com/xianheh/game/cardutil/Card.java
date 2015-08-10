package com.xianheh.game.cardutil;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.annotations.SerializedName;
import com.xianheh.game.gamesetup.Table;

/**
 * @author xianhehuang@gmail.com
 */
public class Card {
  private static final int DECK_SIZE = 50;
  private int value;
  @SerializedName("card_color")
  private CardColor cardColor;
  @SerializedName("card_id")
  int cardId;

  private static final BiMap<CardColor, String> cardColorToRaw =
      new ImmutableBiMap.Builder<CardColor, String>()
          .put(CardColor.BLUE, "BLUE")
          .put(CardColor.YELLOW, "YELLOW")
          .put(CardColor.RED, "RED")
          .put(CardColor.GREEN, "GREEN")
          .put(CardColor.WHITE, "WHITE")
          .build();

  public enum CardColor {
    BLUE,
    YELLOW,
    RED,
    GREEN,
    WHITE
  }

  public static CardColor setCardColor(String cardColor) {
    return cardColorToRaw.inverse().get(cardColor);
  }

  public Card (int value, CardColor cardColor, int cardId) {
    Preconditions.checkArgument(cardId < DECK_SIZE);
    this.value = value;
    this.cardColor = cardColor;
    this.cardId = cardId;
  }

  public int getValue () {
    return this.value;
  }

  public CardColor getCardColor() {
    return this.cardColor;
  }

  public int getCardId() {
    return this.cardId;
  }

  public static boolean isCardPlayable(Card cardPlayed, Table table) {
    return cardPlayed.getValue() - table.getCard(cardPlayed.getCardColor()).getValue() == 1;
  }
}

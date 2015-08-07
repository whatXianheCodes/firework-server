import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.annotations.SerializedName;

/**
 * @author xianhehuang@gmail.com
 */
public class Card {
  private static final int DECK_SIZE = 50;
  private CardValue value;
  @SerializedName("card_color")
  private CardColor cardColor;
  @SerializedName("card_id")
  int cardId;

  private static final BiMap<CardValue, String> cardValueToRaw =
      new ImmutableBiMap.Builder<CardValue, String>()
          .put(CardValue.ONE, "1")
          .put(CardValue.TWO, "2")
          .put(CardValue.THREE, "3")
          .put(CardValue.FOUR, "4")
          .put(CardValue.FIVE, "5")
          .build();

  public enum CardValue {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE
  }

  public static CardValue setCardValue(String cardValue) {
    return cardValueToRaw.inverse().get(cardValue);
  }

  public static int getCardValue(CardValue cardValue) {
    return Integer.parseInt(cardValueToRaw.get(cardValue));
  }

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

  public Card (CardValue value, CardColor cardColor, int cardId) {
    Preconditions.checkArgument(cardId < DECK_SIZE);
    this.value = value;
    this.cardColor = cardColor;
    this.cardId = cardId;
  }

  public CardValue getValue () {
    return this.value;
  }

  public CardColor getCardColor() {
    return this.cardColor;
  }

  public int getCardId() {
    return this.cardId;
  }
}

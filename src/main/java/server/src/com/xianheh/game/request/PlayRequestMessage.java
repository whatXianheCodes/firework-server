package com.xianheh.game.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author xianhehuang@gmail.com
 */
public class PlayRequestMessage {
  private String action;
  @SerializedName("player_id")
  private String playerId;
  @SerializedName("hint_color")
  private String hintColor;
  @SerializedName("hint_number")
  private String hintNumber;
  @SerializedName("hint_player_id")
  private String hintPlayerId;
  @SerializedName("card_id")
  private int cardPlayed;
  private String move;

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getHintColor() {
    return hintColor;
  }

  public void setHintColor(String hintColor) {
    this.hintColor = hintColor;
  }

  public String getHintNumber() {
    return hintNumber;
  }

  public void setHintNumber(String hintNumber) {
    this.hintNumber = hintNumber;
  }

  public String getHintPlayerId() {
    return hintPlayerId;
  }

  public void setHintPlayerId(String hintPlayerId) {
    this.hintPlayerId = hintPlayerId;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getMove() {
    return move;
  }

  public void setMove(String move) {
    this.move = move;
  }

  public int getCardPlayed() {
    return cardPlayed;
  }

  public void setCardPlayed(int cardPlayed) {
    this.cardPlayed = cardPlayed;
  }
}

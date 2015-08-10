package com.xianheh.game.response;

import com.google.gson.annotations.SerializedName;
import com.xianheh.game.cardutil.Hand;

/**
 * @author xianhehuang@gmail.com
 */
public class PlayResponse {
  private String action;
  private boolean success;
  @SerializedName("error_message")
  private String errorMessage;
  @SerializedName("player_id")
  private int playerId;
  @SerializedName("hints_left")
  private int hintsLeft;
  @SerializedName("lives_left")
  private int livesLeft;
  private Hand[] hands;
  private int turn;

  public PlayResponse(String action, int playerId, int hintsLeft, int livesLeft,
      Hand[] hands, int turn) {
    this.action = action;
    success = true;
    errorMessage = "";
    this.playerId = playerId;
    this.hintsLeft = hintsLeft;
    this.livesLeft = livesLeft;
    this.hands = hands;
    this.turn = turn;
  }

  public PlayResponse(boolean success, String errorMessage) {
    this.success = success;
    this.errorMessage = errorMessage;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public int getPlayerId() {
    return playerId;
  }

  public void setPlayerId(int playerId) {
    this.playerId = playerId;
  }

  public int getHintsLeft() {
    return hintsLeft;
  }

  public void setHintsLeft(int hintsLeft) {
    this.hintsLeft = hintsLeft;
  }

  public int getLivesLeft() {
    return livesLeft;
  }

  public void setLivesLeft(int livesLeft) {
    this.livesLeft = livesLeft;
  }

  public Hand[] getHands() {
    return hands;
  }

  public void setHands(Hand[] hands) {
    this.hands = hands;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }
}

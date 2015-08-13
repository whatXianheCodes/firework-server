package com.xianheh.game.request;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.xianheh.game.cardutil.Card;
import com.xianheh.game.cardutil.Hand;
import com.xianheh.game.exception.PlayException;
import com.xianheh.game.gamesetup.Table;
import com.xianheh.game.response.PlayResponse;
import com.xianheh.game.response.ResponseCode.ErrorResponse;
import com.xianheh.game.setting.GameConfigurable;

/**
 * @author xianhehuang@gmail.com
 */
public class PlayRequest {

  public static String parseJSONMessage(String message, Table table) throws PlayException {
    Gson gson = new Gson();
    PlayRequestMessage request = gson.fromJson(message, PlayRequestMessage.class);
    PlayResponse response;

    switch(request.getAction()) {
      case Action.GET_HAND:
        response = getHand(checkPlayerId(request.getPlayerId()), table);
        break;
      case Action.GET_ID:
        response = returnId(table);
        break;
      case Action.GIVE_HINT:
        table.addMove(request);
        response = giveHint(table, checkPlayerId(request.getPlayerId()), request.getHintColor(),
            request.getHintNumber());
        break;
      case Action.PLAY:
        table.addMove(request);
        response = playCard(checkPlayerId(request.getPlayerId()), request.getCardPlayed(), table);
        break;
      case Action.DISCARD:
        table.addMove(request);
        response = discardCard(checkPlayerId(request.getPlayerId()), request.getCardPlayed(), table);
        break;
      case Action.GET_MOVE_SINCE:
        if (Strings.isNullOrEmpty(request.getMove())) {
          throw new PlayException(ErrorResponse.INVALID_REQUEST);
        }
        return gson.toJson(table.getMove(Integer.parseInt(request.getMove())));
      default:
        throw new PlayException(ErrorResponse.INVALID_REQUEST);
    }
    return gson.toJson(response);
  }

  public static PlayResponse getHand(int playerId, Table table) throws PlayException {
    return new PlayResponse(Action.GET_STATE, playerId, table.getHints(),
        table.getLives(), getVisibleHand(playerId, table), table.getTurn());
  }

  public static PlayResponse returnId(Table table) {
    PlayResponse response = new PlayResponse(Action.GET_ID, table.getPlayerID(), table.getHints(), table.getLives(),
    null, table.getTurn());
    table.incrementPlayerID();
    return response;
  }

  public static PlayResponse giveHint(Table table, int playerId, String hintColor, String hintNumber)
      throws PlayException {
    if (Strings.isNullOrEmpty(hintColor) && Strings.isNullOrEmpty(hintNumber)) {
      throw new PlayException(ErrorResponse.INVALID_REQUEST);
    }
//    for (int cardRef = 0; cardRef < Hand.MAX_HAND; cardRef++) {
//      Card card = table.getHand(playerId).getCard(cardRef);
//      if (!Strings.isNullOrEmpty(hintColor) && card.getCardColor().equals(Card.getCardColor(hintColor))) {
//
//      } else if (!Strings.isNullOrEmpty(hintNumber) && card.getValue() == Integer.parseInt(hintNumber)) {
//
//      }
//
//    }
    return null;
  }

  public static PlayResponse playCard(int playerId, int cardId, Table table) throws PlayException {
    Card cardPlayed = null;
    for (int cardRef = 0; cardRef < Hand.MAX_HAND; cardRef++) {
      if (table.getHand(playerId).getCard(cardRef).getCardId() == cardId) {
        cardPlayed = table.getHand(playerId).getCard(cardRef);
        break;
      }
    }
    if (cardPlayed == null) {
      throw new PlayException(ErrorResponse.INVALID_REQUEST);
    }
    if (!table.getTable().containsKey(cardPlayed.getCardColor())) {
      if (cardPlayed.getValue() == 1) {
        table.insertCard(cardPlayed, playerId);
      } else {
        throw new PlayException(ErrorResponse.INVALID_MOVE);
      }
    } else if (!Card.isCardPlayable(cardPlayed, table)) {
      throw new PlayException(ErrorResponse.INVALID_MOVE);
    } else {
      table.insertCard(cardPlayed, playerId);
      if (table.getCardValue(cardPlayed.getCardColor()) == 5 &&
          table.getHints() < GameConfigurable.MAX_HINT){
        table.incrementHints();
      }
    }
    table.advanceTurn();
    return new PlayResponse(Action.PLAY, playerId, table.getHints(), table.getLives(), getVisibleHand(playerId, table),
        table.getTurn());
  }

  public static PlayResponse discardCard(int playerId, int cardId, Table table) throws PlayException {
    Card cardPlayed = null;
    for (int cardRef = 0; cardRef < Hand.MAX_HAND; cardRef++) {
      if (table.getHand(playerId).getCard(cardRef).getCardId() == cardId) {
        cardPlayed = table.getHand(playerId).getCard(cardRef);
        break;
      }
    }
    if (!table.getHand(playerId).replace(cardPlayed, table.getDeck())) {
      throw new PlayException(ErrorResponse.INVALID_MOVE);
    } else if (table.getHints() < GameConfigurable.MAX_HINT) {
      table.incrementHints();
    }
    table.advanceTurn();
    return new PlayResponse(Action.DISCARD,  playerId, table.getHints(), table.getLives(),
        getVisibleHand(playerId, table), table.getTurn());
  }

  private static int checkPlayerId(String playerIdString) throws PlayException {
    int playerId;
    if (Strings.isNullOrEmpty(playerIdString)) {
      throw new PlayException(ErrorResponse.INVALID_REQUEST);
    }
    playerId = Integer.parseInt(playerIdString);
    if (playerId >= GameConfigurable.MAX_PLAYERS) {
      throw new PlayException(ErrorResponse.INVALID_PLAYER_ID);
    }
    return playerId;
  }

  private static Hand[] getVisibleHand(int playerId, Table table) {
    Hand[] visibleHand = new Hand[GameConfigurable.MAX_PLAYERS-1];
    int currentIndex = 0;
    for (int playerRef = 0; playerRef < GameConfigurable.MAX_PLAYERS; playerRef++) {
      if (playerRef != playerId) {
        visibleHand[currentIndex] = table.getHand(playerRef);
        currentIndex++;
      }
    }
    return visibleHand;
  }
}

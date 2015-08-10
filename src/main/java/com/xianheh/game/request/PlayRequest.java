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
 * @author xianhe@index.com
 */
public class PlayRequest {

  public static PlayResponse parseJSONMessage(String message, Table table) throws PlayException {
    Gson gson = new Gson();
    PlayRequestMessage request = gson.fromJson(message, PlayRequestMessage.class);

    switch(request.getAction()) {
      case Action.GET_HAND:
        return startGame(checkPlayerId(request.getPlayerId()), table);
      case Action.GET_ID:
        return returnId(table);
//      case GIVE_HINT:
//        giveHint();
//        break;
      case Action.PLAY:
        return playCard(checkPlayerId(request.getPlayerId()), request.getCardPlayed(), table);
      case Action.DISCARD:
        return discardCard(checkPlayerId(request.getPlayerId()), request.getCardPlayed(), table);
      default:
        throw new PlayException(ErrorResponse.INVALID_REQUEST);
    }
  }

  public static PlayResponse startGame(int playerId, Table table) throws PlayException {
    return new PlayResponse(Action.GET_STATE, playerId, table.getHints(),
        table.getLives(), getVisibleHand(playerId, table), table.getTurn());
  }

  public static PlayResponse returnId(Table table) {
    PlayResponse response = new PlayResponse(Action.GET_ID, table.getPlayerID(), table.getHints(), table.getLives(),
    null, table.getTurn());
    table.incrementPlayerID();
    return response;
  }

  public static PlayResponse playCard(int playerId, Card cardPlayed, Table table) throws PlayException {
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
    table.incrementTurn();
    return new PlayResponse(Action.PLAY, playerId, table.getHints(), table.getLives(), getVisibleHand(playerId, table),
        table.getTurn());
  }

  public static PlayResponse discardCard(int playerId, Card cardPlayed, Table table) throws PlayException {
    if (!table.getHands()[playerId].replace(cardPlayed, table.getDeck())) {
      throw new PlayException(ErrorResponse.INVALID_MOVE);
    } else if (table.getHints() < GameConfigurable.MAX_HINT) {
      table.incrementHints();
    }
    table.incrementTurn();
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
    Hand[] visbleHand = new Hand[GameConfigurable.MAX_PLAYERS-1];
    int currentIndex = 0;
    for (int playerRef = 0; playerRef < GameConfigurable.MAX_PLAYERS; playerRef++) {
      if (playerRef != playerId) {
        visbleHand[currentIndex] = table.getHands()[playerRef];
        currentIndex++;
      }
    }
    return visbleHand;
  }
}

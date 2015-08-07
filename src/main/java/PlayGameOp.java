import com.google.common.collect.Maps;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This is a temporary functionality of draw card. Will implement with other object once the basic
 * functionality is done
 *
 * @author xianhehuang@gmail.com
 */

@WebServlet("/play")
public class PlayGameOp extends HttpServlet {

  private final static String INVALID_REQUEST = "INVALID_REQUEST";
  private final static String INVALID_MOVE = "INVALID_MOVE";
  private final static String INVALID_PLAYER_ID = "INVALID_PLAYER_ID";
  private final static String START_GAME = "START_GAME";
  private final static String GET_ID = "GET_ID";
  private final static String GIVE_HINT = "GIVE_HINT";
  private final static String PLAY = "PLAY";
  private final static String DISCARD = "DISCARD";
  private final static int MAX_HINT = 8;
  private static Logger LOGGER = Logger.getLogger(PlayGameOp.class.getName());
  private static final int MAX_PLAYERS = 5;

  private Deck mDeck;
  private boolean success;
  private String errorMessage;
  private int turn;
  private int turnsLeft;
  private Hand[] hands;
  private int lives;
  private int hints;
  private Map<Card.CardColor, Card> table;
  private int playerID = 0;

  public void init() {
    mDeck = new Deck();
    success = true;
    turn = 0;
    turnsLeft = MAX_PLAYERS;
    lives = 3;
    hints = MAX_HINT;
    setupTable();
  }

  // TODO: change to doPost after debugging
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    String responseMessage = processRequestJSON(request);
    sendResponse(response, responseMessage);
    advanceTurn();
  }

  private void setupTable() {
    hands = new Hand[MAX_PLAYERS];
    table = Maps.newHashMap();
    for (int playerRef = 0; playerRef < MAX_PLAYERS; playerRef++) {
      hands[playerRef] = new Hand(mDeck, playerRef);
      LOGGER.info(playerRef + "th player");
      hands[playerRef].printHand();
    }
    LOGGER.info(mDeck.getDeckSize() + "cards left");
  }

  private String processRequestJSON(HttpServletRequest request) throws IOException, ServletException {
    StringBuffer requestString = new StringBuffer();
    String line;
    try {
      BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null)
        requestString.append(line);
    } catch (Exception e) {
      setErrorMessage(INVALID_REQUEST);
    }
    return parseJSONMessage(requestString.toString());
  }

  private String parseJSONMessage(String message) {
    Gson gson = new Gson();
    PlayRequest request = gson.fromJson(message, PlayRequest.class);

    switch(request.getAction()) {
      case START_GAME:
        return startGame(Integer.parseInt(request.getPlayerId()));
      case GET_ID:
        return returnId();
//      case GIVE_HINT:
//        giveHint();
//        break;
      case PLAY:
        return playCard(Integer.parseInt(request.getPlayerId()), request.getCardPlayed());
      case DISCARD:
        return discardCard(Integer.parseInt(request.getPlayerId()), request.getCardPlayed());
      default:
        PlayResponse response = new PlayResponse("GENERIC_ERROR", success, errorMessage, playerID, hints, lives,
            null, turn);
        return gson.toJson(response);
    }
  }

  private String startGame(int playerId) {
    Gson gson = new Gson();
    if (playerId >= MAX_PLAYERS) {
      setErrorMessage(INVALID_PLAYER_ID);
    }
    PlayResponse response = new PlayResponse(START_GAME, success, errorMessage, playerId, hints, lives,
        getVisibleHand(playerId), turn);
    return gson.toJson(response);
  }

  private String returnId() {
    Gson gson = new Gson();
    PlayResponse response = new PlayResponse(GET_ID, success, errorMessage, playerID, hints, lives,
        null, turn);
    playerID++;
    return gson.toJson(response);
  }

  private boolean isCardPlayable(Card cardPlayed) {
    return Card.getCardValue(cardPlayed.getValue()) - Card.getCardValue(table.get(cardPlayed.getCardColor()).getValue())
        == 1;
  }

  private String playCard(int playerId, Card cardPlayed) {
    Gson gson = new Gson();
    if (playerId >= MAX_PLAYERS) {
      setErrorMessage(INVALID_PLAYER_ID);
    } else if (!table.containsKey(cardPlayed.getCardColor()) && cardPlayed.getValue() != Card.CardValue.ONE) {
      setErrorMessage(INVALID_MOVE);
    } else if (!isCardPlayable(cardPlayed)) {
      setErrorMessage(INVALID_MOVE);
    } else if (!hands[playerId].replace(cardPlayed, mDeck)) {
      setErrorMessage(INVALID_MOVE);
    } else {
      table.put(cardPlayed.getCardColor(), cardPlayed);
      if (table.get(cardPlayed.getCardColor()).getValue() == Card.CardValue.FIVE){
        hints++;
      }
    }
    PlayResponse response = new PlayResponse(START_GAME, success, errorMessage, playerId, hints, lives,
        getVisibleHand(playerId), turn);
    return gson.toJson(response);
  }

  private String discardCard(int playerId, Card cardPlayed) {
    Gson gson = new Gson();
    if (playerId >= MAX_PLAYERS) {
      setErrorMessage(INVALID_PLAYER_ID);
    } else if (!hands[playerId].replace(cardPlayed, mDeck)) {
      setErrorMessage(INVALID_MOVE);
    } else if (hints < MAX_HINT) {
      hints++;
    }
    PlayResponse response = new PlayResponse(START_GAME, success, errorMessage, playerId, hints, lives,
        getVisibleHand(playerId), turn);
    return gson.toJson(response);
  }

  private Hand[] getVisibleHand(int playerId) {
    Hand[] visbleHand = new Hand[MAX_PLAYERS-1];
    int currentIndex = 0;
    for (int playerRef = 0; playerRef < MAX_PLAYERS; playerRef++) {
      if (playerRef != playerId) {
        visbleHand[currentIndex] = hands[playerRef];
        currentIndex++;
      }
    }
    return visbleHand;
  }

  private void setErrorMessage(String message) {
    success = false;
    errorMessage = message;
  }

  private void advanceTurn() {
    turn = (turn + 1) % MAX_PLAYERS;
    if (lives == 0) {
      if (turnsLeft == 0) {
        LOGGER.info("GAME OVER");
      }
      else {
        turnsLeft--;
      }
    }
  }

  private void sendResponse(HttpServletResponse response, String responseMessage) throws IOException {
    response.getWriter().write(responseMessage);
    if (success) {
      response.setStatus(response.SC_OK);
    } else {
      response.setStatus(response.SC_FORBIDDEN);
    }
  }
}

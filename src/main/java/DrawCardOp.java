import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This is a temporary functionality of draw card. Will implement with other object once the basic
 * functionality is done
 *
 * @author xianhehuang@gmail.com
 */

@WebServlet("/draw_card")
public class DrawCardOp extends HttpServlet {

  private final static String INVALID_MOVE = "INVALID_MOVE";
  private static Logger LOGGER = Logger.getLogger(DrawCardOp.class.getName());
  private static final int MAX_PLAYERS = 5;
  private Deck mDeck;
  private boolean success;
  private String errorMessage;
  private int[] cards;
  private int turn;
  private int turnsLeft;

  public void init() {
    // Reset hit counter.
    mDeck = new Deck();
    success = true;
    turn = 0;
    turnsLeft = Integer.MAX_VALUE;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    processRequest(request);
    drawCard();
    advanceTurn();
    sendResponse(response);
  }

  private void advanceTurn() {
    turn = (turn + 1) % MAX_PLAYERS;
    turnsLeft--;
    LOGGER.info("turn: " + turn);
    LOGGER.info("turns left: " + turnsLeft);
    //TODO: end game
//        if (turnsLeft == 0) {
//            endGame();
//        }
  }

  private void processRequest(HttpServletRequest request) {
    // TODO: get id from http request
    int playerId = 0;

    success = true;
    errorMessage = "";

    if (turn != playerId) {
      success = false;
      errorMessage = INVALID_MOVE;
    }
  }

  private void drawCard() {
    if (MAX_PLAYERS < 4) {
      cards = new int[]{
          mDeck.pop(),
          mDeck.pop(),
          mDeck.pop(),
          mDeck.pop(),
          mDeck.pop(),
      };
    } else {
      cards = new int[]{
          mDeck.pop(),
          mDeck.pop(),
          mDeck.pop(),
          mDeck.pop(),
      };
    }
  }

  private void sendResponse(HttpServletResponse response) throws IOException {
    JSONObject drawCardResultJSON = new JSONObject();
    JSONObject resultJSON = new JSONObject();
    resultJSON.put("success", success);
    resultJSON.put("cards", cards);
    resultJSON.put("error_message", errorMessage);
    drawCardResultJSON.put("draw_card_response", resultJSON);
    response.setContentType("application/json");
    response.getWriter().write(drawCardResultJSON.toString());
    if (success) {
      response.setStatus(response.SC_OK);
    } else {
      response.setStatus(response.SC_FORBIDDEN);
    }
  }
}

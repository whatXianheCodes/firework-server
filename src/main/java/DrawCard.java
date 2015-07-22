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
 * @author Xianhe Huang
 */

@WebServlet("/draw_card")
public class DrawCard extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(DrawCard.class.getName());
    private final static String INVALID_MOVE = "INVALID_MOVE";
    private static int turn = 0;
    private final static int mPlayers = 5;
    private final static boolean mLog = false;
    Deck mDeck;

    public DrawCard() {
        mDeck = new Deck();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO: get id from http request
        int playerId = 0;

        boolean success = true;
        String errorMessage = "";

        if (turn != playerId) {
            success = false;
            errorMessage = INVALID_MOVE;
        }
        int[] cards;
        if (mPlayers < 4) {
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

        if (mLog) {
            StringBuilder sb = new StringBuilder();
            for (int c : cards) {
                sb.append(CardUtils.getCardName(c));
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);

            LOGGER.info(String.format("Player %d drew %s.", playerId, sb.toString()));
        }

        advanceTurn();

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
        }
        else {
            response.setStatus(response.SC_FORBIDDEN);
        }
    }

    private void advanceTurn() {
        turn = (turn + 1) % mPlayers;
        turn--;
        //TODO: end game
//        if (mTurnsLeft == 0) {
//            endGame();
//        }
    }
}

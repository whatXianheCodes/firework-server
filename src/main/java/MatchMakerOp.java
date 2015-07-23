import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xianhehuang@gmail.com
 */
@WebServlet("/create_table")
public class MatchMakerOp extends HttpServlet {

  private final static Logger LOGGER = Logger.getLogger(MatchMakerOp.class.getName());

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    int playerId = 1;
    Player player = new Player(playerId);
    String tableName = Lobby.addTable(player);
    LOGGER.info("Number of player: " + Lobby.playerCount());
    response.setContentLength(tableName.length());
    response.setContentType(tableName);

    OutputStream output = response.getOutputStream();
    output.write(tableName.getBytes());
    output.flush();
    output.close();
  }
}
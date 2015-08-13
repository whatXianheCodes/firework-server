package com.xianheh.game.ops;

import com.google.gson.Gson;
import com.xianheh.game.exception.PlayException;
import com.xianheh.game.gamesetup.Table;
import com.xianheh.game.request.PlayRequest;
import com.xianheh.game.response.PlayResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * This is a temporary functionality of draw card. Will implement with other object once the basic
 * functionality is done
 *
 * @author xianhehuang@gmail.com
 */

@WebServlet("/play")
public class PlayGameOp extends HttpServlet {

  private Table table;
  public void init() {
    table = new Table();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    String responseMessage = null;
    try {
      responseMessage = processRequest(request);
    } catch (PlayException e) {
      PlayResponse errorPlayResponse = new PlayResponse(false, e.getMessage());
      sendError(response, errorPlayResponse);
    }
    sendSuccess(response, responseMessage);
    table.advanceTurn();
  }

  private String processRequest(HttpServletRequest request) throws IOException, ServletException, PlayException {
    StringBuffer requestStringBuilder = new StringBuffer();
    String line;
    BufferedReader reader = request.getReader();
    while ((line = reader.readLine()) != null){
      requestStringBuilder.append(line);
    }
    return PlayRequest.parseJSONMessage(requestStringBuilder.toString(), table);
  }

  private void sendSuccess(HttpServletResponse response, String responseMessage) throws IOException {
    response.getWriter().write(responseMessage);
      response.setStatus(response.SC_OK);
  }

  private void sendError(HttpServletResponse response, PlayResponse responseMessage) throws IOException {
    Gson gson = new Gson();
    response.getWriter().write(gson.toJson(responseMessage));
    response.setStatus(response.SC_FORBIDDEN);
  }
}

package com.xianheh.game.gamesetup;

import com.google.common.collect.Lists;
import com.xianheh.game.cardutil.Deck;

import java.util.List;

/**
 * @author xianhehuang@gmail.com
 */
public class TableHelper {
  List<Player> players;
  Deck deck;
  Player host;
  int turn;
  private static final int MAX_TABLE_SIZE = 4;

  public TableHelper(Player host) {
    players = Lists.newArrayList();
    deck = new Deck();
    this.host = host;
    players.add(host);
    turn = 0;
  }

  public boolean addPlayer(Player player) {
    if (isAvailable()) {
      players.add(player);
      return true;
    }
    return false;
  }

  public boolean validTurn(Player player) {
    return (player == players.get(turn));
  }

  public boolean isAvailable() {
    return players.size() < MAX_TABLE_SIZE;
  }
}

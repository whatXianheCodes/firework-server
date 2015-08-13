package com.xianheh.game.gamesetup;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;


/**
 * @author xianhehuang@gmail.com
 */
public class Lobby {
  private static Map<UUID, TableHelper> lobbyTable = Maps.newHashMap();

  public static String addTable(Player currentPlayer) {
    UUID name = UUID.randomUUID();
    lobbyTable.put(name, new TableHelper(currentPlayer));
    return name.toString();
  }

  public static boolean joinTable(String id, Player currentPlayer) {
    UUID uuid = UUID.fromString(id);
    Preconditions.checkArgument(lobbyTable.containsKey(uuid));
    return lobbyTable.get(uuid).addPlayer(currentPlayer);
  }

  public static int playerCount() {
    return lobbyTable.size();
  }

  // TODO: better way to find for a match
  public static String findMatch(Player currentPlayer) {
    for (Map.Entry<UUID, TableHelper> tableEntry : lobbyTable.entrySet()) {
      if (tableEntry.getValue().isAvailable()) {
        return tableEntry.getKey().toString();
      }
    }
    return addTable(currentPlayer);
  }

  //TODO: may need more stuff
  public static void closeLobby() {
    lobbyTable.clear();
  }
}

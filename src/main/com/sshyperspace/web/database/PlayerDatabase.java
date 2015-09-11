package com.sshyperspace.web.database;

import com.sshyperspace.web.internal.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface PlayerDatabase
{
  /** Finds a player by their name
   *
   * @param name the name of the player, case insensitive
   * @return the player, null if not found */
  @Nullable Player findPlayerByName(String name) throws SQLException;

  /** Enumerates best <code>nPlayers</code> in database by a specified integer property
   *
   * @param nPlayers the number of players to return
   * @param property the property to sort by
   * @return the players in descending order of wealth
   */
  List<Player> getTopPlayerList(int nPlayers, Player.Property property) throws SQLException;
}

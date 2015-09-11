package com.sshyperspace.web.database;

import com.sshyperspace.web.internal.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SqlPlayerDatabase implements PlayerDatabase
{
  private final Connection _conn;
  private final PreparedStatement _findPlayerByNameStmt;
  private final PreparedStatement _getTopPlayerStmts[];

  public SqlPlayerDatabase(Connection connection) throws SQLException
  {
    this._conn = connection;
    this._findPlayerByNameStmt = this._conn.prepareStatement("SELECT * FROM hs_players WHERE name=?");

    int nStatements = Player.Property.values().length;
    this._getTopPlayerStmts = new PreparedStatement[nStatements];

    this.initTopStatements(connection);
  }

  private void initTopStatements(Connection connection) throws SQLException
  {
    for (int i = 0; i < this._getTopPlayerStmts.length; ++i)
    {
      String stmt = String.format("SELECT * FROM hs_players ORDER BY %s DESC LIMIT ?", Player.Property.values()[i].columnName);
      this._getTopPlayerStmts[i] = connection.prepareStatement(stmt);
    }
  }

  /**
   * Creates a player from the current row in result set
   *
   * @param set the result set, whose current row MUST be non-empty
   * @return a new player representing the current row at result set
   * @throws SQLException
   */
  private Player loadPlayer(ResultSet set) throws SQLException
  {
    String name = set.getString("name");

    Map<Player.Property, Integer> properties = new HashMap<>();
    for (Player.Property property : Player.Property.values())
      properties.put(property, set.getInt(property.columnName));

    Player player = new Player(name, properties);
    return player;
  }

  @Override
  public @Nullable Player findPlayerByName(String name) throws SQLException
  {
    ResultSet set = this._findPlayerByNameStmt.executeQuery();
    Player player = null;
    if (set.next())
      player = loadPlayer(set);

    set.close();
    return player;
  }

  @Override
  public synchronized List<Player> getTopPlayerList(int nPlayers, Player.Property property) throws SQLException
  {
    int propIndex = property.ordinal();
    this._getTopPlayerStmts[propIndex].setInt(0, nPlayers);

    ResultSet set = this._getTopPlayerStmts[propIndex].executeQuery();
    List<Player> players = new LinkedList<>();
    while (set.next())
      players.add(loadPlayer(set));

    return players;
  }
}

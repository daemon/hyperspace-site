package com.sshyperspace.web.database;

import java.util.Map;

public class Player
{
  public enum Property
  {
    ID("id"),
    MONEY("money"),
    EXPERIENCE("exp"),
    MONEY_GIVE("money_give"),
    MONEY_RECEIVE("money_grant"),
    MONEY_BALL("money_ball"),
    MONEY_KILL("money_kill");

    String columnName;
    Property(String columnName)
    {
      this.columnName = columnName;
    }
  }

  private final String _name;
  private final Map<Property, Integer> _properties;

  public Player(String name, Map<Property, Integer> properties)
  {
    this._name = name;
    this._properties = properties;
  }

  public String getName()
  {
    return this._name;
  }

  public int getProperty(Property property)
  {
    return this._properties.get(property);
  }
}

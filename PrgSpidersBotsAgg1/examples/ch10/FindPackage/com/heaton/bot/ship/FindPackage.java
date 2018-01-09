package com.heaton.bot.ship;
import com.heaton.bot.*;
import com.heaton.bot.catbot.*;

public class FindPackage {
  public static String findPackage(String url,String code,String country)
  {
    try {
      ShipBot ship = new ShipBot(new HTTPSocket());
      ship.setURL(url);
      ship.setCountry(country);
      return ship.lookup(code);
    } catch ( Exception e ) {
      return e.toString();
    }
  }
}
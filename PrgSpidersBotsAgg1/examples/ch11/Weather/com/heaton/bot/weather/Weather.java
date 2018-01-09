package com.heaton.bot.weather;
import com.heaton.bot.*;
import java.io.*;

/**
 * Example from Chapter 11
 * 
 * A simple weather aggregator. This bot will download the 
 * current temp from several different US cities. This 
 * aggregator can either return the list or write HTML
 * to a file.
 */
public class Weather {

  /**
   * The city name
   */
  public String city;

  /**
   * The temperature in degrees Fahrenheit.
   */
  public double deg;

  /**
   * Called to get the current temperature.
   * 
   * @param code A weather code for the city.
   * @return The temperature in degrees fahrenheit.
   */
  public static double getTemp(String code)
  {
    try {
      String url;
      url = "http://weather.noaa.gov/weather/current/";
      url += code.toUpperCase();
      url += ".html";
      HTTPSocket http = new HTTPSocket();
      http.send(url,null);
      int i = http.getBody().indexOf("Temperature")+11;
      while ( !Character.isDigit(http.getBody().charAt(i)) )
        i++;
      String str = http.getBody().substring(i,
                                            http.getBody().indexOf(' ',i) );
      return Double.parseDouble(str);
    } catch ( Exception e ) {
    }
    return 0;
  }

  /**
   * A list of cities to aggregate.
   */
  static String _city[] = {
    "Anchorage, AK|PANC",
    "Atlanta, GA|KATL",
    "Chicago, IL|KCGX",
    "Denver, CO|KDEN",
    "Honolulu, HI|PHNL",
    "Los Angeles|KLAX",
    "New Orleans, LA|KMSY",
    "New York, NY|KNYC",
    "Orlando, FL|KSFB",
    "Phoenix, AZ|KPHX",
    "St. Louis, MO|KSTL",
    "Washington, DC|KDCA"};


  public static Weather[] getList()
  {
    Weather array[] = new Weather[_city.length];
    for ( int i=0;i<_city.length;i++ ) {
      array[i] = new Weather();
      array[i].city = _city[i].substring(0,
                                         _city[i].indexOf("|") );
      array[i].deg = getTemp(
                            _city[i].substring( _city[i].indexOf("|") + 1));
    }
    return array;
  }

  /**
   * Aggregate this list of cities to a file.
   * 
   * @param path Where to write the HTML file.
   */
  public static void fileAggregate(String path)
  {
    try {
      FileOutputStream fw = new FileOutputStream(path,true);
      PrintStream ps = new PrintStream(fw);
      ps.println("<html><head><title>Current Weather</title></head>");
      ps.println("<body>");
      ps.println("<h1>Current Weather</h1><table border=0>");
      Weather wx[] = getList();
      for ( int i=0;i<wx.length;i++ ) {
        ps.println("<tr><td>" + wx[i].city + "</td><td>" 
                   + wx[i].deg + "</td></tr>");
      }
      ps.println("</table></body></html>");
      ps.close();
      fw.close();    
    } catch ( Exception e ) {
    }

  }
}
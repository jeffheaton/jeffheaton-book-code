package com.heaton.bot;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * The bot exclusion class is used to read and
 * process a robots.txt file from a web site.
 * Using this file a bot can make sure it is
 * obeying this public policy file.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class BotExclusion {

  /**
   * The full URL of the robots.txt file.
   */
  protected String _robotFile;

  /**
   * A list of full URL's to exclude.
   */
  protected Vector _exclude = new Vector();
  /**
   * @param http A HTTP object to use.
   * @param url A URL from the webster to load the robots.txt file from.
   */

  public void load(HTTP http,String url)
  throws MalformedURLException,
  UnknownHostException,
  java.io.IOException
  {
    String str;
    boolean active = false;

    URL u = new URL(url);
    URL u2 = new URL(
                    u.getProtocol(),
                    u.getHost(),
                    u.getPort(),
                    "/robots.txt");
    _robotFile = u2.toString();
    http.send(_robotFile,null);

    StringReader sr = new StringReader(http.getBody());
    BufferedReader r = new BufferedReader(sr);
    while ( (str=r.readLine()) != null ) {
      str = str.trim();
      if ( str.length()<1 )
        continue;
      if ( str.charAt(0)=='#' )
        continue;
      int i = str.indexOf(':');
      if ( i==-1 )
        continue;
      String command = str.substring(0,i);
      String rest = str.substring(i+1).trim();
      if ( command.equalsIgnoreCase("User-agent") ) {
        active = false;
        if ( rest.equals("*") )
          active = true;
        else {
          if ( rest.equalsIgnoreCase(http.getAgent()) )
            active = true;
        }
      }
      if ( active ) {
        if ( command.equalsIgnoreCase("disallow") ) {
          URL u3 = new URL(new URL(_robotFile),rest);
          if ( !isExcluded(u3.toString()) )
            _exclude.addElement(u3.toString());
        }
      }
    }
  }

  /**
   * This is the main worker method for this class.
   * This method can be called to determine if the
   * specified URL should be excluded.
   *
   * @param url The URL to be checked.
   * @return Returns true if the specified URL is to be excluded.
   * Returns false if not.
   */
  public boolean isExcluded(String url)
  {
    for ( Enumeration e = _exclude.elements();
        e.hasMoreElements() ; ) {
      String str = (String)e.nextElement();
      if ( str.startsWith(url) )
        return true;
    }
    return false;
  }

  /**
   * Returns a list of URL's to be excluded.
   *
   * @return A vector of URL's to be excluded.
   */
  public Vector getExclude()
  {
    return _exclude;
  }
  /**
   * Returns the full URL of the robots.txt file.
   *
   * @return The full URL of the robots.txt file.
   */

  public String getRobotFile()
  {
    return _robotFile;
  }
}

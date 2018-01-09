package com.heaton.bot;

import java.io.*;
import java.net.*;

/**
 * This simple static class contains
 * several methods that are useful to
 * manipulate URL's.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class URLUtility {
  /**
   * Private constructor prevents insanitation.
   */
  private URLUtility()
  {
  }

  /**
   * Strip the query string from a URL.
   *
   * @param url    The URL to examine.
   * @return The URL with no query string.
   * @exception MalformedURLException
   */
  static public URL stripQuery(URL url)
  throws MalformedURLException
  {
    String file = url.getFile();
    int i=file.indexOf("?");
    if ( i==-1 )
      return url;
    file = file.substring(0,i);
    return new URL(url.getProtocol(),url.getHost(),url.getPort(),file);
  }

  /**
   * Strip the anchor tag from the URL.
   *
   * @param url    The URL to scan.
   * @return The URL with no anchor tag.
   * @exception MalformedURLException
   */
  static public URL stripAnhcor(URL url)
  throws MalformedURLException
  {
    String file = url.getFile();
    return new URL(url.getProtocol(),url.getHost(),url.getPort(),file);
  }

  /**
   * Encodes a string in base64.
   *
   * @param s      The string to encode.
   * @return The encoded string.
   */
  static public String base64Encode(String s)
  {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    Base64OutputStream out = new Base64OutputStream(bout);
    try {
      out.write(s.getBytes());
      out.flush();
    } catch ( IOException e ) {
    }

    return bout.toString();
  }

  /**
   * Resolve the base of a URL.
   *
   * @param base   The base.
   * @param rel    The relative path.
   * @return The combined absolute URL.
   */
  static public String resolveBase(String base,String rel)
  {
    String protocol;
    int i = base.indexOf(':');
    if ( i!=-1 ) {
      protocol = base.substring(0,i+1);
      base = "http:" + base.substring(i+1);
    } else
      protocol = null;

    URL url;

    try {
      url = new URL(new URL(base),rel);
    } catch ( MalformedURLException e ) {
      return "";
    }

    if ( protocol!=null ) {
      base = url.toString();
      i = base.indexOf(':');
      if ( i!=-1 )
        base = base.substring(i+1);
      base = protocol + base;
      return base;
    } else
      return url.toString();
  }
}

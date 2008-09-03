package com.heatonresearch.httprecipes.ch3.recipe2;

import java.io.*;
import java.net.*;

/**
 * Recipe #3.2: Extract Information from a Web Site
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * Access the httprecipes.com site and get the time in 
 * St. Louis, MO.  Shows how to parse data from HTML.
 *
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not
 * publish the source code without the author's permission.
 * For more information on distributing this code, please
 * visit:
 *    http://www.heatonresearch.com/hr_legal.php
 *
 * @author Jeff Heaton
 * @version 1.1
 */
public class GetTime
{
  /**
   * The size of the download buffer.
   */
  public static int BUFFER_SIZE = 8192;

  /**
   * This method downloads the specified URL into a Java
   * String. This is a very simple method, that you can
   * reused anytime you need to quickly grab all data from
   * a specific URL.
   *
   * @param url The URL to download.
   * @return The contents of the URL that was downloaded.
   * @throws IOException Thrown if any sort of error occurs.
   */
  public String downloadPage(URL url) throws IOException
  {
    StringBuilder result = new StringBuilder();
    byte buffer[] = new byte[BUFFER_SIZE];

    InputStream s = url.openStream();
    int size = 0;

    do
    {
      size = s.read(buffer);
      if (size != -1)
        result.append(new String(buffer, 0, size));
    } while (size != -1);

    return result.toString();
  }

  /**
   * Extract a string of text from between the two specified tokens.  The 
   * case of the two tokens must match.  
   *
   * @param url The URL to download.
   * @param token1 The text, or tag, that comes before the desired text.
   * @param token2 The text, or tag, that comes after the desired text.
   * @param count Which occurrence of token1 to use, 1 for the first.
   * @return The contents of the URL that was downloaded.
   */
  public String extract(String str, String token1, String token2, int count)
  {
    int location1, location2;

    location1 = location2 = 0;
    do
    {
      location1 = str.indexOf(token1, location1);

      if (location1 == -1)
        return null;

      count--;
    } while (count > 0);

    location2 = str.indexOf(token2, location1 + 1);
    if (location2 == -1)
      return null;

    return str.substring(location1 + token1.length(), location2);
  }

  /**
   * Run the example.
   */
  public void go()
  {
    try
    {
      URL u = new URL("http://www.httprecipes.com/1/3/time.php");
      String str = downloadPage(u);

      System.out.println(extract(str, "<b>", "</b>", 1));

    } catch (MalformedURLException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Typical Java main method, create an object, and then
   * call that object's go method.
   *
   * @param args Not used.
   */
  public static void main(String args[])
  {
    GetTime module = new GetTime();
    module.go();
  }
}

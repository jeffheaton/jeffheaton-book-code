package com.heatonresearch.httprecipes.ch6.recipe7;

import java.io.*;
import java.net.*;

import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #6.7: Extract Across Several Linked Pages
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse a list that is broken 
 * across several pages with a next and previous button.
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
public class ExtractPartial
{
  /*
   * The size buffer to use for downloading.
   */ 
  public static int BUFFER_SIZE = 8192;

  /**
   * This method downloads the specified URL into a Java
   * String. This is a very simple method, that you can
   * reused anytime you need to quickly grab all data from
   * a specific URL.
   *
   * @param url The URL to download.
   * @param timeout The number of milliseconds to wait for connection
   * @return The contents of the URL that was downloaded.
   * @throws IOException Thrown if any sort of error occurs.
   */
  public String downloadPage(URL url, int timeout) throws IOException
  {
    StringBuilder result = new StringBuilder();
    byte buffer[] = new byte[BUFFER_SIZE];

    URLConnection http = url.openConnection();
    http.setConnectTimeout(100);
    InputStream s = http.getInputStream();
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
   * Called to process each individual item found.
   * @param official Site The official site for this state.
   * @param flag The flag for this state.
   */
  private void processItem(URL officialSite, URL flag)
  {
    StringBuilder result = new StringBuilder();
    result.append("\"");
    result.append(officialSite.toString());
    result.append("\",\"");
    result.append(flag.toString());
    result.append("\"");
    System.out.println(result.toString());
  }

  /**
   * This method is very useful for grabbing information from a
   * HTML page.  
   *
   * @param url The URL to download.
   * @param token1 The text, or tag, that comes before the desired text.
   * @param token2 The text, or tag, that comes after the desired text.
   * @param count Which occurrence of token1 to use, 1 for the first.
   * @return The contents of the URL that was downloaded.
   * @throws IOException Thrown if any sort of error occurs.
   */
  public String extractNoCase(String str, String token1, String token2,
      int count)
  {
    int location1, location2;

    // convert everything to lower case
    String searchStr = str.toLowerCase();
    token1 = token1.toLowerCase();
    token2 = token2.toLowerCase();

    // now search
    location1 = location2 = 0;
    do
    {
      location1 = searchStr.indexOf(token1, location1 + 1);

      if (location1 == -1)
        return null;

      count--;
    } while (count > 0);

    location1 += token1.length();

    // return the result from the original string that has mixed
    // case
    location2 = str.indexOf(token2, location1 + 1);
    if (location2 == -1)
      return null;

    return str.substring(location1, location2);
  }

  /**
   * Called to process each partial page.
   * @param url The URL of the partial page.
   * @return Returns the next partial page, or null if no more.
   * @throws IOException Thrown if an exception occurs while reading.
   */
  public URL process(URL url) throws IOException
  {
    URL result = null;
    StringBuilder buffer = new StringBuilder();
    String value = "";
    String src = "";

    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);
    boolean first = true;

    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("a"))
        {
          buffer.setLength(0);
          value = tag.getAttributeValue("href");
          URL u = new URL(url, value.toString());
          value = u.toString();
          src = null;
        } else if (tag.getName().equalsIgnoreCase("img"))
        {
          src = tag.getAttributeValue("src");
        } else if (tag.getName().equalsIgnoreCase("/a"))
        {
          if (buffer.toString().equalsIgnoreCase("[Next 5]"))
          {
            result = new URL(url, value);
          } else if (src != null)
          {
            if (!first)
            {
              URL urlOfficial = new URL(url, value);
              URL urlFlag = new URL(url, src);
              processItem(urlOfficial, urlFlag);
            } else
              first = false;
          }
        }
      } else
      {
        buffer.append((char) ch);
      }
    }

    return result;
  }

  /**
   * Called to download the state information from several partial pages.
   * Each page displays only 5 of the 50 states, so it is necessary to link
   * each partial page together.  THis method calls "process" which will process
   * each of the partial pages, until there is no more data.
   * @throws IOException Thrown if an exception occurs while reading.
   */
  public void process() throws IOException
  {
    URL url = new URL("http://www.httprecipes.com/1/6/partial.php");
    do
    {
      url = process(url);
    } while (url != null);

  }

  /**
   * The main method, create a new instance of the object and call
   * process.
   * @param args not used.
   */  
  public static void main(String args[])
  {
    try
    {
      ExtractPartial parse = new ExtractPartial();
      parse.process();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

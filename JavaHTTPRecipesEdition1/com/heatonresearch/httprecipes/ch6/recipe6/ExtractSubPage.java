package com.heatonresearch.httprecipes.ch6.recipe6;

import java.io.*;
import java.net.*;

import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #6.6: Extract Data from Subpages
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse a parent page, then visit
 * each child page looking for data.
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
public class ExtractSubPage
{
  /*
   * The size buffer to use for downloading.
   */ 
  public static int BUFFER_SIZE = 8192;

  
  /**
   * Process each sub page. The sub pages are where the data actually is.
   * @param u The URL of the sub page.
   * @throws IOException Thrown if an error occurs while processing.
   */
  private void processSubPage(URL u) throws IOException
  {
    String str = downloadPage(u, 5000);
    String code = extractNoCase(str, "Code:<b></td><td>", "</td>", 0);
    if (code != null)
    {
      String capital = extractNoCase(str, "Capital:<b></td><td>", "</td>", 0);
      String name = extractNoCase(str, "<h1>", "</h1>", 0);
      String flag = extractNoCase(str, "<img src=\"", "\" border=\"1\">", 2);
      String site = extractNoCase(str, "Official Site:<b></td><td><a href=\"",
          "\"", 0);

      URL flagURL = new URL(u, flag);

      StringBuilder buffer = new StringBuilder();
      buffer.append("\"");
      buffer.append(code);
      buffer.append("\",\"");
      buffer.append(name);
      buffer.append("\",\"");
      buffer.append(capital);
      buffer.append("\",\"");
      buffer.append(flagURL.toString());
      buffer.append("\",\"");
      buffer.append(site);
      buffer.append("\"");
      System.out.println(buffer.toString());
    }
  }

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
   * This method is very useful for grabbing information from a
   * HTML page.  
   *
   * @param url The URL to download.
   * @param token1 The text, or tag, that comes before the desired text
   * @param token2 The text, or tag, that comes after the desired text
   * @param count Which occurrence of token1 to use, 1 for the first
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
   * Process the specified URL and extract data from all of the sub pages
   * that this page links to.
   * @param url The URL to process.
   * @throws IOException Thrown if an error occurs while reading the URL.
   */
  public void process(URL url) throws IOException
  {
    String value = "";
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);

    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("a"))
        {
          value = tag.getAttributeValue("href");
          URL u = new URL(url, value.toString());
          value = u.toString();
          processSubPage(u);
        }
      }
    }
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
      URL u = new URL("http://www.httprecipes.com/1/6/subpage.php");
      ExtractSubPage parse = new ExtractSubPage();
      parse.process(u);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
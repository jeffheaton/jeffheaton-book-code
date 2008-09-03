package com.heatonresearch.httprecipes.ch9.recipe2;

import java.io.*;
import java.net.*;

import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #9.2: JavaScript Includes
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to access JavaScript includes.
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
public class Includes
{
  /**
   * The size of the download buffer.
   */
  public static int BUFFER_SIZE = 8192;
  
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

    // return the result from the original string that has mixed
    // case
    location2 = str.indexOf(token2, location1 + 1);
    if (location2 == -1)
      return null;

    return str.substring(location1 + token1.length(), location2);
  }
  
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
   * Called to download the text from a page.  If any JavaScript
   * include is found, the text from that page is read too.
   */
  public void process()
      throws IOException
  {
    URL url = new URL("http://www.httprecipes.com/1/9/includes.php");
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);
    StringBuilder buffer = new StringBuilder();
    
    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("script") && tag.getAttributeValue("src")!=null)
        {
          String src = tag.getAttributeValue("src");
          URL u = new URL(url,src);
          String include = downloadPage(u);
          buffer.append("<script>");
          buffer.append(include);
          buffer.append("</script>");
        }
        else
        {
          buffer.append(tag.toString());
        }
      }
      else 
      {
        buffer.append((char)ch);
      }
    }
    
    System.out.println(buffer.toString());
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
      Includes parse = new Includes();
      parse.process();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
package com.heatonresearch.httprecipes.ch4.recipe1;

import java.io.IOException;
import java.net.*;

/**
 * Recipe #4.1: Scan a URL's Headers
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe displays the headers provided by a web server.
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
public class ScanURL
{
  /**
   * Scan the URL and display headers.
   * 
   * @param u The URL to scan.
   * @throws IOException Error scanning URL.
   */
  public void scan(String u) throws IOException
  {
    URL url = new URL(u);
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    int count = 0;
    String key, value;

    do
    {
      key = http.getHeaderFieldKey(count);
      value = http.getHeaderField(count);
      count++;
      if (value != null)
      {
        if (key == null)
          System.out.println(value);
        else
          System.out.println(key + ": " + value);
      }
    } while (value != null);
  }

  /**
   * Typical Java main method, create an object, and then
   * start the object passing arguments. If insufficient 
   * arguments are provided, then display startup 
   * instructions.
   * 
   * @param args Program arguments.
   */
  public static void main(String args[])
  {
    try
    {
      if (args.length != 1)
      {
        System.out.println("Usage: \njava ScanURL [URL to Scan]");
      } else
      {
        ScanURL d = new ScanURL();
        d.scan(args[0]);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

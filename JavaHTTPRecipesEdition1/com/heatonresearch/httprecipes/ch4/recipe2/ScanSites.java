package com.heatonresearch.httprecipes.ch4.recipe2;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Recipe #4.2: Scan IP's for Sites
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to scan a series of IP addresses
 * for web sites.  This recipe is designed to take a IP
 * address, such as 192.168.1 and then cycle through all
 * 256 IP addresses by providing the fourth part of the
 * IP address.
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
public class ScanSites
{
  // the size of a buffer
  public static int BUFFER_SIZE = 8192;

  /**
   * This method downloads the specified URL into a Java
   * String. This is a very simple method, that you can
   * reused anytime you need to quickly grab all data from
   * a specific URL.
   *
   * @param url The URL to download.
   * @param timeout The number of milliseconds to wait for connection.
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
   * Extract a string of text from between the two specified tokens.  The 
   * case of the two tokens need not match.  
   *
   * @param url The URL to download.
   * @param token1 The text, or tag, that comes before the desired text.
   * @param token2 The text, or tag, that comes after the desired text.
   * @param count Which occurrence of token1 to use, 1 for the first.
   * @return The contents of the URL that was downloaded.
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
   * Scan the specified IP address and return the title of
   * the web page found there, or null if no connection can
   * be made.
   * 
   * @param ip The IP address to scan.
   * @return The title of the web page, or null if no website.
   */
  private String scanIP(String ip)
  {
    try
    {
      System.out.println("Scanning: " + ip);
      String page = downloadPage(new URL("http://" + ip), 1000);
      String title = extractNoCase(page, "<title>", "</title>", 0);
      if (title == null)
        title = "[Untitled site]";
      return title;
    } catch (IOException e)
    {
      return null;
    }
  }

  /**
   * Scan a range of 256 IP addressed.  Provide the prefix
   * of the IP address, without the final fourth.  For 
   * example "192.168.1".
   * 
   * @param ip The IP address prefix(i.e. 192.168.1)
   */
  public void scan(String ip)
  {
    if (!ip.endsWith("."))
    {
      ip += ".";
    }

    // create a list to hold sites found
    List<String> list = new ArrayList<String>();

    // scan through IP addresses ending in 0 - 255
    for (int i = 1; i < 255; i++)
    {
      String address = ip + i;
      String title = scanIP(address);
      if (title != null)
        list.add(address + ":" + title);
    }

    // now display the list of sites found
    System.out.println();
    System.out.println("Sites found:");
    if (list.size() > 0)
    {
      for (String site : list)
      {
        System.out.println(site);
      }
    } else
    {
      System.out.println("No sites found");
    }
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
        System.out.println("Usage: ScanSites [IP prefix, i.e. 192.168.1]");
      } else
      {
        ScanSites d = new ScanSites();
        d.scan(args[0]);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

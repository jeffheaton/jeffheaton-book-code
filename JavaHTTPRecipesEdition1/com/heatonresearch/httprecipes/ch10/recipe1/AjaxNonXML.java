package com.heatonresearch.httprecipes.ch10.recipe1;

import java.io.*;
import java.net.*;

/**
 * Recipe #10.1: Non-XML AJAX
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to access data from an AJAX website.
 * The data that will be extracted is HTML.
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
public class AjaxNonXML
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
   * This method will extract data found between two tokens, which are
   * usually tags.  This method does not care about the case of the
   * tokens.
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
      if( location1>0 )
        location1++;
      
      location1 = searchStr.indexOf(token1, location1 );

      if (location1 == -1)
        return null;

      count--;
    } while (count > 0);

    // return the result from the original string that has mixed
    // case
    location2 = str.indexOf(token2, location1 + token1.length() + 1);
    if (location2 == -1)
      return null;

    return str.substring(location1 + token1.length(), location2);
  }

  /**
   * This method will download data from the specified state.
   * This data will come in as a partial HTML document, 
   * the necessary data will be extracted from there.
   * @param state The state you want to download (i.e. Missouri).
   * @throws IOException Thrown if a communication error occurs.
   */
  public void process(String state) throws IOException
  {
    URL url = new URL("http://www.httprecipes.com/1/10/statehtml.php?state="+state);
    String buffer = downloadPage(url,10000);
    String name = this.extractNoCase(buffer,"<h1>", "</h1>", 0);
    String capital = this.extractNoCase(buffer,"Capital:<b></td><td>", "</td>", 0);
    String code = this.extractNoCase(buffer,"Code:<b></td><td>", "</td>", 0);
    String site = this.extractNoCase(buffer,"Official Site:<b></td><td><a href=\"", "\"", 0);
    
    System.out.println("State name:" + name);
    System.out.println("State capital:"+ capital);
    System.out.println("Code:"+code);
    System.out.println("Site:"+site);   
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
        System.out.println("Usage:\njava AjaxNonXML [state, i.e. Missouri]");
      } else
      {
        AjaxNonXML d = new AjaxNonXML();
        d.process(args[0]);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }  
}

package com.heatonresearch.httprecipes.ch5.recipe1;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

/**
 * Recipe #5.1: Is URL HTTPS?
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to determine if a URL is using
 * the HTTPS protocol.
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
public class IsHTTPS
{
  
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
    String strURL = "";
    
    // obtain a URL to use
    if (args.length < 1)
    {
      strURL = "https://www.httprecipes.com/1/5/secure/";
    } else
    {
      strURL = args[0];
    }

    URL url;
    try
    {
      url = new URL(strURL);
      URLConnection conn = url.openConnection();
      conn.connect();
      
      // see what type of object was returned
      if (conn instanceof HttpsURLConnection)
      {
        System.out.println("Valid HTTPS URL");
      } else if (conn instanceof HttpURLConnection)
      {
        System.out.println("Valid HTTP URL");
      } else
      {
        System.out.println("Unknown protocol URL");
      }

    } catch (MalformedURLException e)
    {
      System.out.println("Invalid URL");
    } catch (IOException e)
    {
      System.out.println("Error connecting to URL");
    }
  }

}

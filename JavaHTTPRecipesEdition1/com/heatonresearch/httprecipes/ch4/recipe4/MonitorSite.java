package com.heatonresearch.httprecipes.ch4.recipe4;

import java.util.*;
import java.net.*;
import java.io.*;

/**
 * Recipe #4.4: Monitor a Site
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe will monitor the specified website to see
 * if the site is still "up".  This recipe will scan the
 * site once a minute.
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
public class MonitorSite
{
  
  /**
   * Scan a URL every minute to make sure it is still up.
   * @param url The URL to monitor.
   */
  public void monitor(URL url)
  {
    while (true)
    {
      System.out.println("Checking " + url + " at " + (new Date()));

      // try to connect
      try
      {
        URLConnection http = url.openConnection();
        http.connect();
        System.out.println("The site is up.");
      } catch (IOException e1)
      {
        System.out.println("The site is down!!!");
      }

      // now wait for a minute before checking again
      try
      {
        Thread.sleep(60000);
      } catch (InterruptedException e)
      {
      }
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
        System.out.println("Usage: MonitorSite [URL to Monitor]");
      } else
      {
        MonitorSite d = new MonitorSite();
        d.monitor(new URL(args[0]));
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

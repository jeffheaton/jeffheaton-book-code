package com.heatonresearch.httprecipes.ch12.recipe1;

import java.io.*;
import java.net.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.heatonresearch.httprecipes.rss.*;

/**
 * Recipe #12.1: Display an RSS Feed
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to use the RSS class to load a
 * RSS feed.  The RSS feed is then displayed.
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
public class LoadRSS
{

  /**
   * Display an RSS feed.
   * @param url The URL of the RSS feed.
   * @throws IOException Thrown if an IO exception occurs.
   * @throws SAXException Thrown if an XML parse exception occurs.
   * @throws ParserConfigurationException Thrown if there is an error setting
   * up the parser.
   */
  public void process(URL url) throws IOException, SAXException,
      ParserConfigurationException
  {
    RSS rss = new RSS();
    rss.load(url);
    System.out.println(rss.toString());
  }

  /**
   * The main method processes the command line arguments and
   * then calls process to display the feed.
   * 
   * @param args Holds the RSS feed to download.
   */
  public static void main(String args[])
  {
    try
    {
      URL url;

      if (args.length != 0)
        url = new URL(args[0]);
      else
        url = new URL("http://www.httprecipes.com/1/12/rss1.xml");

      LoadRSS load = new LoadRSS();
      load.process(url);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

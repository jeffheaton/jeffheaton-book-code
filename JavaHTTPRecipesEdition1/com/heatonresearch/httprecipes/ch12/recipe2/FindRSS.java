package com.heatonresearch.httprecipes.ch12.recipe2;

import java.io.*;
import java.net.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.heatonresearch.httprecipes.html.*;
import com.heatonresearch.httprecipes.rss.*;

/**
 * Recipe #12.2: Find an RSS Feed
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to provide a base URL and find an
 * RSS feed for that site, if one exists.  This recipe
 * works by looking for the link tag that specifies the
 * location of the RSS feed.
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
public class FindRSS
{
  /**
   * Display an RSS feed.
   * @param url The URL of the RSS feed.
   * @throws IOException Thrown if an IO exception occurs.
   * @throws SAXException Thrown if an XML parse exception occurs.
   * @throws ParserConfigurationException Thrown if there is an error setting
   * up the parser.
   */
  public void processRSS(URL url) throws IOException, SAXException,
      ParserConfigurationException
  {
    RSS rss = new RSS();
    rss.load(url);
    System.out.println(rss.toString());
  }

  /**
   * This method looks for a link tag at the specified URL.  If a link
   * tag is found that specifies an RSS feed, then that feed is 
   * displayed.
   * @param url The URL of the web site.
   * @throws IOException Thrown if an IO exception occurs.
   * @throws SAXException Thrown if an XML parse exception occurs.
   * @throws ParserConfigurationException Thrown if there is an error setting
   * up the parser.
   */
  public void process(URL url) throws IOException, SAXException,
      ParserConfigurationException
  {
    String href = null;
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);
    int ch;
    do
    {
      ch = parse.read();
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("link"))
        {
          String type = tag.getAttributeValue("type");
          if (type != null && type.indexOf("rss") != -1)
          {
            href = tag.getAttributeValue("href");
          }
        }
      }
    } while (ch != -1);

    if (href == null)
    {
      System.out.println("No RSS link found.");
    } else
      processRSS(new URL(href));

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
        url = new URL("http://www.httprecipes.com/");

      FindRSS load = new FindRSS();
      load.process(url);
    } catch (Exception e)
    {
      e.printStackTrace();
    }

  }

}

package com.heatonresearch.httprecipes.ch6.recipe4;

import java.io.*;
import java.net.*;
import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #6.4: Parse Links
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse links from an HTML page.
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
public class ExtractLinks
{

  /**
   * Process an individual option tag.  Store the state name
   * and code to a list.
   * @param name The name of the option.
   * @param value The value of the option.
   */
  private void processOption(String name, String value)
  {
    StringBuilder result = new StringBuilder();
    result.append('\"');
    result.append(name);
    result.append("\",\"");
    result.append(value);
    result.append('\"');
    System.out.println(result.toString());
  }

  /**
   * Process the specified URL and parse an option list.
   * @param url The URL to process.
   * @param optionList Which option list to process, zero for the first one.
   * @throws IOException Thrown if the page cannot be read.
   */
  public void process(URL url, int optionList) throws IOException
  {
    String value = "";
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);
    StringBuilder buffer = new StringBuilder();

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
          buffer.setLength(0);
        } else if (tag.getName().equalsIgnoreCase("/a"))
        {
          processOption(buffer.toString(), value);
        }
      } else
      {
        buffer.append((char) ch);
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
      URL u = new URL("http://www.httprecipes.com/1/6/link.php");
      ExtractLinks parse = new ExtractLinks();
      parse.process(u, 1);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
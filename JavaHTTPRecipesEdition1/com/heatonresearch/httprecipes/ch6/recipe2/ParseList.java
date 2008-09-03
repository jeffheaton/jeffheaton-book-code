package com.heatonresearch.httprecipes.ch6.recipe2;

import java.io.*;
import java.net.*;
import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #6.2: Parse List
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse a list.
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
public class ParseList
{

  /** 
   * Advance to the specified HTML tag.
   * @param parse The HTML parse object to use.
   * @param tag The HTML tag.
   * @param count How many tags like this to find.
   * @return True if found, false otherwise.
   * @throws IOException If an exception occurs while reading.
   */
  private boolean advance(ParseHTML parse, String tag, int count)
      throws IOException
  {
    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        if (parse.getTag().getName().equalsIgnoreCase(tag))
        {
          count--;
          if (count <= 0)
            return true;
        }
      }
    }
    return false;
  }

  /*
   * Handle each list item, as it is found.
   */
  private void processItem(String item)
  {
    System.out.println(item);
  }

  /**
   * Called to extract a list from the specified URL.
   * @param url The URL to extract the list from.
   * @param listType What type of list, specify its beginning tag (i.e. <UL>).
   * @param optionList Which list to search, zero for first.
   * @throws IOException Thrown if an IO exception occurs.
   */
  public void process(URL url, String listType, int optionList)
      throws IOException
  {
    String listTypeEnd = listType + "/";
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);
    StringBuilder buffer = new StringBuilder();
    boolean capture = false;

    advance(parse, listType, optionList);

    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("li"))
        {
          if (buffer.length() > 0)
            processItem(buffer.toString());
          buffer.setLength(0);
          capture = true;
        } else if (tag.getName().equalsIgnoreCase("/li"))
        {
          System.out.println(buffer.toString());
          processItem(buffer.toString());
          buffer.setLength(0);
          capture = false;
        } else if (tag.getName().equalsIgnoreCase(listTypeEnd))
        {
          break;
        }
      } else
      {
        if (capture)
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
      URL u = new URL("http://www.httprecipes.com/1/6/list.php");
      ParseList parse = new ParseList();
      parse.process(u, "ul", 1);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
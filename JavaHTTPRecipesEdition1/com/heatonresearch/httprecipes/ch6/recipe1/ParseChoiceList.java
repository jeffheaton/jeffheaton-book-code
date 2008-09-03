package com.heatonresearch.httprecipes.ch6.recipe1;

import java.io.*;
import java.net.*;
import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #6.1: Parse Choice List
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse data from a choice list.
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
public class ParseChoiceList
{

  /**
   * Called for each option item that is found. 
   * @param name The name of the option item.
   * @param value The value of the option item.
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

  /**
   * Process the specified URL and extract the option list there.
   * @param url The URL to process.
   * @param optionList Which option list to process, zero for first.
   * @throws IOException Any exceptions that might have occurred while reading.
   */
  public void process(URL url, int optionList) throws IOException
  {
    String value = "";
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);
    StringBuilder buffer = new StringBuilder();

    advance(parse, "select", optionList);

    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("option"))
        {
          value = tag.getAttributeValue("value");
          buffer.setLength(0);
        } else if (tag.getName().equalsIgnoreCase("/option"))
        {
          processOption(buffer.toString(), value);
        } else if (tag.getName().equalsIgnoreCase("/choice"))
        {
          break;
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
      URL u = new URL("http://www.httprecipes.com/1/6/form.php");
      ParseChoiceList parse = new ParseChoiceList();
      parse.process(u, 1);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
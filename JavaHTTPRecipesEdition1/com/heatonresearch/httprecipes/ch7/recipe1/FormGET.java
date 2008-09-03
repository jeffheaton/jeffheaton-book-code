package com.heatonresearch.httprecipes.ch7.recipe1;

import java.io.*;
import java.net.*;
import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #7.1: Parse List from a Form GET
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse a list, obtained from a FORM GET.
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
public class FormGET
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

  /**
   * Handle each list item, as it is found.
   * @param item The item to be processed.
   */
  private void processItem(String item)
  {
    System.out.println(item.trim());
  }

  /**
   * Access the website and perform a search for either states or capitals.
   * @param search A search string.
   * @param type What to search for(s=state, c=capital)
   * @throws IOException Thrown if an IO exception occurs.
   */
  public void process(String search, String type) throws IOException
  {
    String listType = "ul";
    String listTypeEnd = "/ul";
    StringBuilder buffer = new StringBuilder();
    boolean capture = false;

    // build the URL
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    FormUtility form = new FormUtility(bos, null);
    form.add("search", search);
    form.add("type", type);
    form.add("action", "Search");
    form.complete();

    String surl = "http://www.httprecipes.com/1/7/get.php?" + bos.toString();
    URL url = new URL(surl);
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);

    // parse from the URL

    advance(parse, listType, 0);

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
          processItem(buffer.toString());
          buffer.setLength(0);
          capture = false;
        } else if (tag.getName().equalsIgnoreCase(listTypeEnd))
        {
          processItem(buffer.toString());
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
      FormGET parse = new FormGET();
      parse.process("Mi", "s");
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
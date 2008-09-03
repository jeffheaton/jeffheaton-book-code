package com.heatonresearch.httprecipes.ch6.recipe3;

import java.io.*;
import java.net.*;
import java.util.*;
import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #6.3: Parse Table
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse from an HTML table.
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
public class ParseTable
{

  /** Advance to the specified HTML tag.
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
   * This method is called once for each table row located, it 
   * contains a list of all columns in that row.  The method provided
   * simply prints the columns to the console.
   * @param list Columns that were found on this row.
   */
  private void processTableRow(List<String> list)
  {
    StringBuilder result = new StringBuilder();
    for (String item : list)
    {
      if (result.length() > 0)
        result.append(",");
      result.append('\"');
      result.append(item);
      result.append('\"');

    }
    System.out.println(result.toString());
  }

  /**
   * Called to parse a table.  The table number at the specified URL
   * will be parsed.
   * @param url The URL of the HTML page that contains the table.
   * @param tableNum The table number to parse, zero for the first.
   * @throws IOException Thrown if an error occurs while reading.
   */
  public void process(URL url, int tableNum) throws IOException
  {
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);
    StringBuilder buffer = new StringBuilder();
    List<String> list = new ArrayList<String>();
    boolean capture = false;

    advance(parse, "table", tableNum);

    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("tr"))
        {
          list.clear();
          capture = false;
          buffer.setLength(0);
        } else if (tag.getName().equalsIgnoreCase("/tr"))
        {
          if (list.size() > 0)
          {
            processTableRow(list);
            list.clear();
          }
        } else if (tag.getName().equalsIgnoreCase("td"))
        {
          if (buffer.length() > 0)
            list.add(buffer.toString());
          buffer.setLength(0);
          capture = true;
        } else if (tag.getName().equalsIgnoreCase("/td"))
        {
          list.add(buffer.toString());
          buffer.setLength(0);
          capture = false;
        } else if (tag.getName().equalsIgnoreCase("/table"))
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
      URL u = new URL("http://www.httprecipes.com/1/6/table.php");
      ParseTable parse = new ParseTable();
      parse.process(u, 2);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
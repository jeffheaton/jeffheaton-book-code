package com.heatonresearch.httprecipes.ch11.recipe3;

import com.google.soap.search.*;
import com.heatonresearch.httprecipes.html.ParseHTML;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Recipe #11.3: Using the Google API to Find Someone was Born
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to use the Google API to determine
 * someone's birth year.  This bot uses both the Google API
 * and regular HTTP programming.  
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
public class WhenBorn
{
  /*
   * The key that Google provides anyone who uses their API.
   */
  static String key;

  /*
   * The search object to use.
   */
  static GoogleSearch search;
  
  /*
   * This map stores a mapping between a year, and how many times that year
   * has come up as a potential birth year.
   */
  private Map<Integer, Integer> results = new HashMap<Integer, Integer>();

  
  /**
   * Perform a Google search and return the results. Only return
   * 100 results.
   * @param query What to search for.
   * @return The results of the Google search.
   */
  public Collection<GoogleSearchResultElement> getResults(String query)
  {
    Collection<GoogleSearchResultElement> result = new ArrayList<GoogleSearchResultElement>();
    int resultCount = 0;
    int top = 0;

    do
    {
      search.setQueryString("\"" + query + "\"");
      search.setStartResult(top);
      GoogleSearchResult r = null;
      try
      {
        r = search.doSearch();
      } catch (GoogleSearchFault e)
      {
        e.printStackTrace();
      }

      if (r != null)
      {
        Object list[] = r.getResultElements();
        resultCount = list.length;
        for (int i = 0; i < resultCount; i++)
        {
          GoogleSearchResultElement element = (GoogleSearchResultElement) list[i];
          result.add(element);
        }
      }
      top = top + 10;
    } while ((resultCount >= 10) && (result.size() < 100));

    return result;
  }

  
  /**
   * Examine a sentence and see if it contains the word born
   * and a number.
   * 
   * @param sentence The sentence to search.
   * @return The number that was found.
   */
  private int extractBirth(String sentence)
  {
    boolean foundBorn = false;
    int result = -1;

    StringTokenizer tok = new StringTokenizer(sentence);
    while (tok.hasMoreTokens())
    {
      String word = tok.nextToken();

      try
      {
        result = Integer.parseInt(word);
      } catch (NumberFormatException e)
      {
        if (word.equalsIgnoreCase("born"))
          foundBorn = true;
      }
    }

    if (!foundBorn)
      result = -1;

    return result;
  }

  /**
   * Increase the specified year's count in the map.
   * @param year The year to increase.
   */
  private void increaseYear(int year)
  {
    Integer count = results.get(year);
    if (count == null)
      count = new Integer(1);
    else
      count = new Integer(count.intValue() + 1);
    results.put(year, count);
  }

  
  /**
   * Check the specified URL for a birth year.  This will occur if one
   * sentence is found that has the word born, and a numeric value less
   * than 3000.
   * 
   * @param url The URL to check.
   * @throws IOException Thrown if a communication error occurs.
   */
  public void checkURL(URL url) throws IOException
  {
    int ch;
    StringBuilder sentence = new StringBuilder();

    URLConnection http = url.openConnection();
    http.setConnectTimeout(1000);
    http.setReadTimeout(1000);
    InputStream is = http.getInputStream();
    ParseHTML html = new ParseHTML(is);
    do
    {
      ch = html.read();
      if ((ch != -1) && (ch != 0))
      {
        if (ch == '.')
        {
          String str = sentence.toString();
          int year = extractBirth(str);
          if ((year > 1) && (year < 3000))
          {
            System.out.println("URL supports year: " + year);
            increaseYear(year);
          }
          sentence.setLength(0);
        } else
          sentence.append((char) ch);
      }
    } while (ch != -1);

  }

  
  /**
   * Get birth year that occurred the largest number of times.
   * 
   * @return The birth year that occurred the largest number of times.
   */
  public int getResult()
  {
    int result = -1;
    int maxCount = 0;

    Set<Integer> set = results.keySet();
    for (int year : set)
    {
      int count = results.get(year);
      if (count > maxCount)
      {
        result = year;
        maxCount = count;
      }
    }

    return result;
  }

  
  /**
   * This method is called to determine the birth year for a person.  It
   * obtains 100 web pages that Google returns for that person.  Each
   * of these pages is then searched for the birth year of that person.
   * Which ever year is selected the largest number of times is selected
   * as the birth year.
   * 
   * @param name The name of the person you are seeing the birth year for.
   * @throws IOException Thrown if a communication error occurs.
   */
  public void process(String name) throws IOException
  {
    search = new GoogleSearch();
    search.setKey(key);

    System.out.println("Getting search results form Google.");
    Collection<GoogleSearchResultElement> c = getResults(name);
    int i = 0;

    System.out.println("Scanning URL's from Google.");
    for (GoogleSearchResultElement element : c)
    {
      try
      {
        i++;
        URL u = new URL(element.getURL());
        System.out.println("Scanning URL: " + i + "/" + c.size() + ":" + u);
        checkURL(u);
      } catch (IOException e)
      {

      }
    }

    int resultYear = getResult();
    if (resultYear == -1)
    {
      System.out.println("Could not determine when " + name + " was born.");
    } else
    {
      System.out.println(name + " was born in " + resultYear);
    }
  }

  /**
   * The main method processes the command line arguments and
   * then calls process method to determine the birth year.
   * 
   * @param args Holds the Google key and the site to search.
   */
  public static void main(String args[])
  {
    if (args.length < 2)
    {
      System.out.println("Usage:\njava WhenBorn [Google Key] [Famous Person]");
    } else
    {
      try
      {
        key = args[0];
        WhenBorn when = new WhenBorn();
        when.process(args[1]);
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

}
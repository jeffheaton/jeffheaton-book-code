package com.heatonresearch.httprecipes.ch11.recipe1;

import com.google.soap.search.*;

import java.util.*;

/**
 * Recipe #11.1: Using the Google API to Find Links
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to use the Google API to find all
 * sites that link to a given web site.  This recipe also
 * scans to see how many links each of them have as well.
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
public class ScanLinks
{
  /*
   * The key that Google provides anyone who uses their API.
   */
  static String key;

  /*
   * The search object to use.
   */
  static GoogleSearch search;

  /**
   * This method queries Google for the specified search, all
   * URL's found by Google are returned.
   * 
   * @param query What to search for.
   * @return The URL's that google returned for the search.
   */
  public static Collection<GoogleSearchResultElement> getResults(String query)
  {
    Collection<GoogleSearchResultElement> result = new ArrayList<GoogleSearchResultElement>();
    int resultCount = 0;
    int top = 0;

    do
    {
      search.setQueryString(query);
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
      System.out.println(top);
    } while (resultCount >= 10);

    return result;
  }

  /**
   * For the given URL check how many links the URL has.
   * 
   * @param url The URL to check.
   * @return The number of links that URL has.
   */
  public static int getLinkCount(String url)
  {
    int result = 0;
    search.setQueryString("link:" + url);
    GoogleSearchResult r = null;
    try
    {
      r = search.doSearch();
    } catch (GoogleSearchFault e)
    {
      e.printStackTrace();
    }

    result = r.getEstimatedTotalResultsCount();
    return result;

  }

  /**
   * The main method processes the command line arguments and
   * then calls getResults to build up the list.
   * 
   * @param args Holds the Google key and the site to search.
   */
  public static void main(String args[])
  {
    if (args.length < 2)
    {
      System.out.println("ScanLinks [Google Key] [Site to Scan]");
    } else
    {
      key = args[0];
      search = new GoogleSearch();
      search.setKey(key);

      Collection<GoogleSearchResultElement> c = getResults("\"" + args[1]
          + "\"");

      for( GoogleSearchResultElement element:c)
      {
        StringBuilder str = new StringBuilder();
        str.append(getLinkCount(element.getURL()));
        str.append(":");
        str.append(element.getTitle() );
        str.append("(");
        str.append(element.getURL());
        str.append(")");
        System.out.println(str.toString());

      }
    }
  }
}

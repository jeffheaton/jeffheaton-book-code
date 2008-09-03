package com.heatonresearch.httprecipes.spider;

import java.io.*;
import java.net.*;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * SimpleReport: This is a very simple implementation of the
 * SpiderReportable interface. It stays within a single host
 * and does not process any data.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class SimpleReport implements SpiderReportable
{
  /**
   * The current host, only accept URL's from this host.
   */
  private String host;

  /**
   * This function is called when the spider is ready to
   * process a new host. THis function simply stores the
   * value of the current host.
   * 
   * @param host
   *          The new host that is about to be processed.
   * @return True if this host should be processed, false
   *         otherwise.
   */
  public boolean beginHost(String host)
  {
    if (this.host == null)
    {
      this.host = host;
      return true;
    } else
    {
      return false;
    }
  }

  /**
   * Called when the spider is starting up. This method
   * provides the SpiderReportable class with the spider
   * object.
   * 
   * @param spider
   *          The spider that will be working with this
   *          object.
   */
  public void init(Spider spider)
  {
  }

  /**
   * Called when the spider encounters a URL.
   * 
   * @param url
   *          The URL that the spider found.
   * @param source
   *          The page that the URL was found on.
   * @param type
   *          The type of link this URL is.
   * @return True if the spider should scan for links on
   *         this page.
   */
  public boolean spiderFoundURL(URL url, URL source, URLType type)
  {
    if ((this.host != null) && (!this.host.equalsIgnoreCase(url.getHost())))
    {
      return false;
    }

    return true;
  }

  /**
   * Called when the spider is about to process a NON-HTML
   * URL. For this SimpleReport manager, this is ignored.
   * 
   * @param url
   *          The URL that the spider found.
   * @param stream
   *          An InputStream to read the page contents from.
   * @throws IOException
   *           Thrown if an IO error occurs while processing
   *           the page.
   */
  public void spiderProcessURL(URL url, InputStream stream) throws IOException
  {
  }

  /**
   * Called when the spider is ready to process an HTML
   * URL. For this SimpleReport manager, this is ignored.
   * 
   * @param url
   *          The URL that the spider is about to process.
   * @param parse
   *          An object that will allow you you to parse the
   *          HTML on this page.
   * @throws IOException
   *           Thrown if an IO error occurs while processing
   *           the page.
   */
  public void spiderProcessURL(URL url, SpiderParseHTML parse)
      throws IOException
  {
  }

  /**
   * Called when the spider tries to process a URL but gets
   * an error. For this SimpleReport manager, this is
   * ignored.
   * 
   * @param url
   *          The URL that generated an error.
   */
  public void spiderURLError(URL url)
  {
  }

}

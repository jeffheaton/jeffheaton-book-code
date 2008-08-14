package com.heatonresearch.httprecipes.spider.filter;

import java.io.*;
import java.net.*;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * SpiderFilter: Filters will cause the spider to skip
 * URL's.
 * 
 * This software is copyrighted. You may use it in programs
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public interface SpiderFilter {
  /**
   * Check to see if the specified URL is to be excluded.
   * 
   * @param url
   *          The URL to be checked.
   * @return Returns true if the URL should be excluded.
   */
  public boolean isExcluded(URL url);

  /**
   * Called when a new host is to be processed. Hosts 
   * are processed one at a time.  SpiderFilter classes 
   * can not be shared among hosts.
   * 
   * @param host
   *          The new host.
   * @param userAgent
   *          The user agent being used by the spider. Leave
   *          null for default.
   * @throws IOException
   *           Thrown if an I/O error occurs.
   */
  public void newHost(String host, String userAgent) throws IOException;
}

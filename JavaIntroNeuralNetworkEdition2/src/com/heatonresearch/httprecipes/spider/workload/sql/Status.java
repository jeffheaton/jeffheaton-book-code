package com.heatonresearch.httprecipes.spider.workload.sql;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * Status: This class defines the constant status values for
 * both the spider_host and spider_workload tables.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class Status
{
  /**
   * The item is waiting to be processed.
   */
  public static final String STATUS_WAITING = "W";

  /**
   * The item was processed, but resulted in an error.
   */
  public static final String STATUS_ERROR = "E";

  /**
   * The item was processed successfully.
   */
  public static final String STATUS_DONE = "D";

  /**
   * The item is currently being processed.
   */
  public static final String STATUS_PROCESSING = "P";

  /**
   * This item should be ignored, only applies to hosts.
   */
  public static final String STATUS_IGNORE = "I";
}

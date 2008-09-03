package com.heatonresearch.httprecipes.ch13.recipe1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import com.heatonresearch.httprecipes.spider.*;
import com.heatonresearch.httprecipes.spider.workload.*;

/**
 * Recipe #13.1: Scanning for broken links 
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * The report class for the Link Checker.
 * 
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not publish
 * the source code without the author's permission. For more
 * information on distributing this code, please visit:
 * http://www.heatonresearch.com/hr_legal.php
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class LinkReport implements SpiderReportable {
  /**
   * For logging.
   */
  private static Logger logger = Logger
      .getLogger("com.heatonresearch.httprecipes.spider.Spider");

  /**
   * The host we are working with.
   */
  private String base;

  /**
   * The bad URL's.
   */
  private List<String> bad = new ArrayList<String>();

  /**
   * The workload manager being used.
   */
  private WorkloadManager workloadManager;

  /**
   * This function is called when the spider is ready to
   * process a new host. This function simply stores the
   * value of the current host.
   * 
   * @param host
   *          The new host that is about to be processed.
   * @return True if this host should be processed, false
   *         otherwise.
   */
  public boolean beginHost(String host) {
    if (this.base == null) {
      this.base = host;
      return true;
    } else {
      return false;
    }
  }

  /**
   * @return the bad
   */
  public List<String> getBad() {
    return this.bad;
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
  public void init(Spider spider) {
    this.workloadManager = spider.getWorkloadManager();
  }

  /**
   * @param bad
   *          the bad to set
   */
  public void setBad(List<String> bad) {
    this.bad = bad;
  }

  public boolean spiderFoundURL(URL url, URL source,
      SpiderReportable.URLType type) {

    if ((this.base != null) && (!this.base.equalsIgnoreCase(url.getHost()))) {
      return false;
    }

    return true;
  }

  /**
   * Called when the spider is about to process a NON-HTML
   * URL.
   * 
   * @param url
   *          The URL that the spider found.
   * @param stream
   *          An InputStream to read the page contents from.
   * @throws IOException
   *           Thrown if an IO error occurs while processing
   *           the page.
   */
  public void spiderProcessURL(URL url, InputStream stream) {
  }

  /**
   * Called when the spider is ready to process an HTML
   * URL.
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
  public void spiderProcessURL(URL url, SpiderParseHTML parse) {
    try {
      parse.readAll();
    } catch (IOException e) {
      logger.log(Level.INFO, "Error reading page:" + url.toString());
    }
  }

  /**
   * Called when the spider tries to process a URL but gets
   * an error.
   * 
   * @param url
   *          The URL that generated an error.
   */
  public void spiderURLError(URL url) {
    URL source;
    try {
      source = this.workloadManager.getSource(url);
      StringBuilder str = new StringBuilder();
      str.append("Bad URL:");
      str.append(url.toString());
      str.append(" found at ");
      str.append(source.toString());
      this.bad.add(str.toString());
    } catch (WorkloadException e) {
      e.printStackTrace();
    }
  }

}

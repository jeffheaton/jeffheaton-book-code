package com.heatonresearch.httprecipes.spider;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import com.heatonresearch.httprecipes.spider.workload.*;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * SpiderWorker: This class forms the workloads that are
 * passed onto the thread pool.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class SpiderWorker implements Runnable {
  /**
   * The logger.
   */
  private static Logger logger = Logger
      .getLogger("com.heatonresearch.httprecipes.spider.SpiderWorker");

  /**
   * The URL being processed.
   */
  private URL url;

  /**
   * The Spider object that this worker belongs to.
   */
  private Spider spider;

  /**
   * Construct a SpiderWorker object.
   * 
   * @param spider
   *          The spider this worker will work with.
   * @param url
   *          The URL to be processed.
   */
  public SpiderWorker(Spider spider, URL url) {
    this.spider = spider;
    this.url = url;
  }

  /*
   * This method is called by the thread pool to process one
   * single URL.
   */
  public void run() {
    URLConnection connection = null;
    InputStream is = null;

    try {
      logger.fine("Processing: " + this.url);
      // get the URL's contents
      connection = this.url.openConnection();
      connection.setConnectTimeout(this.spider.getOptions().timeout);
      connection.setReadTimeout(this.spider.getOptions().timeout);
      if (this.spider.getOptions().userAgent != null) {
        connection.setRequestProperty("User-Agent",
            this.spider.getOptions().userAgent);
      }

      // read the URL
      is = connection.getInputStream();

      // parse the URL
      if (connection.getContentType().equalsIgnoreCase("text/html")) {
        SpiderParseHTML parse = new SpiderParseHTML(connection.getURL(),
            new SpiderInputStream(is, null), this.spider);
        this.spider.getReport().spiderProcessURL(this.url, parse);
      } else {
        this.spider.getReport().spiderProcessURL(this.url, is);
      }

    } catch (IOException e) {
      logger.log(Level.INFO, "I/O error on URL:" + this.url.toString());
      try {
        this.spider.getWorkloadManager().markError(this.url);
      } catch (WorkloadException e1) {
        logger.log(Level.WARNING, "Error marking workload(1).", e);
      }
      this.spider.getReport().spiderURLError(this.url);
      return;
    } catch (Throwable e) {
      try {
        this.spider.getWorkloadManager().markError(this.url);
      } catch (WorkloadException e1) {
        logger.log(Level.WARNING, "Error marking workload(2).", e);
      }

      logger.log(Level.SEVERE, "Caught exception at URL:" + this.url.toString(), e);
      this.spider.getReport().spiderURLError(this.url);
      return;
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
        }
      }
    }

    try {
      // mark URL as complete
      this.spider.getWorkloadManager().markProcessed(this.url);
      logger.fine("Complete: " + this.url);
      if (!this.url.equals(connection.getURL())) {
        // save the URL(for redirect's)
        this.spider.getWorkloadManager().add(connection.getURL(), this.url,
            this.spider.getWorkloadManager().getDepth(connection.getURL()));
        this.spider.getWorkloadManager().markProcessed(connection.getURL());
      }
    } catch (WorkloadException e) {
      logger.log(Level.WARNING, "Error marking workload(3).", e);
    }

  }

}

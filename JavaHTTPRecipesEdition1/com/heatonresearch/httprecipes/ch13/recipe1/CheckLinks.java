package com.heatonresearch.httprecipes.ch13.recipe1;

import java.net.*;

import com.heatonresearch.httprecipes.spider.*;
import com.heatonresearch.httprecipes.spider.workload.*;
import com.heatonresearch.httprecipes.spider.workload.memory.*;

/**
 * Recipe #13.1: Scanning for broken links 
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * This recipe will spider the given URL looking for bad
 * links. Only URL's that have the same domain name are
 * examined. There is no depth limitation on the spider, it
 * will visit as much of the site as it can access.
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
public class CheckLinks {

  /**
   * Main method for the application.
   * 
   * @param args
   *          Holds the website to check (i.e.
   *          http://www.httprecipes.com/)
   */
  static public void main(String args[]) {
    try {
      if (args.length != 1) {
        System.out.println("Usage: CheckLinks [website to check]");
      } else {
        CheckLinks links = new CheckLinks();
        links.check(new URL(args[0]));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called by main to check a link. After
   * spidering through the site, the final list of bad links
   * is displayed.
   * 
   * @param url
   *          The URL to check for bad links.
   * @throws WorkloadException
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * 
   */
  public void check(URL url) throws InstantiationException,
      IllegalAccessException, ClassNotFoundException, WorkloadException {
    SpiderOptions options = new SpiderOptions();
    options.workloadManager = MemoryWorkloadManager.class.getCanonicalName();
    LinkReport report = new LinkReport();
    Spider spider = new Spider(options, report);
    spider.addURL(url, null, 1);
    spider.process();
    System.out.println(spider.getStatus());

    if (report.getBad().size() > 0) {
      System.out.println("Bad Links Found:");
      for (String str : report.getBad()) {
        System.out.println(str);
      }
    } else {
      System.out.println("No bad links were found.");
    }

  }

}
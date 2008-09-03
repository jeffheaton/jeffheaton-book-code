package com.heatonresearch.httprecipes.ch13.recipe2;

import java.io.*;
import java.net.*;

import com.heatonresearch.httprecipes.spider.*;
import com.heatonresearch.httprecipes.spider.workload.*;

/**
 * Recipe #13.2: Download Site 
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * The recipe downloads the contents of a web site. The
 * spider starts with a single URL and downloads everything
 * it can find that is on the same host.
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
public class DownloadSite {
  /**
   * The main method.
   * 
   * @param args
   *          Specifies the path to the config file, the
   *          path to download to, and the starting URL.
   */
  public static void main(String args[]) {
    try {
      if (args.length < 3) {
        System.out
            .println(
        "Usage: DownloadSite [Path to spider.conf] [Path to download to] [URL to download]");
      } else {
        DownloadSite download = new DownloadSite();
        download.download(args[0], new URL(args[2]), args[1]);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Download an entire site.
   * 
   * @param config
   *          The spider configuration file to use.
   * @param base
   *          The URL to start from.
   * @param local
   *          The local path to save files to.
   * @throws IOException
   *           Thrown if an I/O error occurs.
   * @throws SpiderException
   *           Thrown if there is an error connecting to the
   *           database.
   * @throws InstantiationException
   *           Thrown if there is an error parsing the
   *           config file.
   * @throws IllegalAccessException
   *           Thrown if the database driver can not be
   *           loaded.
   * @throws ClassNotFoundException
   *           Thrown if the database driver can not be
   *           located.
   * @throws WorkloadException
   *           Thrown if there is an error reading the
   *           config file.
   */
  public void download(String config, URL base, String local)
      throws IOException, SpiderException, InstantiationException,
      IllegalAccessException, ClassNotFoundException, WorkloadException {
    SpiderReport report = new SpiderReport(local);
    SpiderOptions options = new SpiderOptions();
    options.load(config);
    Spider spider = new Spider(options, report);
    spider.addURL(base, null, 1);
    spider.process();
    System.out.println(spider.getStatus());
  }
}

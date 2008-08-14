package com.heatonresearch.httprecipes.spider.logging;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * SpiderFormatter: A simple formatter to display JDK
 * logging.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class SpiderFormatter extends Formatter {

  /**
   * Called by the JDK Logging System to format log entries.
   * 
   * @param log
   *          The LogRecord to log.
   * @return The line to be logged.
   */  
  public String format(LogRecord log) {
    StringBuilder str = new StringBuilder();
    str.append(log.getLevel());
    str.append(':');
    str.append(new Date(log.getMillis()));
    str.append(':');
    str.append(log.getThreadID());
    str.append(':');
    str.append(log.getMessage());
    str.append('\n');
    if (log.getThrown() != null) {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(os);
      log.getThrown().printStackTrace(ps);
      str.append(os.toString());
      ps.close();
      try {
        os.close();
      } catch (IOException e) {
      }
    }

    return str.toString();
  }

}

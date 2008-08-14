package com.heatonresearch.httprecipes.spider;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * SpiderException: This exception is thrown when the spider
 * encounters an error.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class SpiderException extends Exception {

	/**
	 * Serial id for this class.
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Construct a SpiderException.
   * 
   * @param message
   *          The message for the exception.
   */
  public SpiderException(String message) {
    super(message);
  }

  /**
   * Wrap another exception as a SpiderException.
   * 
   * @param t
   *          The exception that occured.
   */
  public SpiderException(Throwable t) {
    super(t);
  }
}

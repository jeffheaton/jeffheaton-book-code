package com.heatonresearch.httprecipes.spider.workload;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * WorkloadException: This exception is thrown when the
 * workload manager encounters an error.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class WorkloadException extends Exception {

	/**
	 * Serial id for this class.
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Construct a WorkloadException with the specified
   * message.
   * 
   * @param message
   *          The exception message.
   */
  public WorkloadException(String message) {
    super(message);
  }

  /**
   * Called to wrap another exception as a
   * WorkloadException.
   * 
   * @param t
   *          An exception to be wrapped.
   */
  public WorkloadException(Throwable t) {
    super(t);
  }
}

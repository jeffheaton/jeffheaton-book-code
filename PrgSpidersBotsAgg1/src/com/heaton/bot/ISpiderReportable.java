package com.heaton.bot;

/**
 * This interface represents a class that
 * the spider can report to. As the spider
 * does its job, events from this interface
 * will be called.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public interface ISpiderReportable {

  /**
   * Called when the spider finds an internal
   * link. An internal link shares the same
   * host address as the URL that started
   * the spider.
   *
   * @param url The URL that was found by the spider.
   * @return true - The spider should add this URL to the workload.
   * false - The spider should not add this URL to the workload.
   */
  public boolean foundInternalLink(String url);

  /**
   * Called when the spider finds an external
   * link. An external link does not share the
   * same host address as the URL that started
   * the spider.
   *
   * @param url The URL that was found by the spider.
   * @return true - The spider should add this URL to the workload.
   * false - The spider should not add this URL to the workload.
   */
  public boolean foundExternalLink(String url);

  /**
   * Called when the spider finds a type of
   * link that does not point to another HTML
   * page(for example a mailto link).
   *
   * @param url The URL that was found by the spider.
   * @return true - The spider should add this URL to the workload.
   * false - The spider should not add this URL to the workload.
   */
  public boolean foundOtherLink(String url);

  /**
   * Called to actually process a page. This is where the
   * work actually done by the spider is usually preformed.
   *
   * @param page The page contents.
   * @param error true - This page resulted in an HTTP error.
   * false - This page downloaded correctly.
   */
  public void processPage(HTTP page);

  /**
   * Called to request that a page be processed.
   * This page was just downloaded by the spider.
   *
   * @param page The page contents.
   * @param error true - This page resulted in an HTTP error.
   * false - This page downloaded correctly.
   */
  public void completePage(HTTP page,boolean error);

  /**
   * This method is called by the spider to determine if
   * query strings should be removed. A query string
   * is the text that follows a ? on a URL. For example:
   *
   * http://www.heat-on.com/cgi-bin/login.jsp?id=a;pwd=b
   *
   * Everything to the left of, and including, the ? is
   * considered part of the query string.
   *
   * @return true - Query string should be removed.
   * false - Leave query strings as is.
   */
  public boolean getRemoveQuery();

  /**
   * Called when the spider has no more work.
   */
  public void spiderComplete();
}

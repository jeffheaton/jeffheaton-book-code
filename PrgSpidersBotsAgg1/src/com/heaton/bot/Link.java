package com.heaton.bot;

/**
 * Very simple data class to hold HTML links.
 * This class holds both the actual link URL,
 * as well as the ALT tag.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class Link {

  /**
   * The ALT attribute.
   */
  protected String _alt;

  /**
   * The link attribute.
   */
  protected String _href;


  /**
   * The link prompt text
   */
  protected String _prompt;


  /**
   * The constructor.
   *
   * @param alt The alt attribute for this link.
   * @param href The URL this link goes to.
   * @param prompt The prompt(if any) for this link.
   */

  public Link(String alt,String href,String prompt)
  {
    _alt = alt;
    _href = href;
    _prompt = prompt;
  }

  /**
   * Returns the ALT attribute.
   *
   * @return The ALT attribute.
   */
  public String getALT()
  {
    return _alt;
  }

  /**
   * Returns the link attribute.
   *
   * @return The link(HREF) attribute.
   */
  public String getHREF()
  {
    return _href;
  }

  /**
   * Returns the link attribute.
   *
   * @return The link(HREF) attribute.
   */
  public String getPrompt()
  {
    return _prompt;
  }


  /**
   * Returns the link URL.
   *
   * @return The link URL.
   */
  public String toString()
  {
    return _href;
  }

  /**
   * Set the prompt.
   *
   * @param prompt The prompt for this link.
   */
  public void setPrompt(String prompt)
  {
    _prompt = prompt;
  }
}

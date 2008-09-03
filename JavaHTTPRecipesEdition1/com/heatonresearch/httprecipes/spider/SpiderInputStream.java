package com.heatonresearch.httprecipes.spider;

import java.io.*;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * SpiderInputStream: This class is used by the spider to
 * both parse and save an InputStream.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class SpiderInputStream extends InputStream {
  /**
   * The InputStream to read from.
   */
  private InputStream is;

  /**
   * The OutputStream to write to.
   */
  private OutputStream os;

  /**
   * Construct the SpiderInputStream. Whatever is read from
   * the InputStream will also be written to the
   * OutputStream.
   * 
   * @param is
   *          The InputStream.
   * @param os
   *          The OutputStream.
   */
  public SpiderInputStream(InputStream is, OutputStream os) {
    this.is = is;
    this.os = os;
  }

  /*
   * Read a single byte from the stream. @throws IOException
   * If an I/O exception occurs. @return The character that
   * was read from the stream.
   */
  @Override
  public int read() throws IOException {
    int ch = this.is.read();
    if (this.os != null) {
      this.os.write(ch);
    }
    return ch;
  }

  /**
   * Set the OutputStream.
   * 
   * @param os
   *          The OutputStream.
   */
  public void setOutputStream(OutputStream os) {
    this.os = os;
  }

}

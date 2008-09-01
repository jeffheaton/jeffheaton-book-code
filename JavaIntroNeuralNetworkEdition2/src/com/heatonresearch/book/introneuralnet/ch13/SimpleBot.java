/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 *   
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */
package com.heatonresearch.book.introneuralnet.ch13;

import java.io.*;
import java.net.*;

public class SimpleBot
{
  /**
   * The size of the download buffer.
   */
  public static int BUFFER_SIZE = 8192;

  /**
   * This method downloads the specified URL into a Java
   * String. This is a very simple method, that you can
   * reused anytime you need to quickly grab all data from
   * a specific URL.
   * 
   * @param url The URL to download.
   * @return The contents of the URL that was downloaded.
   * @throws IOException Thrown if any sort of error occurs.
   */
  public String downloadPage(URL url) throws IOException
  {
    StringBuilder result = new StringBuilder();
    byte buffer[] = new byte[BUFFER_SIZE];

    InputStream s = url.openStream();
    int size = 0;

    do
    {
      size = s.read(buffer);
      if (size != -1)
        result.append(new String(buffer, 0, size));
    } while (size != -1);

    return result.toString();
  }

  /**
   * Run the example.
   * 
   * @param page The page to download.
   */
  public void go(String page)
  {
    try
    {
      URL u = new URL(page);
      String str = downloadPage(u);
      System.out.println(str);

    } catch (MalformedURLException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Typical Java main method, create an object, and then
   * call that object's go method.
   * 
   * @param args Website to access.
   */
  public static void main(String args[])
  {
    SimpleBot module = new SimpleBot();
    String page;
    if (args.length == 0)
      page = "http://www.httprecipes.com/1/3/time.php";
    else
      page = args[0];
    module.go(page);
  }
}

package com.heatonresearch.httprecipes.ch3.recipe4;

import java.io.*;
import java.net.*;

/**
 * Recipe #3.4: Downloading a Text File
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * Download a binary file, such as an image, from a URL.
 *
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not
 * publish the source code without the author's permission.
 * For more information on distributing this code, please
 * visit:
 *    http://www.heatonresearch.com/hr_legal.php
 *
 * @author Jeff Heaton
 * @version 1.1
 */
public class DownloadBinary
{
  // the size of a buffer
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

  public void saveBinaryPage(String filename, String page) throws IOException
  {
    OutputStream os = new FileOutputStream(filename);
    os.write(page.getBytes());
    os.close();
  }

  /**
   * Download a binary file from the Internet.
   * 
   * @param page The web URL to download.
   * @param filename The local file to save to.
   */
  public void download(String page, String filename)
  {
    try
    {
      URL u = new URL(page);
      String str = downloadPage(u);
      saveBinaryPage(filename, str);

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
   * start that object.
   * 
   * @param args URL to download, and local file.
   */
  public static void main(String args[])
  {
    try
    {
      if (args.length != 2)
      {
        DownloadBinary d = new DownloadBinary();
        d.download("http://www.httprecipes.com/1/3/sea.jpg", "./sea2.jpg");
      } else
      {
        DownloadBinary d = new DownloadBinary();
        d.download(args[0], args[1]);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

package com.heatonresearch.httprecipes.ch6.recipe5;

import java.io.*;
import java.net.*;

import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #6.5: Parse and Extract Images
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to parse and extract(download) images
 * from an HTML page.
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

public class ExtractImages
{
  /*
   * The size buffer to use for downloading.
   */ 
  public static int BUFFER_SIZE = 8192;

  /**
   * Download a binary file from the Internet.
   * 
   * @param page The web URL to download.
   * @param filename The local file to save to.
   */
  public void downloadBinaryPage(URL url, File file) throws IOException
  {
    byte buffer[] = new byte[BUFFER_SIZE];
    OutputStream os = new FileOutputStream(file);

    InputStream is = url.openStream();
    int size = 0;

    do
    {
      size = is.read(buffer);
      if (size != -1)
        os.write(buffer, 0, size);
    } while (size != -1);

    os.close();
    is.close();
  }

  /**
   * Extract just the filename from a URL.
   * @param u The URL to extract from.
   * @return The filename.
   */
  private String extractFile(URL u)
  {
    String str = u.getFile();

    // strip off path information
    int i = str.lastIndexOf('/');
    if (i != -1)
      str = str.substring(i + 1);
    return str;
  }

  /**
   * Process the specified URL and download the images.
   * @param url The URL to process.
   * @param saveTo A directory to save the images to.
   * @throws IOException Thrown if any error occurs.
   */
  public void process(URL url, File saveTo) throws IOException
  {
    InputStream is = url.openStream();
    ParseHTML parse = new ParseHTML(is);

    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("img"))
        {
          String src = tag.getAttributeValue("src");
          URL u = new URL(url, src);
          String filename = extractFile(u);
          File saveFile = new File(saveTo, filename);
          this.downloadBinaryPage(u, saveFile);
        }
      }
    }
  }

  /**
   * The main method, create a new instance of the object and call
   * process.
   * @param args Not used.
   */  
  public static void main(String args[])
  {
    try
    {
      URL u = new URL("http://www.httprecipes.com/1/6/image.php");
      ExtractImages parse = new ExtractImages();
      parse.process(u, new File("."));
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

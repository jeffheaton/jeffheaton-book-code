package com.heatonresearch.httprecipes.ch3.recipe5;

import java.io.*;
import java.net.*;

/**
 * Recipe #3.5: Downloading an Image
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * Download a text file, such as a HTML page, from a URL.
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
public class DownloadText
{

  /**
   * Download the specified text page.
   * 
   * @param page The URL to download from.
   * @param filename The local file to save to.
   */
  public void download(String page, String filename)
  {
    try
    {
      URL u = new URL(page);
      InputStream is = u.openStream();
      OutputStream os = new FileOutputStream(filename);
      downloadText(is, os);
      is.close();
      os.close();

    } catch (MalformedURLException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Download a text file, and convert the line breaks for whatever
   * the current operating system is.
   * 
   * @param is The input stream to read from.
   * @param os The output stream to write to..
   */
  private void downloadText(InputStream is, OutputStream os) throws IOException
  {
    byte lineSep[] = System.getProperty("line.separator").getBytes();
    int ch = 0;
    boolean inLineBreak = false;
    boolean hadLF = false;
    boolean hadCR = false;

    do
    {
      ch = is.read();
      if (ch != -1)
      {
        if ((ch == '\r') || (ch == '\n'))
        {
          inLineBreak = true;
          if (ch == '\r')
          {
            if (hadCR)
              os.write(lineSep);
            else
              hadCR = true;
          } else
          {
            if (hadLF)
              os.write(lineSep);
            else
              hadLF = true;
          }
        } else
        {
          if (inLineBreak)
          {
            os.write(lineSep);
            hadCR = hadLF = inLineBreak = false;
          }
          os.write(ch);
        }
      }
    } while (ch != -1);
  }

  /**
   * Typical Java main method, create an object, and then
   * pass the parameters on if provided, otherwise default.
   * 
   * @param args URL to download, and local file.
   */
  public static void main(String args[])
  {
    try
    {
      if (args.length != 2)
      {
        DownloadText d = new DownloadText();
        d.download("http://www.httprecipes.com/1/3/text.php", "./text.html");
      } else
      {
        DownloadText d = new DownloadText();
        d.download(args[0], args[1]);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

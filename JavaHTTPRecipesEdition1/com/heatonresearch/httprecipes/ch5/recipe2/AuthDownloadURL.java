package com.heatonresearch.httprecipes.ch5.recipe2;

import java.net.*;
import java.io.*;

/**
 * Recipe #5.2: Downloading a URL(text or binary)
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to download a text or binary file,
 * using HTTP authentication.
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
public class AuthDownloadURL
{
  public static int BUFFER_SIZE = 8192;

  /**
   * Download either a text or binary file from a URL.
   * The URL's headers will be scanned to determine the
   * type of tile.
   * 
   * @param remoteURL The URL to download from.
   * @param localFile The local file to save to.
   * @throws IOException Exception while downloading.
   */
  public void download(URL remoteURL, File localFile, String uid, String pwd)
      throws IOException
  {
    HttpURLConnection http = (HttpURLConnection) remoteURL.openConnection();
    addAuthHeader(http, uid, pwd);
    InputStream is = http.getInputStream();
    OutputStream os = new FileOutputStream(localFile);
    String type = http.getHeaderField("Content-Type").toLowerCase().trim();
    if (type.startsWith("text"))
      downloadText(is, os);
    else
      downloadBinary(is, os);
    is.close();
    os.close();
    http.disconnect();
  }

  /**
   * Add HTTP authntication headers to the specified HttpURLConnection
   * object.
   * @param http The HTTP connection.
   * @param uid The user id.
   * @param pwd The password.
   */
  private void addAuthHeader(HttpURLConnection http, String uid, String pwd)
  {
    if ((uid.length() == 0) && (pwd.length() == 0))
      return;
    String hdr = uid + ":" + pwd;
    String encode = base64Encode(hdr);
    http.addRequestProperty("Authorization", "Basic " + encode);
  }

  /**
   * Encodes a string in base64.
   *
   * @param s      The string to encode.
   * @return The encoded string.
   */
  static public String base64Encode(String s)
  {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    Base64OutputStream out = new Base64OutputStream(bout);
    try
    {
      out.write(s.getBytes());
      out.flush();
    } catch (IOException e)
    {
    }

    return bout.toString();
  }

  /**
   * Overloaded version of download that accepts strings,
   * rather than URL objects.
   * 
   * @param remoteURL The URL to download from.
   * @param localFile The local file to save to.
   * @throws IOException Exception while downloading.
   */
  public void download(String remoteURL, String localFile, String uid,
      String pwd) throws IOException
  {
    download(new URL(remoteURL), new File(localFile), uid, pwd);
  }

  /**
   * Download a text file, which means convert the line
   * ending characters to the correct type for the 
   * operating system that is being used.
   * 
   * @param is The input stream, which is the URL.
   * @param os The output stream, a local file.
   * @throws IOException Exception while downloading.
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
   * Download a binary file.  Which means make an exact 
   * copy of the incoming stream.
   * 
   * @param is The input stream, which is the URL.
   * @param os The output stream, a local file.
   * @throws IOException Exception while downloading.
   */
  private void downloadBinary(InputStream is, OutputStream os)
      throws IOException
  {
    byte buffer[] = new byte[BUFFER_SIZE];

    int size = 0;

    do
    {
      size = is.read(buffer);
      if (size != -1)
        os.write(buffer, 0, size);
    } while (size != -1);
  }

  /**
   * Typical Java main method, create an object, and then
   * start the object passing arguments. If insufficient 
   * arguments are provided, then display startup 
   * instructions.
   * 
   * @param args Program arguments.
   */
  public static void main(String args[])
  {
    try
    {
      AuthDownloadURL d = new AuthDownloadURL();

      if (args.length != 4)
      {
        d.download("https://www.httprecipes.com/1/5/secure/", 
            "./test.html",
            "testuser", 
            "testpassword");
      } else
      {

        d.download(args[0], args[1], args[2], args[3]);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
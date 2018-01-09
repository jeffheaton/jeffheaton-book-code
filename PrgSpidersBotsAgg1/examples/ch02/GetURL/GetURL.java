import java.io.*;
import java.net.*;

/**
 * Example program from Chapter 2
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 * This program uses the standard Java URL class to open a
 * connection to a web page and download the contents.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
class GetURL
{

  /**
   * This method will display the URL specified by the parameter.
   *
   * @param u The URL to display.
   */
  static protected void getURL(String u)
  {
    URL url;
    InputStream is;
    InputStreamReader isr;
    BufferedReader r;
    String str;

    try
    {
      System.out.println("Reading URL: " + u );
      url = new URL(u);
      is = url.openStream();
      isr = new InputStreamReader(is);
      r = new BufferedReader(isr);
      do
      {
        str = r.readLine();
        if(str!=null)
          System.out.println( str );
      } while( str!= null );
    }
    catch(MalformedURLException e)
    {
      System.out.println("Must enter a valid URL");
    }
    catch(IOException e)
    {
      System.out.println("Can't connect");
    }
  }

  /**
   * Program entry point.
   *
   * @param args Command line arguments.  Specified the URL to download.
   */
  static public void main(String args[])
  {
    if( args.length<1)
      System.out.println("Usage: GetURL ");
    else
      getURL( args[0] );
  }
}


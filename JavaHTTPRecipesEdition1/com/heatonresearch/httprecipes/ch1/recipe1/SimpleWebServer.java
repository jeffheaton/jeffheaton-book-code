package com.heatonresearch.httprecipes.ch1.recipe1;

import java.io.*;
import java.net.*;

/**
 * Recipe #1.1: Very Simple Web Server
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * A simple web server that will respond to every request
 * with "Hello World".
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
public class SimpleWebServer
{
  /*
   * The server socket.
   */
  private ServerSocket serverSocket;

  /**
   * Construct the web server to listen on the specified 
   * port.
   * 
   * @param port The port to use for the server.
   * @throws IOException Thrown if any sort of error occurs.
   */
  public SimpleWebServer(int port) throws IOException
  {
    serverSocket = new ServerSocket(port);
  }

  /**
   * The run method endlessly waits for connections. 
   * As each connection is opened (from web browsers)
   * the connection is passed off to handleClientSession.
   */
  public void run()
  {
    for (;;)
    {
      try
      {
        Socket clientSocket = serverSocket.accept();
        handleClientSession(clientSocket);
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Handle a client session. This method displays the incoming
   * HTTP request and responds with a "Hello World" response.
   * 
   * @param url The URL to download.
   * @return The contents of the URL that was downloaded.
   * @throws IOException Thrown if any sort of error occurs.
   */
  private void handleClientSession(Socket socket) throws IOException
  {
    // setup to read from the socket in lines
    InputStream is = socket.getInputStream();
    InputStreamReader inputStreamReader = new InputStreamReader(is);
    BufferedReader in = new BufferedReader(inputStreamReader);

    // setup to write to socket in lines
    OutputStream os = socket.getOutputStream();
    PrintStream out = new PrintStream(os);

    // read in the first line
    System.out.println("**New Request**");
    String first = in.readLine();
    System.out.println(first);

    // read in headers and post data
    String line;
    do
    {
      line = in.readLine();
      if(line!=null)
      System.out.println(line);
    } while (line!=null && line.trim().length() > 0);

    // write the HTTP response
    out.println("HTTP/1.1 200 OK");
    out.println("");
    out.println("<html>");
    out.println("<head><title>Simple Web Server</title></head>");
    out.println("<body>");
    out.println("<h1>Hello World</h1>");
    out.println("<//body>");
    out.println("</html>");

    // close everything up
    out.close();
    in.close();
    socket.close();

  }

  /**
   * Read in the arguments and start the server.
   * 
   * @param args Web server port.
   */
  public static void main(String args[])
  {
    try
    {
      if (args.length < 1)
      {
        System.out.println("Usage:\njava SimpleWebServer [port]");
      } else
      {
        int port;
        try
        {
          port = Integer.parseInt(args[0]);
          SimpleWebServer server = new SimpleWebServer(port);
          server.run();
        } catch (NumberFormatException e)
        {
          System.out.println("Invalid port number");
        }

      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

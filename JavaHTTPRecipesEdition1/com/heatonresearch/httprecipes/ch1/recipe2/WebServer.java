package com.heatonresearch.httprecipes.ch1.recipe2;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Recipe #1.2: Simple File Based Web Server
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * A simple web server that exposes a single directory as the
 * root of a web site.  Only the most basic web server functions
 * are provided, such as index.html redirect.
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
public class WebServer
{
  /*
   * The server socket.
   */
  private ServerSocket serverSocket;

  /*
   * The directory to contain HTML and image files.
   */
  private String httproot;

  /**
   * Construct the web server to listen on the specified 
   * port.
   * 
   * @param port The port to use for the server.
   * @param httproot The root directory for HTML and image files.
   * @throws IOException Thrown if any sort of error occurs.
   */
  public WebServer(int port, String httproot) throws IOException
  {
    serverSocket = new ServerSocket(port);
    this.httproot = httproot;
  }

  /**
   * The run method endlessly waits for connections.
   * As each connection is opened(from web browsers)
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
   * Add a slash to the end of a path, if there is not a slash
   * there already.  This method adds the correct type of slash,
   * depending on the operating system.
   * 
   * @param path The path to add a slash to.
   * @return The path with a slash added.
   */
  private String addSlash(String path)
  {
    path = path.trim();
    if (path.endsWith("" + File.separatorChar))
      return path;
    else
      return path + File.separatorChar;
  }

  /**
   * Handle a client session. This method displays the incoming
   * HTTP request and passes the response off to either sendFile
   * or error.
   * 
   * @param socket The client socket.
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

    // read in the first line
    System.out.println("**New Request**");
    String first = in.readLine();
    System.out.println(first);

    // read in headers and post data
    String line;
    do
    {
      line = in.readLine();
      System.out.println(line);
    } while (line!=null && line.trim().length() > 0);

    // write the HTTP response
    StringTokenizer tok = new StringTokenizer(first);
    String verb = (String) tok.nextElement();
    String path = (String) tok.nextElement();

    if (verb.equalsIgnoreCase("GET"))
      sendFile(os, path);
    else
      error(os, 500, "Unsupported command");

    // close everything up
    os.close();
    in.close();
    socket.close();
  }

  /**
   * Determine the correct "content type" based on the file
   * extension.
   * 
   * @param path The file being transfered.
   * @return The correct content type for this file.
   * @throws IOException Thrown if any sort of error occurs.
   */
  private String getContent(String path)
  {
    path = path.toLowerCase();
    if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
      return "image/jpeg";
    else if (path.endsWith(".gif"))
      return "image/gif";
    else if (path.endsWith(".png"))
      return "image/png";
    else
      return "text/html";
  }

  /**
   * Send a disk file.  The path passed in is from the URL, this
   * URL is translated into a local disk file, which is then
   * transfered.
   * 
   * @param out The output stream.
   * @param path The file requested from the URL.
   * @throws IOException Thrown if any sort of error occurs.
   */
  private void sendFile(OutputStream out, String path) throws IOException
  {
    // parse the file by /'s and build a local file
    StringTokenizer tok = new StringTokenizer(path, "/", true);
    System.out.println(path);
    String physicalPath = addSlash(httproot);

    while (tok.hasMoreElements())
    {
      String e = (String) tok.nextElement();
      if (!e.trim().equalsIgnoreCase(File.separator))
      {
        if (e.equals("..") || e.equals("."))
        {
          error(out, 500, "Invalid request");
          return;
        }
        physicalPath += e;
      } else
        physicalPath = addSlash(physicalPath);

    }

    // if there is no file specified, default
    // to index.html

    if (physicalPath.endsWith(File.separator))
    {
      physicalPath = physicalPath + "index.html";
    }

    // open the file and send it if it exists
    File file = new File(physicalPath);
    if (file.exists())
    {
      // send the file
      FileInputStream fis = new FileInputStream(file);
      byte buffer[] = new byte[(int) file.length()];
      fis.read(buffer);
      fis.close();
      this.transmit(out, 200, "OK", buffer, getContent(physicalPath));
    }
    // file does not exist, so send file not found
    else
    {
      this.error(out, 404, "File Not Found");
    }
  }

  /**
   * Transmit a HTTP response.  All responses are handled by
   * this method.  
   * 
   * @param out The output stream.
   * @param code The response code, i.e. 404 for not found.
   * @param message The message, usually OK or error message.
   * @param body The data to be transfered.
   * @param content The content type.
   * @throws IOException Thrown if any sort of error occurs.
   */
  private void transmit(OutputStream out, int code, String message,
      byte body[], String content) throws IOException
  {
    StringBuilder headers = new StringBuilder();
    headers.append("HTTP/1.1 ");
    headers.append(code);
    headers.append(' ');
    headers.append(message);
    headers.append("\n");
    headers.append("Content-Length: " + body.length + "\n");
    headers.append("Server: Heaton Research Example Server\n");
    headers.append("Connection: close\n");
    headers.append("Content-Type: " + content + "\n");
    headers.append("\n");
    out.write(headers.toString().getBytes());
    out.write(body);

  }

  /**
   * Display an error to the web browser.
   * 
   * @param out The output stream.
   * @param code The response code, i.e. 404 for not found.
   * @param message The error that occurred.
   * @throws IOException Thrown if any sort of error occurs.
   */
  private void error(OutputStream out, int code, String message)
      throws IOException
  {
    StringBuilder body = new StringBuilder();
    body.append("<html><head><title>");
    body.append(code + ":" + message);
    body.append("</title></head><body><p>An error occurred.</p><h1>");
    body.append(code);
    body.append("</h1><p>");
    body.append(message);
    body.append("</p></body></html>");
    transmit(out, code, message, body.toString().getBytes(), "text/html");
  }

  /**
   * Read in the arguments and start the server.
   * 
   * @param args Web server port and http root directory.
   */
  public static void main(String args[])
  {
    try
    {
      if (args.length < 2)
      {
        System.out.println("Usage:\njava WebServer [port] [http root path]");
      } else
      {
        int port;
        try
        {
          port = Integer.parseInt(args[0]);
          WebServer server = new WebServer(port, args[1]);
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

package com.heatonresearch.httprecipes.ch8.recipe1;

import java.io.*;
import java.net.*;
import java.util.*;

import com.heatonresearch.httprecipes.html.*;

/**
 * Recipe #8.1: Cookieless login and parse
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to login to a system and download 
 * some data.  This site does not use cookies to keep the login
 * session, but rather passes a session parameter on the URL.
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
public class Cookieless
{
  /**
   * This method is called to log into the system and return 
   * a session id.  Once you have the session ID you can call
   * the search function to perform searches.
   * @param uid The user id to use for login.
   * @param pwd The password to use for login.
   * @return The session id if login was successful, null if it was not.
   * @throws IOException If an exception occurs while reading.
   */
  private String login(String uid, String pwd) throws IOException
  {
    URL url = new URL("http://www.httprecipes.com/1/8/cookieless.php");
    URLConnection http = url.openConnection();
    http.setDoOutput(true);
    OutputStream os = http.getOutputStream();
    FormUtility form = new FormUtility(os, null);
    form.add("uid", uid);
    form.add("pwd", pwd);
    form.add("action", "Login");
    form.complete();
    http.getInputStream();
    String query = http.getURL().getQuery();
    if (query != null)
    {
      Map<String, String> map = FormUtility.parse(query);
      return map.get("session");
    } else
      return null;
  }

  /** Advance to the specified HTML tag.
   * @param parse The HTML parse object to use.
   * @param tag The HTML tag.
   * @param count How many tags like this to find.
   * @return True if found, false otherwise.
   * @throws IOException If an exception occurs while reading.
   */
  private boolean advance(ParseHTML parse, String tag, int count)
      throws IOException
  {
    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        if (parse.getTag().getName().equalsIgnoreCase(tag))
        {
          count--;
          if (count <= 0)
            return true;
        }
      }
    }
    return false;
  }

  /**
   * Use the session to search for the specified state or capital.  The search
   * method can be called multiple times per login.
   * @param session The session to use.
   * @param search The search string to use.
   * @param type What to search for(s=state,c=capital).
   * @return A list of states or capitals.
   * @throws IOException Thrown if a communication failure occurs
   */
  public List<String> search(String session, String search, String type)
      throws IOException
  {
    String listType = "ul";
    String listTypeEnd = "/ul";
    StringBuilder buffer = new StringBuilder();
    boolean capture = false;
    List<String> result = new ArrayList<String>();

    // build the URL
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    FormUtility form = new FormUtility(bos, null);
    form.add("search", search);
    form.add("type", type);
    form.add("action", "Search");
    form.complete();

    URL url = new URL("http://www.httprecipes.com/1/8/menunc.php?session="
        + session);
    URLConnection http = url.openConnection();
    http.setDoOutput(true);
    OutputStream os = http.getOutputStream();
    // perform the post

    os.write(bos.toByteArray());

    // read the results
    InputStream is = http.getInputStream();
    ParseHTML parse = new ParseHTML(is);

    // parse from the URL

    advance(parse, listType, 0);

    int ch;
    while ((ch = parse.read()) != -1)
    {
      if (ch == 0)
      {
        HTMLTag tag = parse.getTag();
        if (tag.getName().equalsIgnoreCase("li"))
        {
          if (buffer.length() > 0)
            result.add(buffer.toString());
          buffer.setLength(0);
          capture = true;
        } else if (tag.getName().equalsIgnoreCase("/li"))
        {
          result.add(buffer.toString());
          buffer.setLength(0);
          capture = false;
        } else if (tag.getName().equalsIgnoreCase(listTypeEnd))
        {
          result.add(buffer.toString());
          break;
        }
      } else
      {
        if (capture)
          buffer.append((char) ch);
      }
    }

    return result;
  }

  /**
   * Called to login to the site and download a list of states or capitals.
   * @param uid The user id to use for login.
   * @param pwd The password to use for login.
   * @param search The search string to use.
   * @param type What to search for(s=state,c=capital).
   * @throws IOException Thrown if a communication failure occurs
   */
  public void process(String uid, String pwd, String search, String type)
      throws IOException
  {
    String session = login(uid, pwd);
    if (session != null)
    {
      List<String> list = search(session, search, type);
      for (String item : list)
      {
        System.out.println(item);
      }
    } else
    {
      System.out.println("Error logging in.");
    }
  }

  /*
   * Simple main method to create an instance of the object and
   * call the process method.
   */
  public static void main(String args[])
  {
    try
    {
      Cookieless cookieless = new Cookieless();
      cookieless.process("falken", "joshua", "Mi", "s");
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

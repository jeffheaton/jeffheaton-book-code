package com.heatonresearch.httprecipes.ch7.recipe3;

import java.io.*;
import java.net.*;

import com.heatonresearch.httprecipes.html.FormUtility;

/**
 * Recipe #7.3: HTTP Upload
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to upload a file through a form, using
 * the HTTP upload.  This is supported using a multipart form.
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
public class FormUpload
{

  /**
   * Upload a file.
   * @param uid The user id for the form.
   * @param pwd The password for the form.
   * @param file The file to upload.
   * @throws IOException Thrown if any I/O error occurs.
   */
  public void upload(String uid, String pwd, File file) throws IOException
  {
    // get the boundary used for the multipart upload
    String boundary = FormUtility.getBoundary();
    URLConnection http = (new URL("http://www.httprecipes.com/1/7/uploader.php"))
        .openConnection();
    http.setDoOutput(true);
    // specify that we will use a multipart form
    http.setRequestProperty("Content-Type", "multipart/form-data; boundary="
        + boundary);
    OutputStream os = http.getOutputStream();
    // construct a form
    FormUtility form = new FormUtility(os, boundary);
    form.add("uid", uid);
    form.add("pwd", pwd);
    form.add("uploadedfile", file);
    form.complete();
    // perform the upload
    InputStream is = http.getInputStream();
    is.close();
  }

  /**
   * The main method attempts to obtain the user id, password 
   * and local filename for the upload. 
   * @param args The user id, password and local filename.
   */
  public static void main(String args[])
  {
    try
    {
      String uid = "";
      String pwd = "";
      String filename = "";

      if (args.length < 3)
      {
        System.out.println("Usage:");
        System.out
            .println("java FormUpload [User ID] [Password] [File to upload]");
      } else
      {
        uid = args[0];
        pwd = args[1];
        filename = args[2];

        FormUpload upload = new FormUpload();
        upload.upload(uid, pwd, new File(filename));
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

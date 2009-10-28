/**
 * InvalidString
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 5
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how NOT to compare two strings.  Do not use == on strings!
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

import java.io.*;

class InvalidString
{
  public static void main(String args[])
  {
    try
    {
      InputStreamReader inputStreamReader =
        new InputStreamReader ( System.in );
      BufferedReader in =
        new BufferedReader ( inputStreamReader );
      System.out.print(
        "What is your favorite color? ");
      String color = in.readLine();
      if( color=="red" )
        System.out.println(
          "My favorite color is red too!");
    }
    catch(IOException e)
    {
    }
  }
}
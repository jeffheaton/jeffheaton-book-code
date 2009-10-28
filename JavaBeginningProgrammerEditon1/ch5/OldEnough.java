/**
 * OldEnough
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 5
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class checks to see if you are old enough to vote in the USA.
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

class OldEnough
{
  public static void main(String args[])
  {
    try
    {
      InputStreamReader inputStreamReader =
        new InputStreamReader ( System.in );
      BufferedReader in =
        new BufferedReader ( inputStreamReader );
      System.out.print("How old are you? ");
      String age = in.readLine();
      int iAge = Integer.parseInt(age);
      if( iAge>=18 )
      {
        System.out.println(
          "You are old enough to vote in the United States.");
      }
      if( iAge<18 )
      {
        System.out.println(
          "You are not old enough to vote in" +
          " the United States");
      }
    }
    catch(NumberFormatException e)
    {
      System.out.println("That is not a valid age.");
    }
    catch(IOException e)
    {
    }
  }
}
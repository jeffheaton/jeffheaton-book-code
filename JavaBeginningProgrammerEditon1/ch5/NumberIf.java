/**
 * NumberIf
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 5
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to use "if statements" with numbers.
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

class NumberIf
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
        "Enter a number between 1 and 5? ");

      String num = in.readLine();
      int number = Integer.parseInt(num);
      if( number==1 )
      {
        System.out.println("You entered One.");
      }
      else if( number==2 )
      {
        System.out.println("You entered Two.");
      }
      else if( number==3 )
      {
        System.out.println("You entered Three.");
      }
      else if( number==4 )
      {
        System.out.println("You entered Four.");
      }
      else if( number==5 )
      {
        System.out.println("You entered Five.");
      }
      else
      {
        System.out.println(
          "You did not enter a number between 1 and 5.");
      }
    }
    catch(NumberFormatException e)
    {
      System.out.println(
        "You must enter a valid number.");
    }
    catch(IOException e)
    {
    }
  }
}
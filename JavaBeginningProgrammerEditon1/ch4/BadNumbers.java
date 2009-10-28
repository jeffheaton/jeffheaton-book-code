/**
 * BadNumbers
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 4
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to input numbers, and handle an
 * invalid number.
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

public class BadNumbers
{
  public static void main(String args[])
  {
    try
    {
      InputStreamReader inputStreamReader =
        new InputStreamReader ( System.in );
      BufferedReader in =
        new BufferedReader ( inputStreamReader );
      System.out.print("Enter a length in miles? ");
      String miles = in.readLine();
      double dMiles = Double.parseDouble(miles);
      double dKilometers = 1.609344 *dMiles;
      System.out.println("That is " + dKilometers +
        " kilometers.");
    }
    catch(NumberFormatException e)
    {
      System.out.println(
        "You entered an invalid number.");
    }
    catch(IOException e)
    {
    }
  }
}
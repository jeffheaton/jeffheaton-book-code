/**
 * UserInput
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 3
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class asks the user their name, and then says 
 * hello to the user.
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

public class UserInput
{
  /**
   * Main entry point for example.
   * @param args Not used.
   */
  public static void main(String args[])
  {
    try
    {
      BufferedReader userInput =
        new BufferedReader(
        new InputStreamReader(System.in));
      System.out.print("What is your name? ");
      String str = userInput.readLine();
      System.out.println("Hello " + str);
    }
    catch(IOException e)
    {
      System.out.println("IO Exception");
    }
  }
}
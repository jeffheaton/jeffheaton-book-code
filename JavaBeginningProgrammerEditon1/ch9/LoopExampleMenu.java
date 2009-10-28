/**
 * LoopExampleMenu
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 9
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to use a do/while for a simple menu.
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

public class LoopExampleMenu
{
  public static void main(String args[])
  {
    InputStreamReader inputStreamReader =
      new InputStreamReader ( System.in );
    BufferedReader stdin =
      new BufferedReader ( inputStreamReader );
    String response;
    try
    {
      do
      {
        System.out.println("A> Menu option a");
        System.out.println("B> Menu option b");
        System.out.println("C> Menu option c");
        System.out.println("Q> Quit");
        System.out.print("Select>");
        response = stdin.readLine();
      } while( !response.equals("q") &&
        !response.equals("Q") );
    }
    catch(IOException e)
    {
    }
  }
}
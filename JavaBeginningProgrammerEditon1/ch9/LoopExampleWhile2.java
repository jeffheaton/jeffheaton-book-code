/**
 * LoopExampleWhile2
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 9
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how a while loop may not execute even once.
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

public class LoopExampleWhile2
{
  public static void main(String args[])
  {
    int i = 11;

    while( i<=10 )
    {
      System.out.println("Loop:" + i );
      i = i + 1;
    }
  }
}
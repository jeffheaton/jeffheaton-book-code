/**
 * NestedLoop
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 9
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows a nested loop.
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

public class NestedLoop
{
  public static void main(String args[] )
  {
    for( int outer = 1; outer<=10; outer ++ )
    {
      for( int inner = 1; inner<outer ; inner ++ )
      {
        System.out.print( " " + inner );
      }
      System.out.println("");
    }
  }
}
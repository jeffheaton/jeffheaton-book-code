/**
 * MyClassArgument
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 6
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to pass an argument to a method.
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

public class MyClassArgument
{
  public static void myMethod(int i)
  {
    i++;
  }

  public static void main(String args[])
  {
    int i = 10;
    myMethod(i);
    System.out.println("The value of i is " + i );
  }
}
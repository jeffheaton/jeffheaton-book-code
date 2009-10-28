/**
 * MyClassStaticFunction
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 6
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to use static functions.
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

public class MyClassStaticFunction
{
  public static int multiply(int i)
  {
    return(i*5);
  }
  
  public static void main(String args[])
  {
    int j = 5;
    j = multiply(j);
    System.out.println(j);
  }
}
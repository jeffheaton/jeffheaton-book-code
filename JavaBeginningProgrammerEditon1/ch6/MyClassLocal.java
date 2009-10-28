/**
 * MyClassLocal
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 6
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how local variables do not keep their values between
 * method calls.
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

public class MyClassLocal
{
  public static void myMethod()
  {
    int x = 0;
    System.out.println( "Value of x:" + x );
    x++;
  }
  
  public static void main(String args[])
  {
    myMethod();
    myMethod();
    myMethod();
  }
}
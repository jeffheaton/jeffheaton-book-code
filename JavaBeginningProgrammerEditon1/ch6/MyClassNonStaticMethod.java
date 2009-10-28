/**
 * MyClassNonStaticMethod
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 6
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to use non-static instance variables to hold values.
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

public class MyClassNonStaticMethod
{
  public int total; // instance variable
  
  public void add(int i)
  {
    total = total + i;
  }
  
  public static void main(String args[])
  {
    MyClassNonStaticMethod myObject =
      new MyClassNonStaticMethod();
    MyClassNonStaticMethod myOtherObject =
      new MyClassNonStaticMethod();
    myObject.add(5);
    myOtherObject.add(3);
    myObject.add(10);
    myObject.add(10);
    System.out.println("Total myObject is:" +
      myObject.total );
    System.out.println("Total myOtherObject is:" +
      myOtherObject.total );
  }
}
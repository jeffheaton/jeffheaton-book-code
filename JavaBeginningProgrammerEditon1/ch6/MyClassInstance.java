/**
 * MyClassInstance
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 6
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how use instance variables.
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

class MyClassInstance
{
  public int x = 0;

  public static void main(String args[])
  {
    MyClassInstance myclass1 = new MyClassInstance();
    MyClassInstance myclass2 = new MyClassInstance();

    myclass1.x = 10;
    myclass2.x = 15;

    System.out.println("Current value of myclass1.x is "+myclass1.x);
  }
}
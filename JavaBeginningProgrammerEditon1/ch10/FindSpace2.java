/**
 * FindSpace2
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 10
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class finds the second space(after index 5) in a string.
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

public class FindSpace2
{
  public static void main(String args[])
  {
    String str = "Java is Fun";
    int i = str.indexOf(" ",5);
    System.out.println( "The space is at: "+i );
  }
}
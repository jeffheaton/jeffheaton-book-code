/**
 * GetChars
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 10
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class displays all of the characters that make up a string.
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

public class GetChars
{
  public static void main(String args[])
  {
    String str = "Java is Fun";
    for( int i=0; i<str.length(); i++ )
      System.out.println("Character #" +
        i + " is " + str.charAt(i) );
  }
}
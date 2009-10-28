/**
 * RemoveChar
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 10
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This removes a character from the string.
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

public class RemoveChar 
{ 
  public static String removeChar(String str,int i) 
  { 
    String first = str.substring(0,i); 
    String last = str.substring(i+1,str.length()); 
    str = first+last; 
    return str; 
  } 
  public static void main(String args[]) 
  { 
    String str = "Java is Fun"; 
    str = removeChar(str,5); 
    System.out.println(str); 
  } 
} 

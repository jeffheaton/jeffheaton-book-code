/**
 * StudentList6
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 11
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to display values from the array, with a loop.
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

public class StudentList6 
{ 
  public static void main(String args[]) 
  { 
    String studentList[]; 
    studentList = new String[3]; 
    studentList[0] = "Smith, John"; 
    studentList[1] = "Jones, Bill"; 
    studentList[2] = "Thomson, Jerry"; 
    // now print the students 
    for( int i=0;i<3;i++ ) 
    { 
      System.out.println( studentList[i] ); 
    } 
  } 
} 

/**
 * TestStudent
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 13
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class tests the student class.
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

public class TestStudent 
{ 
  public static void main(String args[]) 
  { 
    Student student = new Student(); 
    try 
    { 
      student.setFirst("John"); 
      student.setLast("Smith"); 
      student.setStudentNumber(1); 
      student.setType("Freshman"); 
      // now print it 
      System.out.println( 
          "Student: first=" + student.getFirst() + 
          "last=" + student.getLast() + 
          "studentNumber=" + 
          student.getStudentNumber() + 
          "type=" + student.getType() ); 
      // now cause an exception 
      student.setType("Unknown"); 
    } 
    catch(TypeException e) 
    { 
      System.out.println("Error:" + e.getMessage() ); 
    } 
  } 
} 
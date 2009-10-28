/**
 * StudentList
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 12
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to extend the student list example, from Chapter 11,
 * to use a bubble sort.
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

import java.io.*;

public class StudentList
{
  // used to read from the user
  static BufferedReader in;
  // how many students are there currently
  static int studentCount;

  // the student list
  static String studentList[];
  public static void bubbleSort(String data[],
    int size)
  {
    boolean done = false;
    int index;
    while (done == false)
    {
      done = true;
      for (index=1 ; index < size;index++)
      {
        if (data[index].compareTo(data[index-1])<0)
        {
          // swap data[index] and data[index-1]
          String temp          = data[index];
          data[index]   = data[index-1];
          data[index-1] = temp;
          done = false;
        }
      }
    }
  }

  public static void addStudent()
  {
    System.out.println("");
    System.out.println("*** Add Student ***");
    if( studentCount >24 )
    {
      System.out.println(
        "There are already 25 students, " +
        "which is the most you can have.");
      return;
    }
    System.out.print("Enter studentÕs first name> ");
    String first = readInput();
    System.out.print("Enter studentÕs last  name> ");
    String last = readInput();
    String name = last + ", " + first;
    studentList[studentCount] = name;
    studentCount = studentCount + 1;
    bubbleSort(studentList,studentCount);
  }
  public static void deleteStudent()
  {
    System.out.println("");
    System.out.println("*** Delete Student ***");
    if( studentCount == 0 )
    {
      System.out.println(
        "There are no students yet, "+
        "no one to delete.");
      return;
    }
    for(int i=0; i<studentCount; i++ )
    {
      System.out.println( (i+1) + ":" +
        studentList[i] );
    }
    System.out.print(
      "Which student number do you wish to delete> ");
    String str = readInput();

    int number = 0;
    try
    {
      number = Integer.parseInt( str );
    }
    catch( NumberFormatException e)
    {
      System.out.println(
        "You did not enter a valid number.");
      return;
    }
    if( number<1 )
    {
      System.out.println(
        "Student number must be at least 1.");
      return;
    }
    if( number>studentCount )
    {
      System.out.println(
        "Student number must be less than " +
        studentCount );
    }
    // now actually delete that student
    int i = number-1;
    while( i<studentCount )
    {
      studentList[i] = studentList[i+1];
      i = i + 1;
    }
    studentCount = studentCount - 1;
  }
  public static void listStudents()
  {
    System.out.println("");
    System.out.println("*** List Students ***");
    if( studentCount == 0 )
    {
      System.out.println(

        "There are no students yet.");
      return;
    }
    for(int i=0; i<studentCount; i++ )
    {
      System.out.println( studentList[i] );
    }
  }
  // a simple method that inputs a line from
  // the user and returns it
  public static String readInput()
  {
    try
    {
      String input = in.readLine();
      input = input.trim();
      return input;
    }
    catch(IOException e)
    {
    }
    return "";// an error occured
  }
  public static void main(String args[])
  {
    // setup the variables
    InputStreamReader inputStreamReader =
      new InputStreamReader ( System.in );
    in = new BufferedReader ( inputStreamReader );
    studentList = new String[25];
    studentCount = 0;
    // now display the main menu
    boolean done = false;
    while( done==false )
    {
      // print 25 blank lines to clear the screen
      for(int i=0;i<25;i++)
      {
        System.out.println("");
      }

      System.out.println(
        "*** Student List Main Menu ***");
      System.out.println("A> Add Student");
      System.out.println("D> Delete Student");
      System.out.println("L> List Students");
      System.out.println("Q> Quit program");
      System.out.print("Choose> ");
      // prompt the user
      String input = readInput();
      input = input.toUpperCase();
      char ch = input.charAt(0);
      switch( ch )
      {
        case 'A':
          addStudent();
          break;
        case 'D':
          deleteStudent();
          break;
        case 'L':
          listStudents();
          break;
        case 'Q':
          done = true;
          break;
        default:
          System.out.println(
            "Please choose a valid choice!");
      }
      System.out.println("");
      System.out.print("[Press any Enter/Return]");
      readInput();
    }
  }
}

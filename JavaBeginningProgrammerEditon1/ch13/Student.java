/**
 * Student
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 13
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class holds a single student.
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

public class Student extends Person 
{ 
  private int studentNumber; 
  private String type; 
  public void setStudentNumber(int theStudentNumber) 
  { 
    studentNumber = theStudentNumber; 
  } 
  public int getStudentNumber() 
  { 
    return studentNumber; 
  } 
  public void setType(String theType) 
  throws TypeException 
  { 
    if( !theType.equalsIgnoreCase("Freshman") && 
        !theType.equalsIgnoreCase("Sophomore") && 
        !theType.equalsIgnoreCase("Junior") && 
        !theType.equalsIgnoreCase("Graduate") && 
        !theType.equalsIgnoreCase("Senior") ) 
      throw new TypeException( 
        "Invalid type: must be Freshman, Sophomore, "           
        +"Junior, Senior or Graduate"); 
    type = theType; 
  } 
  public String getType() 
  {
    return type; 
  } 
} 
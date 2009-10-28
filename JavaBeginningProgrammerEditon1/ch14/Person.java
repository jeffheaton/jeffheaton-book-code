/**
 * Person
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 14
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class holds a person.  This class uses getters and setters.
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

public class Person 
{ 
  private String first; // the person's first name 
  private String last; // the person's last name 
  public void setFirst(String theFirst) 
  { 
    first = theFirst; 
  } 
  public String getFirst() 
  { 
    return first; 
  } 
  public void setLast(String theLast) 
  { 
    last = theLast;
  } 
  public String getLast() 
  { 
    return last; 
  } 
} 

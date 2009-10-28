/**
 * TestPerson
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 13
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class tests the person class.
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

public class TestPerson 
{ 
  public static void main(String args[]) 
  { 
    Person personA = new Person(); 
    Person personB = new Person(); 
    
    personA.setFirst("John"); 
    personA.setLast("Smith"); 
    
    personB.setFirst("Jimmy"); 
    personB.setLast("Jones"); 
    
    // now display the two
    System.out.println( personA.getLast() + ", " + 
      personA.getFirst() ); 
    System.out.println( personB.getLast() + ", " + 
      personB.getFirst() ); 
  } 
} 

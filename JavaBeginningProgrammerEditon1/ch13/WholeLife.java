/**
 * WholeLife
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 13
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class holds a whole life insurance policy.
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

public class WholeLife extends Policy 
  implements Payable 
{ 
  private double cashValue; 
  public double getCashValue() 
  { 
    return cashValue; 
  } 
  public void setCashValue(double d) 
  { 
    cashValue = d; 
  } 
  public double getBenefit() 
  { 
          return(getCashValue() + getFace()); 
  } 
} 

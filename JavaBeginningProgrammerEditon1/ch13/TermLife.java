/**
 * TermLife
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 13
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class holds a term life insurance policy.
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

public class TermLife extends Policy implements Payable
{
   private String begin;
   private String end;
   public void setBegin(String s)
   {
           begin = s;
   }
   public String getBegin()
   {
           return begin;
   }
   public void setEnd(String s)
   {
           end = s;
   }
   public String getEnd()
   {
           return end;
   }
  public double getBenefit()
  {
          return(getFace());
  }

}
/**
 * InsuranceApp
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 13
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows implements the main class for the insurance application.
 * It shows how the cutCheck method can handle any class that implements the
 * Payable interface.
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

public class InsuranceApp 
{ 
  public static void main(String args[]) 
  { 
    System.out.println("Insurance App"); 
    TermLife policy1 = new TermLife(); 
    policy1.setInsured("John Smith"); 
    policy1.setBeneficiary("Jeff Heaton"); 
    policy1.setFace(100000);
    WholeLife policy2 = new WholeLife(); 
    policy2.setInsured("Jane Smith"); 
    policy2.setBeneficiary("Jeff Heaton"); 
    policy2.setFace(100000); 
    policy2.setCashValue(1000); 
    cutCheck(policy1); 
    cutCheck(policy2); 
  } 
  public static void cutCheck(Payable policy) 
  { 
          System.out.println( 
            "The amount of:" + policy.getBenefit() ); 
  } 
} 
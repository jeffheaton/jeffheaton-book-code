/**
 * Policy
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 13
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class implements a base class, named Policy.  All insurance policy types
 * will extend this class.
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

public class Policy 
{ 
  private double face; 
  private double premium; 
  private String insured; 
  private String beneficiary; 
  public double getFace() 
  { 
          return face; 
  } 
  public void setFace(double d) 
  { 
          face = d; 
  } 
  public double getPremium() 
  { 
          return premium; 
  } 
  public void setPremium(double d) 
  { 
          premium = d; 
  } 
  public String getInsured() 
  { 
          return insured; 
  }
  public void setInsured(String s) 
  { 
          insured = s; 
  } 
  public String getBeneficiary() 
  { 
          return beneficiary; 
  } 
  public void setBeneficiary(String s) 
  { 
          beneficiary = s; 
  } 
} 

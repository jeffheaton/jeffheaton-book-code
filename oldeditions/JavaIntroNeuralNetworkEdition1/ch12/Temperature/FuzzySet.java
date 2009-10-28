
/**
 * FuzzySet
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 12
 * Programming Neural Networks in Java
 * http://www.heatonresearch.com/articles/series/1/
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

public class FuzzySet {

  /**
   * The minimum value to be part of this set.
   */
  protected double min;

  /**
   * The maximum value to be part of this set.
   */
  protected double max;

  /**
   * The midpoint value of this set.
   */
  protected double mid;

  /**
   * The value to be returned at the mid point.
   */
  protected double midValue;

  /**
   * Create a new fuzzy set and let the mid value be
   * automatically calculated to be the midpoint between
   * the minimum and maximum values.
   *
   * @param min The minimum value to be part of this set.
   * @param max The maximum value to be part of this set.
   * @param midValue The midValue to be part of this set.
   */
  public FuzzySet(double min,double max,double midValue)
  {
    this(min,max,min+((max-min)/2),midValue);
  }

  /**
   * Create a new fuzzy set and specify the midpoint between
   * the minimum and maximum values.
   *
   * @param min The minimum value to be part of this set.
   * @param max The maximum value to be part of this set.
   * @param midValue The midValue to be part of this set.
   * @param mid The midpoint of the fuzzy set.
   */
  public FuzzySet(double min,double max,double mid,double midValue)
  {
    this.min = min;
    this.max = max;
    this.mid = mid;
    this.midValue = midValue;
  }

  /**
   * Evaluate the input value and determine the membership
   * of that value in this set.
   *
   * @param in The input value to be evaluated.
   * @return Returns the membership value of the input value for
   * this set.
   */
  public double evaluate(double in)
  {
    if ( in<=min || in>=max )
      return 0;
    else if (in==mid)
      return midValue;
    else if (in<mid) {
      double step = midValue / (mid-min);
      double d = in-min;
      return d*step;
    } else {
      double step = midValue / (max-mid);
      double d = max-in;
      return d*step;
    }
  }

  /**
   * Determine if the specified input value is inside
   * of this set or not.
   *
   * @param in The input value to be checked.
   * @return Returns true if the specified input value is contained in the set.
   */
  public boolean isMember(double in)
  {
    return evaluate(in)!=0;
  }

  /**
   * Get the minimum value that must be present for membership
   * in this set.
   *
   * @return Returns the minimum value that must be present for membership
   * in this set.
   */
  public double getMin()
  {
    return mid;
  }

  /**
   * Get the maximum value that must be present for membership
   * in this set.
   *
   * @return Returns the maximum value that must be present for membership
   * in this set.
   */
  public double getMax()
  {
    return max;
  }

  /**
   * Get the midpoint of this set.
   *
   * @return Returns the midpoint of this set.
   */
  public double getMid()
  {
    return mid;
  }

  /**
   * Get the value that should be returned when the midpoint
   * is reached.
   *
   * @return Returns the value that should be returned when the midpoint
   * is reached.
   */
  public double getMidValue()
  {
    return midValue;
  }
}
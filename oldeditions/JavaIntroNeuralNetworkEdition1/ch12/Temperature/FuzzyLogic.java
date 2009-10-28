import java.util.*;
/**
 * FuzzyLogic
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

public class FuzzyLogic {

  /**
   * A list of all fuzzy logic sets that are associated with
   * this class.
   */
  protected ArrayList list = new ArrayList();
  /**
   * Add a fuzzy set to this fuzzy logic processor.
   *
   * @param set The fuzzy logic set to be added.
   */

  public void add(FuzzySet set)
  {
    list.add(set);
  }

  /**
   * Clear all fuzzy logic sets from this processor.
   */
  public void clear()
  {
    list.clear();
  }

  /**
   * This method is called to evaluate an input variable against
   * all fuzzy logic sets that are contained by this processor.
   * This method will return the membership that is held by
   * each fuzzy logic set given the input value.
   *
   * @param in An input parameter that should be checked against all of the
   * fuzzy sets that are contained by this processor.
   * @return An array of doubles that contains the degree to which each of the
   * fuzzy sets matched with the input parameter.
   */
  public double []evaluate(double in)
  {
    double result[] = new double[list.size()];
    Iterator itr = list.iterator();
    int i = 0;
    while (itr.hasNext()) {
      FuzzySet set = (FuzzySet)itr.next();
      result[i++] = set.evaluate(in);
    }
    return result;
  }


}
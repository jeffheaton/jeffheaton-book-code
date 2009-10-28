/**
 * Temperature
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
public class Temperature {

/**
 * Read a single line from the console.
 *
 * @return The line that the user typed.
 */
  static String getline()
  {
    String str="";
    char ch;
    try {
      do {
        ch = (char)System.in.read();
        if ( (ch!='\n') && (ch!='\r') )
          str+=ch;
      } while (ch!='\n');
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return str;
  }
  /**
   * The main method.
   *
   * @param args This program does not use arguments to main.
   */


  public static void main(String args[])
  {
    // prepare the fuzzy logic processor
    FuzzyLogic fuzzyLogic = new FuzzyLogic();
    fuzzyLogic.clear();

    FuzzySet cold = new FuzzySet(-20,59,1);
    FuzzySet warm = new FuzzySet(45,90,1);
    FuzzySet hot = new FuzzySet(75,120,1);

    fuzzyLogic.add(cold);
    fuzzyLogic.add(warm);
    fuzzyLogic.add(hot);
    // present
    String str = "*";

    while (!str.equals("")) {
      System.out.print("Enter a temperature(between -20 and 120 deg f)? ");
      str = getline();
      double temp = 0;
      try {
        if (!str.equals(""))
          temp = Double.parseDouble(str);
      } catch (NumberFormatException e) {
        System.out.println("***Please enter a valid temperature or a blank line to exit");
      }

      double array[] = fuzzyLogic.evaluate(temp);
      System.out.println("Cold:" + array[0] );
      System.out.println("Warm:" + array[1] );
      System.out.println("Hot :" + array[2] );
    }
  }
}
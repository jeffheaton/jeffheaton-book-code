/**
 * OptionDialog
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 15
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class implements a option dialog.
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

import javax.swing.*; 
public class OptionDialog 
{ 
  public static void main(String args[]) 
  { 
    Object[] options = { "Red", "Green", "Blue", "Other" }; 
    int color = JOptionPane.showOptionDialog( 
      null, 
      "What is your favorite color?", 
      "My Swing Application", 
      JOptionPane.DEFAULT_OPTION, 
      JOptionPane.QUESTION_MESSAGE, 
      null, 
      options, 
      options[0]); 
     JOptionPane.showMessageDialog( 
        null, 
        "You entered " + options[color], 
        "My Swing Application", 
        JOptionPane.INFORMATION_MESSAGE); 
  } 
} 

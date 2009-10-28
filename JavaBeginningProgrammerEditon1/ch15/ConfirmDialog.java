/**
 * ConfirmDialog
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 15
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class implements a confirm dialog.
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

public class ConfirmDialog 
{ 
  public static void main(String args[]) 
  { 
    int i = JOptionPane.showConfirmDialog(null, 
      "Do you like the color red?", 
      "My Swing Application", 
      JOptionPane.YES_NO_OPTION); 
    if( i==JOptionPane.YES_OPTION ) 
    { 
      JOptionPane.showMessageDialog( 
        null, 
        "Good, I like it too!", 
        "My Swing Application", 
        JOptionPane.ERROR_MESSAGE); 
    } 
    else 
    { 
      JOptionPane.showMessageDialog( 

        null, 
        "Why? It is a very nice color!", 
        "My Swing Application", 
        JOptionPane.ERROR_MESSAGE); 
    } 
  } 
} 

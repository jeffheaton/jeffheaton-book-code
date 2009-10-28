/**
 * InputDialog
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 15
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class implements a input dialog.
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

public class InputDialog
{
  public static void main(String args[])
  {
     String name = JOptionPane.showInputDialog(null,
       "Please enter your name?",

       "My Swing Application",
       JOptionPane.QUESTION_MESSAGE);
     JOptionPane.showMessageDialog(
        null,
        "Hello " + name,
        "My Swing Application",
        JOptionPane.INFORMATION_MESSAGE);
  }
}

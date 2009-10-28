/**
 * MethodCall
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 6
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how objects can be used to return values from methods.
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

public class MethodCall
{
  static void changeValue(JButton button)
  {
    // Change the text of the button, this
    // new value is reflected outside of the
    // call to "changeValue"
    button.setText("New value");
  }

  static void changeReference(JButton button)
  {
    // Create a new button, and assign its
    // reference to "button". This change is
    // not reflected outside of the call
    // to "changeValue"
    button = new JButton("New value");
  }

  static void changePrimitive(int i)
  {
    i = i + 1;
  }

  /**
   * Main entry point for example.
   * @param args Not used.
   */
  public static void main(String args[])
  {
    // setup the variables
    JButton button1 = new JButton("Old Value");
    JButton button2 = new JButton("Old Value");
    int var = 5;
    // call the methods
    changeValue(button1);
    changeReference(button2);
    changePrimitive(var);
    // display the new values
    System.out.println("Button1:" +
    button1.getText());
    System.out.println("Button2:" +
    button2.getText());
    System.out.println("Primitive variable:" + var);
  }
}
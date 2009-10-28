import javax.swing.*;
import java.awt.*;

/**
 * Graph
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 10
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

public class Graph extends JFrame {

  /**
   * The width of the window.
   */
  public static final int WIDTH = 200;

  /**
   * The height of the window.
   */
  public static final int HEIGHT = 200;

  /**
   * The fuzzy logic set.
   */
  protected FuzzyLogic fuzzyLogic;

  /**
   * A fuzzy set for cold temperatures.
   */
  protected FuzzySet cold;

  /**
   * A fuzzy set for warm temperatures.
   */
  protected FuzzySet warm;

  /**
   * A fuzzy set for hot temperatures.
   */
  protected FuzzySet hot;
  /**
   * Construct the graph frame.
   */

  Graph()
  {
    // setup fuzzy sets

    fuzzyLogic = new FuzzyLogic();
    fuzzyLogic.clear();

    cold = new FuzzySet(-20,59,1);
    warm = new FuzzySet(45,90,1);
    hot = new FuzzySet(75,120,1);

    fuzzyLogic.add(cold);
    fuzzyLogic.add(warm);
    fuzzyLogic.add(hot);



    // setup window
    setTitle("Fuzzy Graph");
    setSize(WIDTH,HEIGHT);
  }

  /**
   * Paint the graph.
   *
   * @param g A graphics object.
   */
  public void paint(Graphics g)
  {
    int last[] = new int[3];
    g.setColor(Color.black);
    g.fillRect(0,0,HEIGHT,WIDTH);
    for (int i=-20;i<=120;i++) {
      double array[] = fuzzyLogic.evaluate(i);
      for (int j=0;j<array.length;j++) {
        switch (j) {
        case 0:g.setColor(Color.green);break;
        case 1:g.setColor(Color.yellow);break;
        case 2:g.setColor(Color.red);break;
        }

        if (array[j]!=0) {
          if (last[j]!=0)
            g.drawLine(
                      i+20,
                      last[j],
                      i+20,
                      HEIGHT-(int)((HEIGHT-20)*array[j]));
          last[j]=HEIGHT-(int)((HEIGHT-20)*array[j]);
        }
      }
    }
  }

  /**
   * The main method.
   *
   * @param args Arguments to main are not used.
   */
  public static void main(String args[])
  {
    (new Graph()).show();
  }
}
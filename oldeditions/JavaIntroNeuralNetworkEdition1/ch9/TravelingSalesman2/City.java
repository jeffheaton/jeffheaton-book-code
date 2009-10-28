/**
 * City
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 9
 * Programming Neural Networks in Java
 * http://www.heatonresearch.com/articles/series/1/
 *
 * This class implements a single city. This
 * is used with the traveling salesman problem.
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

class City {

  /**
   * The city's x position.
   */
  int xpos;

  /**
   * The city's y position.
   */
  int ypos;

  /**
   * Constructor.
   *
   * @param x The city's x position
   * @param y The city's y position.
   */
  City(int x, int y) {
    xpos = x;
    ypos = y;
  }

  /**
   * Return's the city's x position.
   *
   * @return The city's x position.
   */
  int getx() {
    return xpos;
  }

  /**
   * Returns the city's y position.
   *
   * @return The city's y position.
   */
  int gety() {
    return ypos;
  }

  /**
   * Returns how close the city is to another city.
   *
   * @param cother The other city.
   * @return A distance.
   */
  int proximity(City cother) {
    return proximity(cother.getx(),cother.gety());
  }

  /**
   * Returns how far this city is from a a specific point.
   * This method uses the pythagorean theorum to calculate
   * the distance.
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @return The distance.
   */
  int proximity(int x, int y) {
    int xdiff = xpos - x;
    int ydiff = ypos - y;
    return(int)Math.sqrt( xdiff*xdiff + ydiff*ydiff );
  }
}

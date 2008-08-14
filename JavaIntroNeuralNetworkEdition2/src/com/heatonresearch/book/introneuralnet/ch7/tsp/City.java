/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 *   
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */
package com.heatonresearch.book.introneuralnet.ch7.tsp;

/**
 * Chapter 7: Training using Simulated Annealing
 * 
 * City: Holds the location of a city for the traveling salesman problem.
 * 
 * @author Jeff Heaton
 * @version 2.1
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
	 * @param x
	 *            The city's x position
	 * @param y
	 *            The city's y position.
	 */
	City(final int x, final int y) {
		this.xpos = x;
		this.ypos = y;
	}

	/**
	 * Return's the city's x position.
	 * 
	 * @return The city's x position.
	 */
	int getx() {
		return this.xpos;
	}

	/**
	 * Returns the city's y position.
	 * 
	 * @return The city's y position.
	 */
	int gety() {
		return this.ypos;
	}

	/**
	 * Returns how close the city is to another city.
	 * 
	 * @param cother
	 *            The other city.
	 * @return A distance.
	 */
	int proximity(final City cother) {
		return proximity(cother.getx(), cother.gety());
	}

	/**
	 * Returns how far this city is from a a specific point. This method uses
	 * the pythagorean theorum to calculate the distance.
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @return The distance.
	 */
	int proximity(final int x, final int y) {
		final int xdiff = this.xpos - x;
		final int ydiff = this.ypos - y;
		return (int) Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}
}

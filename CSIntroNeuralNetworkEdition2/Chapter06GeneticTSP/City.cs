using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter06GeneticTSP
{
    public class City
    {
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
	public City( int x,  int y) {
		this.xpos = x;
		this.ypos = y;
	}

	/**
	 * Return's the city's x position.
	 * 
	 * @return The city's x position.
	 */
	public int getx() {
		return this.xpos;
	}

	/**
	 * Returns the city's y position.
	 * 
	 * @return The city's y position.
	 */
	public int gety() {
		return this.ypos;
	}

	/**
	 * Returns how close the city is to another city.
	 * 
	 * @param cother
	 *            The other city.
	 * @return A distance.
	 */
	public int proximity( City cother) {
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
	public int proximity( int x,  int y) {
		 int xdiff = this.xpos - x;
		 int ydiff = this.ypos - y;
		return (int) Math.Sqrt(xdiff * xdiff + ydiff * ydiff);
	}
    }
}

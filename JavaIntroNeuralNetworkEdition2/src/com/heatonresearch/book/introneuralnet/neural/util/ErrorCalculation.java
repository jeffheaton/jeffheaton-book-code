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
package com.heatonresearch.book.introneuralnet.neural.util;


/**
 * ErrorCalculation: An implementation of root mean square (RMS)
 * error calculation.  This class is used by nearly every neural
 * network in this book to calculate error.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class ErrorCalculation {
	private double globalError;
	private int setSize;

	/**
	 * Returns the root mean square error for a complete training set.
	 * 
	 * @param len
	 *            The length of a complete training set.
	 * @return The current error for the neural network.
	 */
	public double calculateRMS() {
		final double err = Math.sqrt(this.globalError / (this.setSize));
		return err;

	}

	/**
	 * Reset the error accumulation to zero.
	 */
	public void reset() {
		this.globalError = 0;
		this.setSize = 0;
	}

	/**
	 * Called to update for each number that should be checked.
	 * @param actual The actual number.
	 * @param ideal The ideal number.
	 */
	public void updateError(final double actual[], final double ideal[]) {
		for (int i = 0; i < actual.length; i++) {
			final double delta = ideal[i] - actual[i];
			this.globalError += delta * delta;			
		}
		this.setSize += ideal.length;
	}

}

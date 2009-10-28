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
package com.heatonresearch.book.introneuralnet.neural.activation;

import com.heatonresearch.book.introneuralnet.neural.util.BoundNumbers;

/**
 * ActivationSigmoid: The sigmoid activation function takes on a
 * sigmoidal shape.  Only positive numbers are generated.  Do not
 * use this activation function if negative number output is desired.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class ActivationSigmoid implements ActivationFunction {
	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = 5622349801036468572L;

	/**
	 * A threshold function for a neural network.
	 * @param The input to the function.
	 * @return The output from the function.
	 */
	public double activationFunction(final double d) {
		return 1.0 / (1 + BoundNumbers.exp(-1.0 * d));
	}
	
	/**
	 * Some training methods require the derivative.
	 * @param The input.
	 * @return The output.
	 */
	public double derivativeFunction(double d) {
		return d*(1.0-d);
	}

}

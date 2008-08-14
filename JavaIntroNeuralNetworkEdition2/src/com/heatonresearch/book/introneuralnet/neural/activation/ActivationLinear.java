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

import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;

/**
 * ActivationLinear: The Linear layer is really not an activation function 
 * at all.  The input is simply passed on, unmodified, to the output.
 * This activation function is primarily theoretical and of little actual
 * use.  Usually an activation function that scales between 0 and 1 or
 * -1 and 1 should be used.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class ActivationLinear implements ActivationFunction {

	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = -5356580554235104944L;

	/**
	 * A threshold function for a neural network.
	 * @param The input to the function.
	 * @return The output from the function.
	 */
	public double activationFunction(final double d) {
		return d;
	}

	/**
	 * Some training methods require the derivative.
	 * @param The input.
	 * @return The output.
	 */
	public double derivativeFunction(double d) {
		throw new NeuralNetworkError("Can't use the linear activation function where a derivative is required.");
	}

}

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

import java.io.Serializable;

/**
 * ActivationFunction: This interface allows various 
 * activation functions to be used with the feedforward
 * neural network.  Activation functions are applied
 * to the output from each layer of a neural network.
 * Activation functions scale the output into the
 * desired range. 
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public interface ActivationFunction extends Serializable {

	/**
	 * A activation function for a neural network.
	 * @param The input to the function.
	 * @return The output from the function.
	 */
	public double activationFunction(double d);

	/**
	 * Performs the derivative of the activation function function on the input.
	 * 
	 * @param d
	 *            The input.
	 * @return The output.
	 */
	public double derivativeFunction(double d);
}

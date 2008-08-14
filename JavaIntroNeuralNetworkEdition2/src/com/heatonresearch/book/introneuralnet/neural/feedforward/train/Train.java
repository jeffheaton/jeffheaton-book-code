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
package com.heatonresearch.book.introneuralnet.neural.feedforward.train;

import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;

/**
 * Train: Interface for all feedforward neural network training
 * methods.  There are currently three training methods define:
 * 
 * Backpropagation
 * Genetic Algorithms
 * Simulated Annealing
 *  
 * @author Jeff Heaton
 * @version 2.1
 */

public interface Train {

	/**
	 * Get the current error percent from the training.
	 * @return The current error.
	 */
	public double getError();

	/**
	 * Get the current best network from the training.
	 * @return The best network.
	 */
	public FeedforwardNetwork getNetwork();

	/**
	 * Perform one iteration of training.
	 */
	public void iteration();
}

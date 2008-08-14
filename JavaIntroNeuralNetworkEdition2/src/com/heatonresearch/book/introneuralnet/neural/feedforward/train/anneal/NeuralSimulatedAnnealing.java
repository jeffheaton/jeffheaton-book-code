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
package com.heatonresearch.book.introneuralnet.neural.feedforward.train.anneal;

import com.heatonresearch.book.introneuralnet.neural.anneal.SimulatedAnnealing;
import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.matrix.MatrixCODEC;

/**
 * NeuralSimulatedAnnealing: This class implements a simulated 
 * annealing training algorithm for feed forward neural networks.
 * It is based on the generic SimulatedAnnealing class.  It is used
 * in the same manner as any other training class that implements
 * the Train interface.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class NeuralSimulatedAnnealing extends SimulatedAnnealing<Double> {
	/**
	 * The neural network that is to be trained.
	 */
	protected FeedforwardNetwork network;

	/**
	 * The training data.
	 */
	protected double input[][];

	/**
	 * The ideal results to the training data.
	 */
	protected double ideal[][];


	/**
	 * Construct a simulated annleaing trainer for a feedforward neural network.
	 * 
	 * @param network
	 *            The neural network to be trained.
	 * @param input
	 *            The input values for training.
	 * @param ideal
	 *            The ideal values for training.
	 * @param startTemp
	 *            The starting temperature.
	 * @param stopTemp
	 *            The ending temperature.
	 * @param cycles
	 *            The number of cycles in a training iteration.
	 */
	public NeuralSimulatedAnnealing(final FeedforwardNetwork network,
			final double input[][], final double ideal[][],
			final double startTemp, final double stopTemp, final int cycles) {
		this.network = network;
		this.input = input;
		this.ideal = ideal;
		this.temperature = startTemp;
		setStartTemperature(startTemp);
		setStopTemperature(stopTemp);
		setCycles(cycles);
	}

	/**
	 * Determine the error of the current weights and thresholds.
	 * 
	 * @throws NeuralNetworkException
	 */
	@Override
	public double determineError() throws NeuralNetworkError {
		return this.network.calculateError(this.input, this.ideal);
	}

	/**
	 * Get the network as an array of doubles.
	 * @return The network as an array of doubles.
	 */
	@Override
	public Double[] getArray() {
		return MatrixCODEC.networkToArray(this.network);
	}

	/**
	 * Get the best network from the training.
	 * @return The best network.
	 */
	public FeedforwardNetwork getNetwork() {
		return this.network;
	}

	/**
	 * Convert an array of doubles to the current best network.
	 */
	@Override
	public void putArray(final Double[] array) {
		MatrixCODEC.arrayToNetwork(array, this.network);
	}

	/**
	 * Randomize the weights and thresholds. This function does most of the work
	 * of the class. Each call to this class will randomize the data according
	 * to the current temperature. The higher the temperature the more
	 * randomness.
	 */
	@Override
	public void randomize() {
		final Double array[] = MatrixCODEC.networkToArray(this.network);

		for (int i = 0; i < array.length; i++) {
			double add = 0.5 - (Math.random());
			add /= getStartTemperature();
			add *= this.temperature;
			array[i] = array[i] + add;
		}

		MatrixCODEC.arrayToNetwork(array, this.network);
	}

	@Override
	public Double[] getArrayCopy() {
		return this.getArray();
	}

}
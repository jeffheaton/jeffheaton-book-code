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
package com.heatonresearch.book.introneuralnet.neural.feedforward.train.genetic;

import java.util.Arrays;

import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.genetic.Chromosome;
import com.heatonresearch.book.introneuralnet.neural.genetic.GeneticAlgorithm;
import com.heatonresearch.book.introneuralnet.neural.matrix.MatrixCODEC;

/**
 * NeuralChromosome: Implements a chromosome that allows a 
 * feedforward neural network to be trained using a genetic
 * algorithm.  The chromosome for a feed forward neural network
 * is the weight and threshold matrix.  
 * 
 * This class is abstract.  If you wish to train the neural
 * network using training sets, you should use the 
 * TrainingSetNeuralChromosome class.  If you wish to use 
 * a cost function to train the neural network, then
 * implement a subclass of this one that properly calculates
 * the cost.
 * 
 * The generic type GA_TYPE specifies the GeneticAlgorithm derived
 * class that implements the genetic algorithm that this class is 
 * to be used with.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
abstract public class NeuralChromosome<GA_TYPE extends GeneticAlgorithm<?>>
		extends Chromosome<Double, GA_TYPE> {

	private static final Double ZERO = Double.valueOf(0);
	private static final double RANGE = 20.0;

	private FeedforwardNetwork network;

	/**
	 * @return the network
	 */
	public FeedforwardNetwork getNetwork() {
		return this.network;
	}

	public void initGenes(final int length) {
		final Double result[] = new Double[length];
		Arrays.fill(result, ZERO);
		this.setGenesDirect(result);
	}

	/**
	 * Mutate this chromosome randomly
	 */
	@Override
	public void mutate() {
		final int length = getGenes().length;
		for (int i = 0; i < length; i++) {
			double d = getGene(i);
			final double ratio = (int) ((RANGE * Math.random()) - RANGE);
			d*=ratio;
			setGene(i,d);
		}
	}



	/**
	 * Set all genes.
	 * 
	 * @param list
	 *            A list of genes.
	 * @throws NeuralNetworkException
	 */
	@Override
	public void setGenes(final Double[] list) throws NeuralNetworkError {

		// copy the new genes
		super.setGenes(list);

		calculateCost();
	}

	/**
	 * @param network
	 *            the network to set
	 */
	public void setNetwork(final FeedforwardNetwork network) {
		this.network = network;
	}

	public void updateGenes() throws NeuralNetworkError {
		this.setGenes(MatrixCODEC.networkToArray(this.network));
	}

	public void updateNetwork() {
		MatrixCODEC.arrayToNetwork(getGenes(), this.network);
	}

}

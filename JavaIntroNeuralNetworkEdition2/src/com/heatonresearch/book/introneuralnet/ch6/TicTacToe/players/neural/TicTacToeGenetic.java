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
package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.neural;

import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.genetic.NeuralChromosome;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.genetic.NeuralGeneticAlgorithm;

/**
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * TicTacToeGenetic: Use a genetic algorithm to teach a neural network
 * to play Tic-Tac-Toe.  The cost of each chromosome is calculated
 * by playing the neural network against a computer player.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class TicTacToeGenetic extends NeuralGeneticAlgorithm<TicTacToeGenetic> {

	private Class<?> opponent;

	public TicTacToeGenetic(final FeedforwardNetwork network,
			final boolean reset, final int populationSize,
			final double mutationPercent, final double percentToMate,
			final Class<?> opponent) throws NeuralNetworkError {

		this.setOpponent(opponent);
		this.setMutationPercent(mutationPercent);
		this.setMatingPopulation(percentToMate * 2);
		this.setPopulationSize(populationSize);
		this.setPercentToMate(percentToMate);

		setChromosomes(new TicTacToeChromosome[getPopulationSize()]);
		for (int i = 0; i < getChromosomes().length; i++) {
			final FeedforwardNetwork chromosomeNetwork = (FeedforwardNetwork) network
					.clone();
			if (reset) {
				chromosomeNetwork.reset();
			}

			final TicTacToeChromosome c = new TicTacToeChromosome(this,
					chromosomeNetwork);
			c.updateGenes();
			setChromosome(i, c);
		}
		sortChromosomes();
	}

	/**
	 * @return the opponent
	 */
	public Class<?> getOpponent() {
		return this.opponent;
	}

	public double getScore() {
		final NeuralChromosome<TicTacToeGenetic> c = getChromosome(0);
		return c.getCost();
	}

	/**
	 * @param opponent
	 *            the opponent to set
	 */
	public void setOpponent(final Class<?> opponent) {
		this.opponent = opponent;
	}

}

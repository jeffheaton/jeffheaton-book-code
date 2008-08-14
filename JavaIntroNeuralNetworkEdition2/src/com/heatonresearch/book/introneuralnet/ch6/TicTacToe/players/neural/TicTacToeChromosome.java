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

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.ScorePlayer;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.Player;
import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.genetic.NeuralChromosome;

/**
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * TicTacToeChromosome: Implements a chromosome for the neural network
 * that plays tic-tac-toe.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class TicTacToeChromosome extends NeuralChromosome<TicTacToeGenetic> {

	/**
	 * The constructor, takes a list of cities to set the initial "genes" to.
	 * 
	 * @param cities
	 *            The order that this chromosome would visit the cities. These
	 *            cities can be thought of as the genes of this chromosome.
	 * @throws NeuralNetworkException
	 */
	public TicTacToeChromosome(final TicTacToeGenetic genetic,
			final FeedforwardNetwork network) throws NeuralNetworkError {
		this.setGeneticAlgorithm(genetic);
		this.setNetwork(network);

		initGenes(network.getWeightMatrixSize());
		updateGenes();
	}

	@Override
	public void calculateCost() {

		try {
			// update the network with the new gene values
			this.updateNetwork();

			final PlayerNeural player1 = new PlayerNeural(getNetwork());
			Player player2;

			player2 = (Player) this.getGeneticAlgorithm().getOpponent()
					.newInstance();
			final ScorePlayer score = new ScorePlayer(player1, player2, false);
			setCost(score.score());
		} catch (final InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

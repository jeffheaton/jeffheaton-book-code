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

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Board;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Move;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.TicTacToe;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.Player;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;

/**
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * PlayerNeural: Play Tic-Tac-Toe using a neural network.  This class
 * can be played against any of the other player types that implement
 * the Player interface.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class PlayerNeural implements Player {

	private final FeedforwardNetwork network;

	public PlayerNeural(final FeedforwardNetwork network) {
		this.network = network;
	}

	public Move getMove(final byte[][] board, final Move prev, final byte color) {
		Move bestMove = null;
		double bestScore = Double.MIN_VALUE;

		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				final Move move = new Move((byte) x, (byte) y, color);
				if (Board.isEmpty(board, move)) {
					final double d = tryMove(board, move);
					if ((d > bestScore) || (bestMove == null)) {
						bestScore = d;
						bestMove = move;
					}
				}
			}
		}

		return bestMove;

	}

	private double tryMove(final byte[][] board, final Move move) {
		final double input[] = new double[9];
		int index = 0;

		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				if (board[x][y] == TicTacToe.NOUGHTS) {
					input[index] = -1;
				} else if (board[x][y] == TicTacToe.CROSSES) {
					input[index] = 1;
				} else if (board[x][y] == TicTacToe.EMPTY) {
					input[index] = 0;
				}

				if ((x == move.x) && (y == move.y)) {
					input[index] = -1;
				}

				index++;
			}
		}

		final double output[] = this.network.computeOutputs(input);
		return output[0];
	}

}

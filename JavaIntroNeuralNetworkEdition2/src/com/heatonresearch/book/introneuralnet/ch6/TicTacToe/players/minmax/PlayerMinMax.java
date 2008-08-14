/*
 * AI
 *
 * Thomas David Baker, 2002-03-11
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.minmax;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Move;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.TicTacToe;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.Player;

/**
 * Implementation of Minimax with alpha-beta pruning AI. Taken in part from <a
 * href="http://mainline.brynmawr.edu/Courses/cs372/fall2000/AB.html">http://mainline.brynmawr.edu/Courses/cs372/fall2000/AB.html</a>.
 * 
 * @author Thomas David Baker, bakert+tictactoe@gmail.com
 * @version 1.1
 */
public class PlayerMinMax implements Player {

	// Internal representation of wheter this AI is noughts or crosses.
	private byte _player = TicTacToe.EMPTY;

	/**
	 * Gets the next move of this player.
	 * 
	 * @param board
	 *            <code>Board</code> representation of the current game state.
	 * @param prev
	 *            <code>Move</code> representing the previous move in the the
	 *            game.
	 * @param color
	 *            <code>int</code> representing the pieces this
	 *            <code>Player</code> is playing with. One of
	 *            <code>TicTacToe.NOUGHTS</code> or
	 *            <code>TicTacToe.CROSSES</code>
	 * @return <code>Move</code> Next move of this player.
	 */
	public Move getMove(final byte[][] board, final Move prev, final byte color) {

		this._player = color;

		final Node root = new Node(board, prev);
		byte max = Byte.MIN_VALUE;

		Node child;
		Node bestNode = null;

		while ((child = root.getChild()) != null) {
			final byte val = minimaxAB(child, true, Byte.MIN_VALUE,
					Byte.MAX_VALUE);
			if (val >= max) {
				max = val;
				bestNode = child;
			}
		}

		return bestNode.move();

	}

	// Implementation of mimimax with alpha-beta pruning.
	private byte minimaxAB(final Node n, final boolean min, final byte a,
			final byte b) {

		byte alpha = a;
		byte beta = b;

		if (n.isLeaf()) {
			return n.value(this._player);
		}

		Node child;

		if (min) {
			while (((child = n.getChild()) != null) && !(alpha > beta)) {
				final byte val = minimaxAB(child, false, alpha, beta);
				if (val < beta) {
					beta = val;
				}
			}
			return beta;
		} else {
			while (((child = n.getChild()) != null) && !(alpha > beta)) {
				final byte val = minimaxAB(child, true, alpha, beta);
				if (val > alpha) {
					alpha = val;
				}
			}
			return alpha;
		}
	}

}

/*
 * Player
 *
 * Thomas David Baker, 2002-03-04
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Move;

/**
 * Interface to be implemented by players for the game of <code>TicTacToe</code>.
 * A <code>Human</code> implementation is provided in this package so it is
 * expected that this class will be implemented by AIs but other human classes
 * could be devised also.
 */
public interface Player {

	/**
	 * Gets the next move for this player.
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
	 * @return <code>Move</code> Next move for the player.
	 */
	public Move getMove(byte[][] board, Move prev, byte color);

}

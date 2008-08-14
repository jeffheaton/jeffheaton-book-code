/*
 * Boring
 *
 * Thomas David Baker, 2003-03-10
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Board;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Move;

/**
 * A very stoopid AI player for <code>TicTacToe</code>. He just looks for the
 * first available square and moves there.
 * 
 * @author Thomas David Baker, bakert+tictactoe@gmail.com
 * @version 1.1
 */
public class PlayerRandom implements Player {

	/**
	 * Gets this player's next move. It is always the next available square.
	 * 
	 * @param board
	 *            <code>Board</code> representation of the current game state.
	 * @param prev
	 *            <code>Move</code> representing the previous move in the the
	 *            game.
	 * @param player
	 *            <code>int</code> representing the pieces this
	 *            <code>Player</code> is playing with. One of
	 *            <code>TicTacToe.NOUGHTS</code> or
	 *            <code>TicTacToe.CROSSES</code>
	 * @return <code>Move</code> Next move for this player.
	 */
	public Move getMove(final byte[][] board, final Move prev, final byte player) {

		for (;;) {
			final byte x = (byte) (Math.random() * 3.0);
			final byte y = (byte) (Math.random() * 3.0);
			final Move m = new Move(x, y, player);
			if (Board.isEmpty(board, m)) {
				return m;
			}
		}

	}

}

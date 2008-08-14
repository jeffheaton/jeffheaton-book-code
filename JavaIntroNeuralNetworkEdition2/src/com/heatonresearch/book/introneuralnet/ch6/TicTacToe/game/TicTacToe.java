/*
 * TicTacToe
 *
 * Thomas David Baker, 2002-03-10
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game;

/**
 * Mother class for the <code>net.bluebones.tictactoe</code> package. This
 * class is executable and expects as two commandline arguments the names of the
 * classes of the two players. The class "Human" is included for human players,
 * as are several AI players.
 * 
 * @author Thomas David Baker, bakert+tictactoe@gmail.com
 * @version 1.1
 */
public class TicTacToe {

	/** <code>byte</code> representing empty. */
	public static final byte EMPTY = -1;
	/** <code>int</code> representing noughts (o) */
	public static final byte NOUGHTS = 0;
	/** <code>int</code> representing crosses (x) */
	public static final byte CROSSES = 1;

	public static char resolvePiece(final byte b) {
		if (b == TicTacToe.CROSSES) {
			return 'X';
		} else if (b == TicTacToe.NOUGHTS) {
			return 'O';
		} else {
			return ' ';
		}

	}

	/**
	 * Takes supplied integer and returns its "reverse", that is the number of
	 * the opposing player. That is,
	 * <code>TicTacToe.reverse(TicTacToe.NOUGHTS)</code> returns
	 * <code>TicTacToe.CROSSES</code> and vice versa. Any other inputs are
	 * illegal and will throw an exception.
	 * 
	 * @param i
	 *            <code>int</code> to "reverse".
	 * @return <code>int</code> of reversed <code>i</code>.
	 */
	public static byte reverse(final byte i) {

		if (i == TicTacToe.NOUGHTS) {
			return TicTacToe.CROSSES;
		} else if (i == TicTacToe.CROSSES) {
			return TicTacToe.NOUGHTS;
		} else {
			throw new IllegalArgumentException("Number supplied was " + i
					+ " but must be TicTacToe.NOUGHTS (" + TicTacToe.NOUGHTS
					+ ") or TicTacToe.CROSSES (" + TicTacToe.CROSSES + ")");
		}
	}

}

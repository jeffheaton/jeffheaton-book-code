using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter6TicTacToe.Game
{
    class TicTacToe
    {
	/** <code>int</code> representing empty. */
	public const int EMPTY = -1;
	/** <code>int</code> representing noughts (o) */
	public const int NOUGHTS = 0;
	/** <code>int</code> representing crosses (x) */
	public const int CROSSES = 1;

	public static char resolvePiece( int b) {
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
	public static int reverse( int i) {

		if (i == TicTacToe.NOUGHTS) {
			return TicTacToe.CROSSES;
		} else if (i == TicTacToe.CROSSES) {
			return TicTacToe.NOUGHTS;
		} else {
			throw new Exception("Number supplied was " + i
					+ " but must be TicTacToe.NOUGHTS (" + TicTacToe.NOUGHTS
					+ ") or TicTacToe.CROSSES (" + TicTacToe.CROSSES + ")");
		}
	}


    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players
{
    class PlayerBoring: Player
    {
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
	public Move getMove( int[,] board,  Move prev,  int player) {
		for (byte x = 0; x < 3; x++) {
			for (byte y = 0; y < 3; y++) {
				 Move m = new Move(x, y, player);
				if (Board.isEmpty(board, m)) {
					return m;
				}
			}
		}
		throw new Exception("I'm just looking for the first "
				+ "empty square and I can't move!");
	}
    }
}

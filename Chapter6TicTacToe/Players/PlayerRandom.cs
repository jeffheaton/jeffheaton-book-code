using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players
{
    class PlayerRandom: Player
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

        Random rand = new Random();

		for (;;) {
			 int x = (int) (rand.NextDouble() * 3.0);
             int y = (int)(rand.NextDouble() * 3.0);
			 Move m = new Move(x, y, player);
			if (Board.isEmpty(board, m)) {
				return m;
			}
		}

	}

    }
}

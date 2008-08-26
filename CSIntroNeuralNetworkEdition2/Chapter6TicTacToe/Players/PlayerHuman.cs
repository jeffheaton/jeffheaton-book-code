using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players
{
    class PlayerHuman: Player
    {

	// helper method for get move - asks stdin for a coordinate.
	private int getCoord() {

		int n = -1;
        try {
		n = int.Parse(Console.ReadLine());
		} catch ( FormatException ) {
			Console.WriteLine("Not a number!");
			return getCoord();
		}

		if ((n < 0) || (n >= Board.SIZE)) {
            Console.WriteLine("Not within range (0-" + (Board.SIZE - 1) + ")!");
			return getCoord();
		}
		return n;

	}

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
	public Move getMove( int[,] board,  Move prev,  int color) {
		Board.printBoard(board);
		// ask for move
		Console.WriteLine("You are playing: " + TicTacToe.resolvePiece(color)
				+ ", select your move.");
		Console.Write("X position? ");
		 int x = getCoord();
		Console.Write("Y position? ");
		 int y = getCoord();

		return new Move(x, y, color);

	}


    }
}

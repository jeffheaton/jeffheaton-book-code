using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Players;

namespace Chapter6TicTacToe.Game
{
    class Game
    {
	// Internal representation of the board for this game.
	private  int[,] _board = new int[3,3];

	// Internal representation of the current player. Noughts starts.
	private int _player = TicTacToe.NOUGHTS;

	// Internal array of both players.
	private Player[] _players = new Player[2];
	// Initialize the board.
	

	

	/**
	 * Creates a new <code>Game</code> with the specified players.
	 * 
	 * @param players
	 *            <code>Player[]</code> of the two players.
	 * @throws <code>IllegalArgumentException</code> unchecked, if there are not
	 *             2 players.
	 */
	public Game( Player[] players) {
		if (players.Length != 2) {
			throw new Exception("Game must be instantiated "
					+ "with two players");
		}
		this._players = players;

		for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++)
            {
				this._board[x,y] = TicTacToe.EMPTY;
			}
		}
	}

	/** Called to begin a <code>Game</code>. */
	public Player play() {
		return play(null);
	}

	// main guts of the game
	private Player play(Move prev) {

		for (;;) {
			// new board here else players can alter the game board as they
			// wish!
			 int[,] b = Board.copy(this._board);
			 Move move = this._players[this._player].getMove(b, prev,
					this._player);

			if (move == null) {
				throw new Exception("Player supplied null move");
			}

			// check square is empty
			if (!Board.isEmpty(this._board, move)) {
				Console.WriteLine("That square is not empty");
				play(prev);
			}

			// place piece
			if (!Board.placePiece(this._board, move)) {
                Console.WriteLine("Illegal Move - try again");
				play(prev);
			}

			if (Board.isWinner(this._board, this._player)) {
				return this._players[this._player];
			}

			// change player
			this._player = TicTacToe.reverse(this._player);

			if (Board.isFull(this._board)) {
				return null;
			}

			prev = move;
		}

	}

	public void printBoard() {
		Board.printBoard(this._board);

	}


    }
}

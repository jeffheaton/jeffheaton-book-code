/*
 * Game
 *
 * Thomas David Baker, 2002-03-10
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.Player;

public class Game {

	// Internal representation of the board for this game.
	private final byte[][] _board = new byte[3][3];

	// Internal representation of the current player. Noughts starts.
	private byte _player = TicTacToe.NOUGHTS;

	// Internal array of both players.
	private Player[] _players = new Player[2];
	// Initialize the board.
	{
		for (int x = 0; x < this._board.length; x++) {
			for (int y = 0; y < this._board[0].length; y++) {
				this._board[x][y] = TicTacToe.EMPTY;
			}
		}
	}

	/**
	 * Creates a new <code>Game</code> with the specified players.
	 * 
	 * @param players
	 *            <code>Player[]</code> of the two players.
	 * @throws <code>IllegalArgumentException</code> unchecked, if there are not
	 *             2 players.
	 */
	public Game(final Player[] players) {
		if (players.length != 2) {
			throw new IllegalArgumentException("Game must be instantiated "
					+ "with two players");
		}
		this._players = players;
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
			final byte[][] b = Board.copy(this._board);
			final Move move = this._players[this._player].getMove(b, prev,
					this._player);

			if (move == null) {
				throw new NullPointerException("Player supplied null move");
			}

			// check square is empty
			if (!Board.isEmpty(this._board, move)) {
				System.out.println("That square is not empty");
				play(prev);
			}

			// place piece
			if (!Board.placePiece(this._board, move)) {
				System.out.println("Illegal Move - try again");
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

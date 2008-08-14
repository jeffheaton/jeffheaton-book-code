/*
 * Human
 *
 * Thomas David Baker, 2002-03-10
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Board;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Move;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.TicTacToe;

/**
 * Implementation of <code>Player</code> that allows for a human to play via
 * STDIN.
 * 
 * @author Thomas David Baker, bakert+tictactoe@gmail.com
 * @version 1.1
 */
public class PlayerHuman implements Player {

	// helper method for get move - asks stdin for a coordinate.
	private byte getCoord() {

		final BufferedReader in = new BufferedReader(new InputStreamReader(
				System.in));
		byte n = -1;
		try {
			n = Byte.parseByte(in.readLine());
		} catch (final IOException e) {
			System.out.println("Problem reading input!");
			return getCoord();
		} catch (final NumberFormatException e) {
			System.out.println("Not a number!");
			return getCoord();
		}

		if ((n < 0) || (n >= Board.SIZE)) {
			System.out
					.println("Not within range (0-" + (Board.SIZE - 1) + ")!");
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
	public Move getMove(final byte[][] board, final Move prev, final byte color) {
		Board.printBoard(board);
		// ask for move
		System.out.println("You are playing: " + TicTacToe.resolvePiece(color)
				+ ", select your move.");
		System.out.print("X position? ");
		final byte x = getCoord();
		System.out.print("Y position? ");
		final byte y = getCoord();

		return new Move(x, y, color);

	}

}
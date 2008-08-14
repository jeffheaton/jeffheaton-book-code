/*
 * Move
 *
 * Thomas David Baker, 2002-03-04
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game;

/**
 * Represents a move in a game of tic-tac-toe.
 * 
 * @author Thomas David Baker, bakert+tictactoe@gmail.com
 * @version 1.1
 */
public class Move {

	// player whose move this is
	private byte _color = TicTacToe.EMPTY;
	/** x-coordinate this move takes place at */
	public byte x = -1;
	/** y-coordinate this move takes place at */
	public byte y = -1;

	/**
	 * Creates a move with the specified coordinates for the specified player.
	 * 
	 * @param x
	 *            <code>int</code> x-coordinate of this move.
	 * @param y
	 *            <code>int</code> y-coordinate of this move.
	 * @param color
	 *            <code>int</code> type of piece played by this player (either
	 *            <code>TicTacToe.NOUGHTS</code> or
	 *            <code>TicTacToe.CROSSES</code>)
	 */
	public Move(final byte x, final byte y, final byte color) {
		this.x = x;
		this.y = y;
		this._color = color;
	}

	/**
	 * Gets the type of piece placed by this move.
	 * 
	 * @return <code>int</code>, either <code>TicTacToe.NOUGHTS</code> or
	 *         <code>TicTacToe.CROSSES</code>
	 */
	public byte color() {
		return this._color;
	}

	/**
	 * Gets a human-readable <code>String</code> representation of this move.
	 * 
	 * @return <code>String</code> representation of this move.
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " (" + this.x + ", " + this.y
				+ ") of color " + this._color;
	}

}

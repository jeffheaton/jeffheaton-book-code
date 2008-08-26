using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter6TicTacToe.Game
{
    class Move
    {
	// player whose move this is
	private int _color = TicTacToe.EMPTY;
	/** x-coordinate this move takes place at */
	public int x = -1;
	/** y-coordinate this move takes place at */
	public int y = -1;

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
	public Move( int x,  int y,  int color) {
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
	public int color() {
		return this._color;
	}

	/**
	 * Gets a human-readable <code>String</code> representation of this move.
	 * 
	 * @return <code>String</code> representation of this move.
	 */
	public String toString() {
		return this.GetType().Name + " (" + this.x + ", " + this.y
				+ ") of color " + this._color;
	}


    }
}

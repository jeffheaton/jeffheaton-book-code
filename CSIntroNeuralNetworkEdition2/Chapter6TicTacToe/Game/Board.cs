using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter6TicTacToe.Game
{
    class Board
    {

	/** Size of one side of the board. */
	public const int SIZE = 3;

	/**
	 * Makes a new copy of the specified board. This is a new object reference
	 * so changes to the original board will not affect it and vice versa.
	 * 
	 * @param board
	 *            <code>int[,]</code> board to copy.
	 * @return <code>int[,]</code> copy of board.
	 */
	public static int[,] copy( int[,] board) {

		 int[,] newBoard = new int[3,3];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				newBoard[x,y] = board[x,y];
			}
		}
		return newBoard;
	}

	/**
	 * Determines if the square the specified <code>Move</code> takes place in
	 * is empty or not.
	 * 
	 * @param move
	 *            <code>Move</code> to check position of.
	 * @return <code>bool</code>, true if the square is empty.
	 */
	public static bool isEmpty( int[,] board,  Move move) {
		return (board[move.x,move.y] == TicTacToe.EMPTY);
	}

	/**
	 * Determines if this board is full (no more moves possible) or not.
	 * 
	 * @return <code>bool</code>, true if this board is full.
	 */
	public static bool isFull( int[,] board) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x,y] == TicTacToe.EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Determines if the specified player has won at the current board position.
	 * 
	 * @param board
	 *            <code>int[,]</code> board to check.
	 * @param player
	 *            <code>int</code> either <code>TicTacToe.NOUGHTS</code> or
	 *            <code>TicTacToe.CROSSES</code>.
	 * @return <code>bool</code>, <code>true</code> if the specified
	 *         player has won.
	 */
	public static bool isWinner( int[,] board,  int player) {

		for (int x = 0; x < 3; x++) {
			if ((board[x,0] == player) && (board[x,1] == player)
					&& (board[x,2] == player)) {
				return true;
			}
		}

		for (int y = 0; y < 3; y++) {
			if ((board[0,y] == player) && (board[1,y] == player)
					&& (board[2,y] == player)) {
				return true;
			}
		}

		// Diagonals
		if ((board[0,0] == player) && (board[1,1] == player)
				&& (board[2,2] == player)) {
			return true;
		}
		if ((board[0,2] == player) && (board[1,1] == player)
				&& (board[2,0] == player)) {
			return true;
		}

		return false;
	}

	/**
	 * Does the move specified in <code>m</code> on <code>board</code>.
	 * 
	 * @param board
	 *            <code>int[,]</code> of board to perform move on.
	 * @param m
	 *            <code>Move</code> to perform.
	 * @return <code>bool</code> of whether the move succeeded or not.
	 * @throws IllegalStateException
	 *             If the move is not legal.
	 */
	public static bool placePiece( int[,] board,  Move m) {

		if (board[m.x,m.y] != TicTacToe.EMPTY) {
			throw new Exception("Tried to play a piece on "
					+ "top of another at (" + m.x + ", " + m.y + ")");
		} else {
			board[m.x,m.y] = m.color();
			return true;
		}
	}

	/** Prints a graphical representation of the board to STDOUT. */
	public static void printBoard( int[,] board) {

		Console.Write("  ");
		for (int i = 0; i < 3; i++) {
			Console.Write(i + " ");
		}
		Console.WriteLine();
		for (int y = 0; y < 3; y++) {
			Console.Write(" ");
			for (int i = 0; i < 3; i++) {
				Console.Write("--");
			}
			Console.WriteLine("- ");
			Console.Write(y + "|");
			for (int x = 0; x < 3; x++) {
				if (board[x,y] == TicTacToe.EMPTY) {
					Console.Write(" |");
				} else if (board[x,y] == TicTacToe.NOUGHTS) {
					Console.Write("o|");
				} else if (board[x,y] == TicTacToe.CROSSES) {
					Console.Write("x|");
				} else {
					throw new Exception("Square (" + x + ", " + y
							+ ") is not empty, white or black!");
				}
			}
			Console.WriteLine(y);
		}
		Console.Write(" ");
		for (int i = 0; i < board.Length; i++) {
			Console.Write("--");
		}
		Console.WriteLine("- ");
		Console.WriteLine(" ");
		Console.Write("  ");
		for (int i = 0; i < board.Length; i++) {
			Console.Write(i + " ");
		}
		Console.WriteLine();
	}


    }
}

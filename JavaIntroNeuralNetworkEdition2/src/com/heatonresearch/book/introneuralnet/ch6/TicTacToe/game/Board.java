/*
 * Board
 *
 * Thomas David Baker, 2002-03-10
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game;

/**
 * Methods that work on a tictactoe board. The board is represented as a
 * <code>byte[][]</code> and worked on with static methods rather than being
 * an object with behaviour so that AI players can have many many copies of
 * boards in memory without making the memory requirements outrageous.
 * 
 * @author Thomas David Baker, bakert+tictactoe@gmail.com
 * @version 1.1
 */
public class Board {

	/** Size of one side of the board. */
	public static final byte SIZE = 3;

	/**
	 * Makes a new copy of the specified board. This is a new object reference
	 * so changes to the original board will not affect it and vice versa.
	 * 
	 * @param board
	 *            <code>byte[][]</code> board to copy.
	 * @return <code>byte[][]</code> copy of board.
	 */
	public static byte[][] copy(final byte[][] board) {

		final byte[][] newBoard = new byte[board.length][board[0].length];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				newBoard[x][y] = board[x][y];
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
	 * @return <code>boolean</code>, true if the square is empty.
	 */
	public static boolean isEmpty(final byte[][] board, final Move move) {
		return (board[move.x][move.y] == TicTacToe.EMPTY);
	}

	/**
	 * Determines if this board is full (no more moves possible) or not.
	 * 
	 * @return <code>boolean</code>, true if this board is full.
	 */
	public static boolean isFull(final byte[][] board) {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				if (board[x][y] == TicTacToe.EMPTY) {
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
	 *            <code>byte[][]</code> board to check.
	 * @param player
	 *            <code>byte</code> either <code>TicTacToe.NOUGHTS</code> or
	 *            <code>TicTacToe.CROSSES</code>.
	 * @return <code>boolean</code>, <code>true</code> if the specified
	 *         player has won.
	 */
	public static boolean isWinner(final byte[][] board, final byte player) {

		for (int x = 0; x < board.length; x++) {
			if ((board[x][0] == player) && (board[x][1] == player)
					&& (board[x][2] == player)) {
				return true;
			}
		}

		for (int y = 0; y < board[0].length; y++) {
			if ((board[0][y] == player) && (board[1][y] == player)
					&& (board[2][y] == player)) {
				return true;
			}
		}

		// Diagonals
		if ((board[0][0] == player) && (board[1][1] == player)
				&& (board[2][2] == player)) {
			return true;
		}
		if ((board[0][2] == player) && (board[1][1] == player)
				&& (board[2][0] == player)) {
			return true;
		}

		return false;
	}

	/**
	 * Does the move specified in <code>m</code> on <code>board</code>.
	 * 
	 * @param board
	 *            <code>byte[][]</code> of board to perform move on.
	 * @param m
	 *            <code>Move</code> to perform.
	 * @return <code>boolean</code> of whether the move succeeded or not.
	 * @throws IllegalStateException
	 *             If the move is not legal.
	 */
	public static boolean placePiece(final byte[][] board, final Move m) {

		if (board[m.x][m.y] != TicTacToe.EMPTY) {
			throw new IllegalStateException("Tried to play a piece on "
					+ "top of another at (" + m.x + ", " + m.y + ")");
		} else {
			board[m.x][m.y] = m.color();
			return true;
		}
	}

	/** Prints a graphical representation of the board to STDOUT. */
	public static void printBoard(final byte[][] board) {

		System.out.print("  ");
		for (int i = 0; i < board.length; i++) {
			System.out.print(i + " ");
		}
		System.out.println();
		for (int y = 0; y < board.length; y++) {
			System.out.print(" ");
			for (int i = 0; i < board[0].length; i++) {
				System.out.print("--");
			}
			System.out.println("- ");
			System.out.print(y + "|");
			for (int x = 0; x < board.length; x++) {
				if (board[x][y] == TicTacToe.EMPTY) {
					System.out.print(" |");
				} else if (board[x][y] == TicTacToe.NOUGHTS) {
					System.out.print("o|");
				} else if (board[x][y] == TicTacToe.CROSSES) {
					System.out.print("x|");
				} else {
					throw new IllegalStateException("Square (" + x + ", " + y
							+ ") is not empty, white or black!");
				}
			}
			System.out.println(y);
		}
		System.out.print(" ");
		for (int i = 0; i < board.length; i++) {
			System.out.print("--");
		}
		System.out.println("- ");
		System.out.println(" ");
		System.out.print("  ");
		for (int i = 0; i < board.length; i++) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
}

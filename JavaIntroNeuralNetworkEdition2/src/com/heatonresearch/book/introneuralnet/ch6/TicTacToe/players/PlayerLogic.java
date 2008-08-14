package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Board;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Move;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.TicTacToe;

/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 * 
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * PlayerLogic: Play tic-tac-toe using traditional logic programming.
 *  
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class PlayerLogic implements Player {

	private Move canWin(final byte board[][], final byte color) {
		for (int i = 0; i < 3; i++) {
			final Move move1 = new Move((byte) 0, (byte) i, color);
			final Move move2 = new Move((byte) 1, (byte) i, color);
			final Move move3 = new Move((byte) 2, (byte) i, color);
			final Move move = findWin(board, move1, move2, move3, color);
			if (move != null) {
				return move;
			}
		}

		for (int i = 0; i < 3; i++) {
			final Move move1 = new Move((byte) i, (byte) 0, color);
			final Move move2 = new Move((byte) i, (byte) 1, color);
			final Move move3 = new Move((byte) i, (byte) 2, color);
			final Move move = findWin(board, move1, move2, move3, color);
			if (move != null) {
				return move;
			}
		}

		Move move1 = new Move((byte) 0, (byte) 0, color);
		Move move2 = new Move((byte) 0, (byte) 0, color);
		Move move3 = new Move((byte) 0, (byte) 0, color);
		Move move = findWin(board, move1, move2, move3, color);
		if (move != null) {
			return move;
		}

		move1 = new Move((byte) 2, (byte) 0, color);
		move2 = new Move((byte) 0, (byte) 0, color);
		move3 = new Move((byte) 0, (byte) 2, color);
		move = findWin(board, move1, move2, move3, color);
		if (move != null) {
			return move;
		}

		return null;
	}

	private Move centerOpen(final byte[][] board, final byte color) {
		final Move center = new Move((byte) 1, (byte) 1, color);
		if (Board.isEmpty(board, center)) {
			return center;
		} else {
			return null;
		}
	}

	private Move cornerOpen(final byte[][] board, final byte color) {
		final Move corner1 = new Move((byte) 0, (byte) 0, color);
		final Move corner2 = new Move((byte) 2, (byte) 2, color);
		final Move corner3 = new Move((byte) 2, (byte) 0, color);
		final Move corner4 = new Move((byte) 0, (byte) 2, color);

		if (Board.isEmpty(board, corner1)) {
			return corner1;
		} else if (Board.isEmpty(board, corner2)) {
			return corner2;
		} else if (Board.isEmpty(board, corner3)) {
			return corner3;
		} else if (Board.isEmpty(board, corner4)) {
			return corner4;
		} else {
			return null;
		}
	}

	private Move findWin(final byte[][] board, final Move move1,
			final Move move2, final Move move3, final int color) {
		if ((board[move1.x][move1.y] == color)
				&& (board[move2.x][move2.y] == color)
				&& (board[move3.x][move3.y] == TicTacToe.EMPTY)) {
			return move3;
		} else if ((board[move2.x][move2.y] == color)
				&& (board[move3.x][move3.y] == color)
				&& (board[move1.x][move1.y] == TicTacToe.EMPTY)) {
			return move1;
		} else if ((board[move1.x][move1.y] == color)
				&& (board[move3.x][move3.y] == color)
				&& (board[move2.x][move2.y] == TicTacToe.EMPTY)) {
			return move2;
		} else {
			return null;
		}
	}

	public Move getMove(final byte[][] board, final Move prev, final byte color) {

		if (Board.isFull(board)) {
			return null;
		}

		Move result = null;
		final byte other = TicTacToe.reverse(color);

		if (result == null) {
			result = canWin(board, color);
		}

		if (result == null) {
			result = canWin(board, other);
		}

		// Step 1: Is the center open
		if (result == null) {
			result = centerOpen(board, color);
		}

		// Step 2: Is the corner open
		if (result == null) {
			result = cornerOpen(board, color);
		}

		// Step 3: Move somewhere
		if (result == null) {
			result = moveSomewhere(board, color);
		}

		if (result.color() != color) {
			result = new Move(result.x, result.y, color);
		}

		return result;
	}

	public Move moveSomewhere(final byte[][] board, final byte player) {

		for (;;) {
			final byte x = (byte) (Math.random() * 3.0);
			final byte y = (byte) (Math.random() * 3.0);
			final Move m = new Move(x, y, player);
			if (Board.isEmpty(board, m)) {
				return m;
			}
		}

	}

}

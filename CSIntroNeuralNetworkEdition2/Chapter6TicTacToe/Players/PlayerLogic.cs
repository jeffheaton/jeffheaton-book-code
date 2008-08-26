using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players
{
    class PlayerLogic: Player
    {


	private Move canWin( int [,]board,  int color) {
        Move move1, move2, move3, move;
		for (int i = 0; i < 3; i++) {
			  move1 = new Move((int) 0, (int) i, color);
			 move2 = new Move((int) 1, (int) i, color);
			  move3 = new Move((int) 2, (int) i, color);
			  move = findWin(board, move1, move2, move3, color);
			if (move != null) {
				return move;
			}
		}

		for (int i = 0; i < 3; i++) {
			  move1 = new Move((int) i, (int) 0, color);
			 move2 = new Move((int) i, (int) 1, color);
			  move3 = new Move((int) i, (int) 2, color);
			  move = findWin(board, move1, move2, move3, color);
			if (move != null) {
				return move;
			}
		}

		move1 = new Move((int) 0, (int) 0, color);
		move2 = new Move((int) 1, (int) 1, color);
		move3 = new Move((int) 2, (int) 2, color);
		move = findWin(board, move1, move2, move3, color);
		if (move != null) {
			return move;
		}

		move1 = new Move((int) 2, (int) 0, color);
		move2 = new Move((int) 0, (int) 0, color);
		move3 = new Move((int) 0, (int) 2, color);
		move = findWin(board, move1, move2, move3, color);
		if (move != null) {
			return move;
		}

		return null;
	}

	private Move centerOpen( int[,] board,  int color) {
		 Move center = new Move((int) 1, (int) 1, color);
		if (Board.isEmpty(board, center)) {
			return center;
		} else {
			return null;
		}
	}

	private Move cornerOpen( int[,] board,  int color) {
		 Move corner1 = new Move((int) 0, (int) 0, color);
		 Move corner2 = new Move((int) 2, (int) 2, color);
		 Move corner3 = new Move((int) 2, (int) 0, color);
		 Move corner4 = new Move((int) 0, (int) 2, color);

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

	private Move findWin( int[,] board,  Move move1,
			 Move move2,  Move move3,  int color) {
		if ((board[move1.x,move1.y] == color)
				&& (board[move2.x,move2.y] == color)
				&& (board[move3.x,move3.y] == TicTacToe.EMPTY)) {
			return move3;
		} else if ((board[move2.x,move2.y] == color)
				&& (board[move3.x,move3.y] == color)
				&& (board[move1.x,move1.y] == TicTacToe.EMPTY)) {
			return move1;
		} else if ((board[move1.x,move1.y] == color)
				&& (board[move3.x,move3.y] == color)
				&& (board[move2.x,move2.y] == TicTacToe.EMPTY)) {
			return move2;
		} else {
			return null;
		}
	}

	public Move getMove( int[,] board,  Move prev,  int color) {

		if (Board.isFull(board)) {
			return null;
		}

		Move result = null;
		 int other = TicTacToe.reverse(color);

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

	public Move moveSomewhere( int[,] board,  int player) {
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

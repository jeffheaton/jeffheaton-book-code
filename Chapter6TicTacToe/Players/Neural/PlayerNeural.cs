using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Feedforward;
using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players.Neural
{
    class PlayerNeural: Player
    {

        	private FeedforwardNetwork network;

	public PlayerNeural( FeedforwardNetwork network) {
		this.network = network;
	}

	public Move getMove( int[,] board,  Move prev,  int color) {
		Move bestMove = null;
		double bestScore = double.MinValue;

		for (int x = 0; x <3; x++) {
			for (int y = 0; y < 3; y++) {
				 Move move = new Move((int) x, (int) y, color);
				if (Board.isEmpty(board, move)) {
					double d = tryMove(board, move);
					if ((d > bestScore) || (bestMove == null)) {
						bestScore = d;
						bestMove = move;
					}
				}
			}
		}

		return bestMove;

	}

	private double tryMove( int[,] board,  Move move) {
		 double []input = new double[9];
		int index = 0;

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x,y] == TicTacToe.NOUGHTS) {
					input[index] = -1;
				} else if (board[x,y] == TicTacToe.CROSSES) {
					input[index] = 1;
				} else if (board[x,y] == TicTacToe.EMPTY) {
					input[index] = 0;
				}

				if ((x == move.x) && (y == move.y)) {
					input[index] = -1;
				}

				index++;
			}
		}

		double []output = this.network.ComputeOutputs(input);
		return output[0];
	}

    }
}

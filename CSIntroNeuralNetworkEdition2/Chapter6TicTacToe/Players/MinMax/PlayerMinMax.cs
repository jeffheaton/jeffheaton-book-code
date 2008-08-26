using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players.MinMax
{
    class PlayerMinMax: Player
    {
	// Internal representation of wheter this AI is noughts or crosses.
	private int _player = TicTacToe.EMPTY;

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
	public Move getMove( int[,] board,  Move prev,  int color) {

		this._player = color;

		 Node root = new Node(board, prev);
		int max = int.MinValue;

		Node child;
		Node bestNode = null;

		while ((child = root.getChild()) != null) {
			 int val = minimaxAB(child, true, int.MinValue,
					int.MaxValue);
			if (val >= max) {
				max = val;
				bestNode = child;
			}
		}

		return bestNode.move();

	}

	// Implementation of mimimax with alpha-beta pruning.
	private int minimaxAB( Node n,  bool min,  int a,
			 int b) {

		int alpha = a;
		int beta = b;

		if (n.isLeaf()) {
			return n.value(this._player);
		}

		Node child;

		if (min) {
			while (((child = n.getChild()) != null) && !(alpha > beta)) {
				 int val = minimaxAB(child, false, alpha, beta);
				if (val < beta) {
					beta = val;
				}
			}
			return beta;
		} else {
			while (((child = n.getChild()) != null) && !(alpha > beta)) {
				 int val = minimaxAB(child, true, alpha, beta);
				if (val > alpha) {
					alpha = val;
				}
			}
			return alpha;
		}
	}

    }
}

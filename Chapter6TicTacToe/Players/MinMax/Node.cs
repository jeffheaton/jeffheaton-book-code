using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players.MinMax
{
    class Node
    {


	// Board position for this node (after its move has taken place).
	private  int[,] _board;
	// Place marker that stores how far along the x-axis the search
	// for children of this node has gone.
	private int _x = 0;
	// Place marker that stores how far along the y-axis the search
	// for children of this node has gone.
	private int _y = 0;
	// Internal representation of whether this node is known to be a
	// leaf node (true), known to have children (false) or not yet
	// determined (null).
	private bool _leaf = false;
    private bool _leafdef = false;
	// The last move played to reach this node.
	private Move _move = null;
	// List of children so far. This list contains 0 or more of this
	// nodes children. It is not guaranteed to contain all the node's children
	// until getChild() returns null.
	// 2005-06-07 changed from being an ArrayList to an array to save
	// space/initialization
	private  Node[] _children = new Node[9];
	private int childNum = 0;

	/** Value of this node as determined by Minimax with alpha-beta pruning. */
	public int score = 0;

	/**
	 * Creates a node with the specified board position, reached by the
	 * specified move.
	 * 
	 * @param board
	 *            <code>Board</code> of the board position of this node.
	 * @param move
	 *            <code>Move</code> last move that was taken to reach this
	 *            board position.
	 */
	public Node( int[,] board,  Move move) {
		this._board = board;
		this._move = move;
	}

	/**
	 * Gets the board position represented by this node.
	 * 
	 * @return <code>Board</code> representation of board position.
	 */
	public int[,] board() {
		return this._board;
	}

	/**
	 * Returns all the children of this node found so far. You probably won't
	 * want to call this method until you're sure you're done with getChild().
	 * 
	 * @return <code>Node[]</code> of all known children for this node.
	 * @see getChild()
	 */
	public Node[] children() {
		 Node[] temp = new Node[this.childNum + 1];
		for (int i = 0; i < this.childNum + 1; i++) {
			temp[i] = this._children[i];
		}
		return temp;
	}

	/**
	 * Gets the "next" child for this node, or <code>null</code> if all
	 * children of this node have already been found.
	 * 
	 * @return <code>Node</code> Representation of child (one move on) in the
	 *         game tree.
	 */
	public Node getChild() {

		bool firstColumn = true;

		for (int x = this._x; x < 3; x++) {
			int starter = 0;
			if (firstColumn) {
				starter = this._y;
			}
			for (int y = starter; y < 3; y++) {
				 Move m = new Move(x, y, this.player());
				if (Board.isEmpty(this._board, m)) {
					 int[,] newPos = Board.copy(this._board);
					Board.placePiece(newPos, m);
					 Node child = new Node(newPos, m);
					this._children[this.childNum++] = child;
					if (y >= (3 - 1)) {
						this._x = (int) (x + 1);
						this._y = 0;
					} else {
						this._x = x;
						this._y = (int) (y + 1);
					}
					this._leaf = false;
                    this._leafdef = true;
					return child;
				}
			}
			firstColumn = false;
		}

		this._leaf = true;
        this._leafdef = true;
		return null;

	}

	/**
	 * Determines if this node is a leaf on the game tree.
	 * 
	 * @return <code>bool</code>, <code>true</code> if the node is a
	 *         leaf.
	 */
	public bool isLeaf() {
		if (this._leafdef == false) {
			return (Board.isFull(this._board)
					|| Board.isWinner(this._board, TicTacToe.NOUGHTS) || Board
					.isWinner(this._board, TicTacToe.CROSSES));
		}
		return this._leaf;
	}

	/**
	 * Gets the last move taken to reach this node on the game tree.
	 * 
	 * @return <code>Move</code> of the move taken to reach this node.
	 */
	public Move move() {
		return this._move;
	}

	/**
	 * Gets the code for the player ABOUT TO MOVE from this node (not the player
	 * who has just moved to create this position.
	 * 
	 * @return <code>int</code> either <code>TicTacToe.NOUGHTS</code> or
	 *         <code>TicTacToe.CROSSES</code>
	 */
	public int player() {
		return (this._move == null ? TicTacToe.NOUGHTS : TicTacToe
				.reverse(this._move.color()));
	}

	/**
	 * Determines the value of this node to the specified player.
	 * 
	 * @param player
	 *            <code>int</code> either <code>TicTacToe.NOUGHTS</code> or
	 *            <code>TicTacToe.CROSSES</code>.
	 * @return <code>int</code> abstract value only meaningful in comparison
	 *         to other node values. Higher is better for the specified player.
	 */
	public int value( int player) {

		if (Board.isWinner(this._board, player)) {
			return int.MaxValue;
			// If they've lost this node has minimum value
		} else if (Board.isWinner(this._board, TicTacToe.reverse(player))) {
			return int.MinValue;
		} else {
			return 0;
		}
	}

    }
}

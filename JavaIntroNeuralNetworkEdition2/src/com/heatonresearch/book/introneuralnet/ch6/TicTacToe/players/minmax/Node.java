/*
 * Node
 *
 * Thomas David Baker, 2003-03-11
 */

package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.minmax;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Board;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Move;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.TicTacToe;

/**
 * Represents a node in the game tree. Used by the <code>AI</code>.
 */
class Node {

	// Board position for this node (after its move has taken place).
	private final byte[][] _board;
	// Place marker that stores how far along the x-axis the search
	// for children of this node has gone.
	private byte _x = 0;
	// Place marker that stores how far along the y-axis the search
	// for children of this node has gone.
	private byte _y = 0;
	// Internal representation of whether this node is known to be a
	// leaf node (true), known to have children (false) or not yet
	// determined (null).
	private Boolean _leaf = null;
	// The last move played to reach this node.
	private Move _move = null;
	// List of children so far. This list contains 0 or more of this
	// nodes children. It is not guaranteed to contain all the node's children
	// until getChild() returns null.
	// 2005-06-07 changed from being an ArrayList to an array to save
	// space/initialization
	private final Node[] _children = new Node[9];
	private byte childNum = 0;

	/** Value of this node as determined by Minimax with alpha-beta pruning. */
	public byte score = 0;

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
	public Node(final byte[][] board, final Move move) {
		this._board = board;
		this._move = move;
	}

	/**
	 * Gets the board position represented by this node.
	 * 
	 * @return <code>Board</code> representation of board position.
	 */
	public byte[][] board() {
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
		final Node[] temp = new Node[this.childNum + 1];
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

		boolean firstColumn = true;

		for (byte x = this._x; x < this._board.length; x++) {
			byte starter = 0;
			if (firstColumn) {
				starter = this._y;
			}
			for (byte y = starter; y < this._board[0].length; y++) {
				final Move m = new Move(x, y, this.player());
				if (Board.isEmpty(this._board, m)) {
					final byte[][] newPos = Board.copy(this._board);
					Board.placePiece(newPos, m);
					final Node child = new Node(newPos, m);
					this._children[this.childNum++] = child;
					if (y >= (this._board[0].length - 1)) {
						this._x = (byte) (x + 1);
						this._y = 0;
					} else {
						this._x = x;
						this._y = (byte) (y + 1);
					}
					this._leaf = new Boolean(false);
					return child;
				}
			}
			firstColumn = false;
		}

		this._leaf = new Boolean(true);
		return null;

	}

	/**
	 * Determines if this node is a leaf on the game tree.
	 * 
	 * @return <code>boolean</code>, <code>true</code> if the node is a
	 *         leaf.
	 */
	public boolean isLeaf() {
		if (this._leaf == null) {
			return (Board.isFull(this._board)
					|| Board.isWinner(this._board, TicTacToe.NOUGHTS) || Board
					.isWinner(this._board, TicTacToe.CROSSES));
		}
		return this._leaf.booleanValue();
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
	public byte player() {
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
	public byte value(final byte player) {

		if (Board.isWinner(this._board, player)) {
			return Byte.MAX_VALUE;
			// If they've lost this node has minimum value
		} else if (Board.isWinner(this._board, TicTacToe.reverse(player))) {
			return Byte.MIN_VALUE;
		} else {
			return 0;
		}
	}
}

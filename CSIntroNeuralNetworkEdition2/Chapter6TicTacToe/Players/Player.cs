using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players
{
    interface Player
    {
        /**
 * Gets the next move for this player.
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
 * @return <code>Move</code> Next move for the player.
 */
        Move getMove(int[,] board, Move prev, int color);
    }
}

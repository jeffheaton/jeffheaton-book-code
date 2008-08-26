using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Chapter6TicTacToe.Players;

namespace Chapter6TicTacToe.Game
{
    class ScorePlayer
    {
	public const int WIN_POINTS = 50;
	public const int DRAW_POINTS = 20;
	public const int LOSE_POINTS = 1;

	public const double PERFECT_SCORE = NeuralTicTacToe.SCORE_SAMPLE
			* WIN_POINTS;
	Player player;
	Player aganst;
	bool verbose;

	public ScorePlayer( Player player,  Player aganst,
			 bool verbose) {
		this.player = player;
		this.aganst = aganst;
		this.verbose = verbose;
	}

	public double score() {
		int win = 0;
		int lose = 0;
		int draw = 0;

		for (int i = 0; i < NeuralTicTacToe.SCORE_SAMPLE; i++) {

			 Player []players = new Player[2];
			players[0] = this.player;
			players[1] = this.aganst;
			 Game game = new Game(players);
			 Player winner = game.play();

			if (this.verbose) {
				Console.WriteLine("Game #" + i + ":" + winner);
				game.printBoard();
			}

			if (winner == this.player) {
				win++;
			} else if (winner == this.aganst) {
				lose++;
			} else {
				draw++;
			}
		}

		if (this.verbose) {
			Console.WriteLine("Win:" + win);
			Console.WriteLine("Lose:" + lose);
			Console.WriteLine("Draw:" + draw);
		}

		 double score = (win * WIN_POINTS) + (lose * LOSE_POINTS)
				+ (draw * DRAW_POINTS);

		 double s = (PERFECT_SCORE - score) / PERFECT_SCORE;

		if (s < 0) {
			return -1;
		}
		return s;

	}

    }
}

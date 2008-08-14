package com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.NeuralTicTacToe;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.Player;

public class ScorePlayer {
	public final static int WIN_POINTS = 50;
	public final static int DRAW_POINTS = 20;
	public final static int LOSE_POINTS = 1;

	public final static double PERFECT_SCORE = NeuralTicTacToe.SCORE_SAMPLE
			* WIN_POINTS;
	Player player;
	Player aganst;
	boolean verbose;

	public ScorePlayer(final Player player, final Player aganst,
			final boolean verbose) {
		this.player = player;
		this.aganst = aganst;
		this.verbose = verbose;
	}

	public double score() {
		int win = 0;
		int lose = 0;
		int draw = 0;

		for (int i = 0; i < NeuralTicTacToe.SCORE_SAMPLE; i++) {

			final Player players[] = new Player[2];
			players[0] = this.player;
			players[1] = this.aganst;
			final Game game = new Game(players);
			final Player winner = game.play();

			if (this.verbose) {
				System.out.println("Game #" + i + ":" + winner);
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
			System.out.println("Win:" + win);
			System.out.println("Lose:" + lose);
			System.out.println("Draw:" + draw);
		}

		final double score = (win * WIN_POINTS) + (lose * LOSE_POINTS)
				+ (draw * DRAW_POINTS);

		final double s = (PERFECT_SCORE - score) / PERFECT_SCORE;

		if (s < 0) {
			return -1;
		}
		return s;

	}
}

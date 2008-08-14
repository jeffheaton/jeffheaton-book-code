/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 *   
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */
package com.heatonresearch.book.introneuralnet.ch6.TicTacToe;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;

import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.Game;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.game.ScorePlayer;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.Player;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.PlayerBoring;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.PlayerHuman;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.PlayerLogic;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.PlayerRandom;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.minmax.PlayerMinMax;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.neural.PlayerNeural;
import com.heatonresearch.book.introneuralnet.ch6.TicTacToe.players.neural.TicTacToeGenetic;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.util.SerializeObject;

/**
 * Chapter 6: Training using a Genetic Algorithm
 * 
 * NeuralTicTacToe: Use a neural network and genetic training to 
 * play tic-tac-toe.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class NeuralTicTacToe {

	public final static int POPULATION_SIZE = 200;
	public final static double MUTATION_PERCENT = 0.10;
	public final static double MATE_PERCENT = 0.25;
	public final static int NEURONS_HIDDEN_1 = 10;
	public final static int NEURONS_HIDDEN_2 = 0;
	public final static int TRAIN_MINUTES = 300;
	public final static int SCORE_SAMPLE = 100;
	public final static int THREAD_POOL_SIZE = 2;

	public static FeedforwardNetwork createNetwork() {
		final FeedforwardNetwork network = new FeedforwardNetwork();
		network.addLayer(new FeedforwardLayer(9));
		network
				.addLayer(new FeedforwardLayer(NeuralTicTacToe.NEURONS_HIDDEN_1));
		if (NeuralTicTacToe.NEURONS_HIDDEN_2 > 0) {
			network.addLayer(new FeedforwardLayer(
					NeuralTicTacToe.NEURONS_HIDDEN_2));
		}
		network.addLayer(new FeedforwardLayer(1));
		network.reset();
		return network;
	}

	public static Player getPlayer(final String name) throws IOException,
			ClassNotFoundException {
		if (name.equalsIgnoreCase("MinMax")) {
			return new PlayerMinMax();
		} else if (name.equalsIgnoreCase("Boring")) {
			return new PlayerBoring();
		} else if (name.equalsIgnoreCase("Human")) {
			return new PlayerHuman();
		} else if (name.equalsIgnoreCase("Random")) {
			return new PlayerRandom();
		} else if (name.equalsIgnoreCase("NeuralLoad")) {
			return new PlayerNeural(loadNetwork());
		} else if (name.equalsIgnoreCase("NeuralBlank")) {
			return new PlayerNeural(createNetwork());
		} else if (name.equalsIgnoreCase("Logic")) {
			return new PlayerLogic();
		} else {
			return null;
		}
	}

	public static FeedforwardNetwork loadNetwork() throws IOException,
			ClassNotFoundException {
		final FeedforwardNetwork result = (FeedforwardNetwork) SerializeObject
				.load("tictactoe.net");
		return result;
	}

	/**
	 * Controlling method of the <code>TicTacToe</code> executable. Begins a
	 * game of tic-tac-toe. Expects class names of the players as two
	 * commandline arguments.
	 * 
	 * @param args
	 *            <code>String[]</code> commandline arguments. This should be
	 *            the class names of the players. If the players are in the
	 *            <code>net.blubones.tictactoe</code> package the qualifier
	 *            can be omitted, otherwise the full class names are required.
	 */
	public static void main(final String[] args) {
		try {
			final NeuralTicTacToe ttt = new NeuralTicTacToe();

			if (args.length < 3) {
				System.out
						.println("Usage: NeuralTicTacToe [game/match/train] [player1] [player2]");
				System.exit(0);
			}

			final Player player1 = getPlayer(args[1]);
			final Player player2 = getPlayer(args[2]);

			if (player1 == null) {
				System.out.println("Must specify player 1.");
				System.exit(0);
			}

			if (player2 == null) {
				System.out.println("Must specify player 2.");
				System.exit(0);
			}

			ttt.setPlayer1(player1);
			ttt.setPlayer2(player2);

			if (args[0].equalsIgnoreCase("game")) {
				ttt.singleGame();
			} else if (args[0].equalsIgnoreCase("match")) {
				ttt.playMatch();
			} else if (args[0].equalsIgnoreCase("train")) {
				ttt.geneticNeural();
			} else {
				System.out.println("Invalid mode.");
			}
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}

	private Player player1;

	private Player player2;

	public void geneticNeural() throws IOException {
		final FeedforwardNetwork network = createNetwork();
		// train the neural network
		System.out.println("Determining initial scores");
		final TicTacToeGenetic train = new TicTacToeGenetic(network, true,
				NeuralTicTacToe.POPULATION_SIZE,
				NeuralTicTacToe.MUTATION_PERCENT, NeuralTicTacToe.MATE_PERCENT,
				this.player2.getClass());
		train.setPool(Executors
				.newFixedThreadPool(NeuralTicTacToe.THREAD_POOL_SIZE));
		int epoch = 1;

		final Date started = new Date();
		Date now = new Date();
		int minutes = 0;
		do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + train.getScore()
					+ ",minutes left="
					+ (NeuralTicTacToe.TRAIN_MINUTES - minutes));
			epoch++;
			now = new Date();
			minutes = (int) (now.getTime() - started.getTime()) / 1000;
			minutes /= 60;
		} while (minutes < NeuralTicTacToe.TRAIN_MINUTES);

		train.getPool().shutdownNow();
		SerializeObject.save("tictactoe.net", train.getNetwork());
	}

	public void playMatch() {
		final Player[] players = new Player[2];
		players[0] = this.player1;
		players[1] = this.player2;
		final ScorePlayer score = new ScorePlayer(players[0], players[1], true);
		System.out.println("Score:" + score.score());
	}

	public void playNeural() throws IOException, ClassNotFoundException {
		final FeedforwardNetwork network = loadNetwork();

		final Player[] players = new Player[2];
		players[0] = new PlayerNeural(network);
		players[1] = new PlayerMinMax();
		final ScorePlayer score = new ScorePlayer(players[0], players[1], true);
		score.score();

	}

	public void setPlayer1(final Player player1) {
		this.player1 = player1;

	}

	public void setPlayer2(final Player player2) {
		this.player2 = player2;

	}

	public void singleGame() {
		Game game = null;

		final Player[] players = new Player[2];
		players[0] = this.player1;
		players[1] = this.player2;
		game = new Game(players);
		final Player winner = game.play();
		System.out.println("Winner is:" + winner);
	}
}

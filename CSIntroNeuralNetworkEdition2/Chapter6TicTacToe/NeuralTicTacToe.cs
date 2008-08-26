using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Util;
using Chapter6TicTacToe.Players;
using Chapter6TicTacToe.Players.MinMax;
using Chapter6TicTacToe.Players.Neural;
using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe
{
    class NeuralTicTacToe
    {
        public const int POPULATION_SIZE = 200;
        public const double MUTATION_PERCENT = 0.10;
        public const double MATE_PERCENT = 0.25;
        public const int NEURONS_HIDDEN_1 = 10;
        public const int NEURONS_HIDDEN_2 = 0;
        public const int TRAIN_MINUTES = 5;
        public const int SCORE_SAMPLE = 100;
        public const int THREAD_POOL_SIZE = 2;

        public static FeedforwardNetwork createNetwork()
        {
            FeedforwardNetwork network = new FeedforwardNetwork();
            network.AddLayer(new FeedforwardLayer(9));
            network
                    .AddLayer(new FeedforwardLayer(NeuralTicTacToe.NEURONS_HIDDEN_1));
            if (NeuralTicTacToe.NEURONS_HIDDEN_2 > 0)
            {
                network.AddLayer(new FeedforwardLayer(
                        NeuralTicTacToe.NEURONS_HIDDEN_2));
            }
            network.AddLayer(new FeedforwardLayer(1));
            network.Reset();
            return network;
        }

        public static Player getPlayer(String name)
        {
            if (string.Equals(name, "MinMax", StringComparison.CurrentCultureIgnoreCase))
            {
                return new PlayerMinMax();
            }
            else if (string.Equals(name, "Boring", StringComparison.CurrentCultureIgnoreCase))
            {
                return new PlayerBoring();
            }
            else if (string.Equals(name, "Human", StringComparison.CurrentCultureIgnoreCase))
            {
                return new PlayerHuman();
            } if (string.Equals(name, "Random", StringComparison.CurrentCultureIgnoreCase))
            {
                return new PlayerRandom();
            } if (string.Equals(name, "NeuralLoad", StringComparison.CurrentCultureIgnoreCase))
            {
                return new PlayerNeural(loadNetwork());
            } if (string.Equals(name, "NeuralBlank", StringComparison.CurrentCultureIgnoreCase))
            {
                return new PlayerNeural(createNetwork());
            }
            else if (string.Equals(name, "Logic", StringComparison.CurrentCultureIgnoreCase))
            {
                return new PlayerLogic();
            }
            else
            {
                return null;
            }
        }

        public static FeedforwardNetwork loadNetwork()
        {
            FeedforwardNetwork result = (FeedforwardNetwork)SerializeObject
                   .Load("tictactoe.net");
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
        static void Main(string[] args)
        {
            try
            {
                NeuralTicTacToe ttt = new NeuralTicTacToe();

                if (args.Length < 3)
                {
                    Console.WriteLine("Usage: NeuralTicTacToe [game/match/train] [player1] [player2]");
                    return;
                }

                Player player1 = getPlayer(args[1]);
                Player player2 = getPlayer(args[2]);

                if (player1 == null)
                {
                    Console.WriteLine("Must specify player 1.");
                    return;
                }

                if (player2 == null)
                {
                    Console.WriteLine("Must specify player 2.");
                    return;
                }

                ttt.setPlayer1(player1);
                ttt.setPlayer2(player2);

                if (string.Equals(args[0], "Game", StringComparison.CurrentCultureIgnoreCase))
                {
                    ttt.singleGame();
                }
                else if (string.Equals(args[0], "Match", StringComparison.CurrentCultureIgnoreCase))
                {
                    ttt.playMatch();
                }
                else if (string.Equals(args[0], "Train", StringComparison.CurrentCultureIgnoreCase))
                {
                    ttt.geneticNeural();
                }
                else
                {
                    Console.WriteLine("Invalid mode.");
                }
            }
            catch (Exception t)
            {
                Console.WriteLine(t);
                Console.WriteLine(t.StackTrace);
            }
        }

        private Player player1;

        private Player player2;

        public void geneticNeural()
        {
            FeedforwardNetwork network = createNetwork();
            // train the neural network
            Console.WriteLine("Determining initial scores");
            TicTacToeGenetic train = new TicTacToeGenetic(network, true,
                   NeuralTicTacToe.POPULATION_SIZE,
                   NeuralTicTacToe.MUTATION_PERCENT, NeuralTicTacToe.MATE_PERCENT,
                   this.player2.GetType());
            train.UseThreadPool = true;
            ThreadPool.SetMaxThreads(NeuralTicTacToe.THREAD_POOL_SIZE, NeuralTicTacToe.THREAD_POOL_SIZE);
            int epoch = 1;

            DateTime started = DateTime.Now;

            int minutes = 0;
            do
            {
                train.Iteration();

                TimeSpan span = (DateTime.Now - started);
                minutes = span.Minutes;

                Console.WriteLine("Epoch #" + epoch + " Error:" + train.getScore()
                        + ",minutes left="
                        + (NeuralTicTacToe.TRAIN_MINUTES - minutes));
                epoch++;

            } while (minutes < NeuralTicTacToe.TRAIN_MINUTES);

            SerializeObject.Save("tictactoe.net", train.Network);
        }

        public void playMatch()
        {
            Player[] players = new Player[2];
            players[0] = this.player1;
            players[1] = this.player2;
            ScorePlayer score = new ScorePlayer(players[0], players[1], true);
            Console.WriteLine("Score:" + score.score());
        }

        public void playNeural()
        {
            FeedforwardNetwork network = loadNetwork();

            Player[] players = new Player[2];
            players[0] = new PlayerNeural(network);
            players[1] = new PlayerMinMax();
            ScorePlayer score = new ScorePlayer(players[0], players[1], true);
            score.score();

        }

        public void setPlayer1(Player player1)
        {
            this.player1 = player1;

        }

        public void setPlayer2(Player player2)
        {
            this.player2 = player2;

        }

        public void singleGame()
        {
            Game.Game game = null;

            Player[] players = new Player[2];
            players[0] = this.player1;
            players[1] = this.player2;
            game = new Game.Game(players);
            Player winner = game.play();
            Console.WriteLine("Winner is:" + winner);
        }


    }
}

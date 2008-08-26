using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Feedforward.Train.Genetic;
using Chapter6TicTacToe.Game;

namespace Chapter6TicTacToe.Players.Neural
{
    class TicTacToeChromosome : NeuralChromosome
    {
        /**
 * The constructor, takes a list of cities to set the initial "genes" to.
 * 
 * @param cities
 *            The order that this chromosome would visit the cities. These
 *            cities can be thought of as the genes of this chromosome.
 * @throws NeuralNetworkException
 */
        public TicTacToeChromosome(TicTacToeGenetic genetic,
                 FeedforwardNetwork network)
        {
            this.GA = genetic;
            this.Network = network;

            InitGenes(network.MatrixSize);
            UpdateGenes();
        }


        override public void CalculateCost()
        {


            // update the network with the new gene values
            this.UpdateNetwork();

            PlayerNeural player1 = new PlayerNeural(this.Network);
            Player player2;

            player2 = (Player)Assembly.GetExecutingAssembly().CreateInstance(this.getTicTacToeGenetic().getOpponent().FullName);

            ScorePlayer score = new ScorePlayer(player1, player2, false);

            double cost = score.score();
            // sometimes the score will be zero, just due to "luck"
            // if this is the case, try once more
            if (cost < 0.00001)
            {
                cost = score.score();
            }

            this.Cost = cost;
        }

        public TicTacToeGenetic getTicTacToeGenetic()
        {
            return (TicTacToeGenetic)this.GA;
        }

    }
}

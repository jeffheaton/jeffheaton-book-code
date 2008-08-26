using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Feedforward.Train.Genetic;

namespace Chapter6TicTacToe.Players.Neural
{
    class TicTacToeGenetic : NeuralGeneticAlgorithm
    {
        private Type opponent;




        public TicTacToeGenetic(FeedforwardNetwork network,
                 bool reset, int populationSize,
                 double mutationPercent, double percentToMate,
                 Type opponent)
        {


            this.setOpponent(opponent);
            this.MutationPercent=mutationPercent;
            this.MatingPopulation=percentToMate * 2;
            this.PopulationSize=populationSize;
            this.PercentToMate=percentToMate;

            this.Chromosomes = new TicTacToeChromosome[this.PopulationSize];
            for (int i = 0; i < this.Chromosomes.Length; i++)
            {
                FeedforwardNetwork chromosomeNetwork = (FeedforwardNetwork)network
                        .Clone();
                if (reset)
                {
                    chromosomeNetwork.Reset();
                }

                TicTacToeChromosome c = new TicTacToeChromosome(this,
                        chromosomeNetwork);
                c.UpdateGenes();
                SetChromosome(i, c);
            }
            SortChromosomes();
        }

        /**
         * @return the opponent
         */
        public Type getOpponent()
        {
            return this.opponent;
        }

        public double getScore()
        {
            NeuralChromosome c = (NeuralChromosome)GetChromosome(0);
            return c.Cost;
        }

        /**
         * @param opponent
         *            the opponent to set
         */
        public void setOpponent(Type opponent)
        {
            this.opponent = opponent;
        }
    }
}

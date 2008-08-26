using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Genetic;

namespace HeatonResearchNeural.Feedforward.Train.Genetic
{
    public class NeuralGeneticAlgorithm : GeneticAlgorithm<double>, Train
    {
        /// <summary>
        /// Get the current best neural network.
        /// </summary>
        public FeedforwardNetwork Network
        {
            get
            {
                NeuralChromosome c = (NeuralChromosome)GetChromosome(0);
                c.UpdateNetwork();
                return c.Network;
            }
        }
    }
}

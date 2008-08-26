using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Genetic;

namespace HeatonResearchNeural.Feedforward.Train.Genetic
{
    class TrainingSetNeuralChromosome : NeuralChromosome
    {
        /// <summary>
        /// The genes that make up this chromosome.
        /// </summary>
        public override double[] Genes
        {
            get
            {
                return base.Genes;
            }

            set
            {
                // copy the new genes
                base.Genes = value;

                CalculateCost();
            }
        }


        /// <summary>
        /// The constructor sets up to train the neural network with a GA.
        /// </summary>
        /// <param name="genetic">The genetic algorithm to use.</param>
        /// <param name="network">The neural network to use.</param>
        public TrainingSetNeuralChromosome(
                 TrainingSetNeuralGeneticAlgorithm genetic,
                 FeedforwardNetwork network)
        {
            this.GA =  genetic;
            this.Network = network;

            InitGenes(network.MatrixSize);
            UpdateGenes();
        }

        /// <summary>
        /// Calculate the cost for this chromosome.
        /// </summary>
        override public void CalculateCost()
        {
            // update the network with the new gene values
            this.UpdateNetwork();

            // update the cost with the new genes
            double[][] input = this.getTrainingSetNeuralGeneticAlgorithm().Input;
            double[][] ideal = this.getTrainingSetNeuralGeneticAlgorithm().Ideal;

            this.Cost  = this.Network.CalculateError(input, ideal);

        }

        /// <summary>
        /// Get the genetic algorithm used for this chromosome.
        /// </summary>
        /// <returns>The genetic algorithm used with this chromosome.</returns>
        public TrainingSetNeuralGeneticAlgorithm getTrainingSetNeuralGeneticAlgorithm()
        {
            return (TrainingSetNeuralGeneticAlgorithm)this.GA;
        }
    }
}

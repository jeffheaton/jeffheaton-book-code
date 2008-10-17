// Encog Neural Network and Bot Library for DotNet v0.5
// http://www.heatonresearch.com/encog/
// http://code.google.com/p/encog-cs/
// 
// Copyright 2008, Heaton Research Inc., and individual contributors.
// See the copyright.txt in the distribution for a full listing of 
// individual contributors.
//
// This is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation; either version 2.1 of
// the License, or (at your option) any later version.
//
// This software is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this software; if not, write to the Free
// Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
// 02110-1301 USA, or see the FSF site: http://www.fsf.org.

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Encog.Neural.Feedforward.Train.Genetic
{
    public class TrainingSetNeuralGeneticAlgorithm : NeuralGeneticAlgorithm
    {
        /// <summary>
        /// The training set's input array.
        /// </summary>
        public double[][] Input
        {
            get
            {
                return this.input;
            }
        }

        /// <summary>
        /// The training set's ideal output values.
        /// </summary>
        public double[][] Ideal
        {
            get
            {
                return this.ideal;
            }
        }

        /// <summary>
        /// The RMS error for the best chromosome.
        /// </summary>
        public new double Error
        {
            get
            {
                FeedforwardNetwork network = this.Network;
                return network.CalculateError(this.input, this.ideal);
            }
        }

        /// <summary>
        /// The input training set.
        /// </summary>
        protected double[][] input;

        /// <summary>
        /// The ideal values for the training set.
        /// </summary>
        protected double[][] ideal;

        /// <summary>
        /// Construct a genetic algorithm for a neural network that uses training sets.
        /// </summary>
        /// <param name="network">The neural network.</param>
        /// <param name="reset">Should each neural network be reset to random values.</param>
        /// <param name="input">The input training set.</param>
        /// <param name="ideal">The ideal values for the input training set.</param>
        /// <param name="populationSize">The initial population size.</param>
        /// <param name="mutationPercent">The mutation percentage.</param>
        /// <param name="percentToMate">The percentage of the population allowed to mate.</param>
        public TrainingSetNeuralGeneticAlgorithm(FeedforwardNetwork network,
                 bool reset, double[][] input,
                 double[][] ideal, int populationSize,
                 double mutationPercent, double percentToMate)
        {

            this.MutationPercent = mutationPercent;
            this.MatingPopulation = percentToMate * 2;
            this.PopulationSize = populationSize;
            this.PercentToMate = percentToMate;

            this.input = input;
            this.ideal = ideal;

            this.Chromosomes = new TrainingSetNeuralChromosome[this.PopulationSize];
            for (int i = 0; i < this.Chromosomes.Length; i++)
            {
                FeedforwardNetwork chromosomeNetwork = (FeedforwardNetwork)network
                       .Clone();
                if (reset)
                {
                    chromosomeNetwork.Reset();
                }

                TrainingSetNeuralChromosome c = new TrainingSetNeuralChromosome(
                       this, chromosomeNetwork);
                c.UpdateGenes();
                SetChromosome(i, c);
            }
            SortChromosomes();
        }
    }
}

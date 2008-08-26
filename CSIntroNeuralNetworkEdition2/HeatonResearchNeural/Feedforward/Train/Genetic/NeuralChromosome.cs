using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Genetic;
using HeatonResearchNeural.Matrix;

namespace HeatonResearchNeural.Feedforward.Train.Genetic
{
    abstract public class NeuralChromosome : Chromosome<double>
    {
        /// <summary>
        /// The neural network associated with this chromosome.
        /// </summary>
        public FeedforwardNetwork Network
        {
            get
            {
                return this.network;
            }
            set
            {
                this.network = value;
            }
        }

        /// <summary>
        /// The genes that make up this chromosome.
        /// </summary>
        public override double[] Genes
        {
            set
            {
                // copy the new genes
                base.Genes = value;

                CalculateCost();
            }
            get
            {
                return base.Genes;
            }
        }


        /// <summary>
        /// The range to mutate over.  How large of mutations are allowed.
        /// </summary>
        private const double RANGE = 20.0;

        /// <summary>
        /// The current best neural network.
        /// </summary>
        private FeedforwardNetwork network;

        /// <summary>
        /// Initialize the genes to zero.
        /// </summary>
        /// <param name="length">How many genes should be initialized.</param>
        public void InitGenes(int length)
        {
            double[] result = new double[length];
            for (int i = 0; i < result.Length; i++)
            {
                result[i] = 0;
            }
            SetGenesDirect(result);
        }

        /// <summary>
        /// Mutate this chromosome randomly.
        /// </summary>
        override public void Mutate()
        {
            Random rand = new Random();

            int length = this.Genes.Length;
            for (int i = 0; i < length; i++)
            {
                double d = GetGene(i);
                double ratio = (int)((RANGE * rand.NextDouble()) - RANGE);
                d *= ratio;
                SetGene(i, d);
            }
        }

        /// <summary>
        /// Update the gene values from the network values.
        /// </summary>
        public void UpdateGenes()
        {
            this.Genes = MatrixCODEC.NetworkToArray(this.network);
        }

        /// <summary>
        /// Update the neural network from the gene values.
        /// </summary>
        public void UpdateNetwork()
        {
            MatrixCODEC.ArrayToNetwork(this.Genes, this.network);
        }
    }
}

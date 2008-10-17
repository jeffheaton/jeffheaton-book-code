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

using Encog.Matrix;

namespace Encog.Neural.Som
{
    public class SelfOrganizingMap
    {

        /// <summary>
        /// Get the input neuron count.
        /// </summary>
        public int InputNeuronCount
        {
            get
            {
                return this.inputNeuronCount;
            }
        }

        /// <summary>
        /// Get the normalization type.
        /// </summary>
        public NormalizationType NormalizationType
        {
            get
            {
                return this.normalizationType;
            }
        }

        /// <summary>
        /// Get the output neurons.
        /// </summary>
        public double[] Output
        {
            get
            {
                return this.output;
            }
        }

        /// <summary>
        /// Get the output neuron count.
        /// </summary>
        public int OutputNeuronCount
        {
            get
            {
                return this.outputNeuronCount;
            }
        }

        /// <summary>
        /// Get the output neuron weights.
        /// </summary>
        public Matrix.Matrix OutputWeights
        {
            get
            {
                return this.outputWeights;
            }
            set
            {
                this.outputWeights = value;
            }
        }



        /// <summary>
        /// Do not allow patterns to go below this very small number.
        /// </summary>
        public double VERYSMALL = Math.Pow(10, -30);

        /// <summary>
        /// The weights of the output neurons base on the input from the input
        /// neurons.
        /// </summary>
        Matrix.Matrix outputWeights;

        /// <summary>
        /// Output neuron activations
        /// </summary>
        protected double[] output;

        /// <summary>
        /// Number of input neurons
        /// </summary>
        protected int inputNeuronCount;

        /// <summary>
        /// Number of output neurons
        /// </summary>
        protected int outputNeuronCount;

        /// <summary>
        /// The normalization type.
        /// </summary>
        protected NormalizationType normalizationType;


        /// <summary>
        /// The constructor.
        /// </summary>
        /// <param name="inputCount">Number of input neurons.</param>
        /// <param name="outputCount">Number of output neurons.</param>
        /// <param name="normalizationType">The normalization type.</param>
        public SelfOrganizingMap(int inputCount, int outputCount,
                 NormalizationType normalizationType)
        {

            this.inputNeuronCount = inputCount;
            this.outputNeuronCount = outputCount;
            this.outputWeights = new Matrix.Matrix(this.outputNeuronCount,
                    this.inputNeuronCount + 1);
            this.output = new double[this.outputNeuronCount];
            this.normalizationType = normalizationType;
        }





        /// <summary>
        /// Determine the winner for the specified input. This is the number of the
        /// winning neuron.
        /// </summary>
        /// <param name="input">The input patter to present to the neural network.</param>
        /// <returns>The winning neuron.</returns>
        public int Winner(double[] input)
        {
            NormalizeInput normalizedInput = new NormalizeInput(input,
                   this.normalizationType);
            return Winner(normalizedInput);
        }


        /// <summary>
        /// Determine the winner for the specified input. This is the number of the
        /// winning neuron.
        /// </summary>
        /// <param name="input">The input pattern.</param>
        /// <returns>The winning neuron.</returns>
        public int Winner(NormalizeInput input)
        {
            int win = 0;

            double biggest = Double.MinValue;
            for (int i = 0; i < this.outputNeuronCount; i++)
            {
                Matrix.Matrix optr = this.outputWeights.GetRow(i);
                this.output[i] = MatrixMath
                        .DotProduct(input.InputMatrix, optr)
                        * input.Normfac;

                this.output[i] = (this.output[i] + 1.0) / 2.0;

                if (this.output[i] > biggest)
                {
                    biggest = this.output[i];
                    win = i;
                }

                if (this.output[i] < 0)
                {
                    this.output[i] = 0;
                }

                if (this.output[i] > 1)
                {
                    this.output[i] = 1;
                }
            }

            return win;
        }
    }
}

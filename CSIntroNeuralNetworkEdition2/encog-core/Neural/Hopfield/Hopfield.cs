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

namespace Encog.Neural.Hopfield
{


    public class HopfieldNetwork
    {


        /// <summary>
        /// The weight matrix for this neural network. A Hopfield neural network is a
        /// single layer, fully connected neural network.
        /// 
        /// The inputs and outputs to/from a Hopfield neural network are always
        /// boolean values.
        /// </summary>
        public Matrix.Matrix LayerMatrix
        {
            get
            {
                return this.weightMatrix;
            }
        }

        /// <summary>
        /// The number of neurons.
        /// </summary>
        public int Size
        {
            get
            {
                return this.weightMatrix.Rows;
            }
        }


        /// <summary>
        /// The weight matrix.
        /// </summary>
        private Matrix.Matrix weightMatrix;

        /// <summary>
        /// Construct a Hopfield neural network of the specified size.
        /// </summary>
        /// <param name="size">The number of neurons in the network.</param>
        public HopfieldNetwork(int size)
        {
            this.weightMatrix = new Matrix.Matrix(size, size);

        }

        /// <summary>
        /// Present a pattern to the neural network and receive the result.
        /// </summary>
        /// <param name="pattern">The pattern to be presented to the neural network.</param>
        /// <returns>The output from the neural network.</returns>
        public bool[] Present(bool[] pattern)
        {

            bool[] output = new bool[pattern.Length];

            // convert the input pattern into a matrix with a single row.
            // also convert the boolean values to bipolar(-1=false, 1=true)
            Matrix.Matrix inputMatrix = Matrix.Matrix.CreateRowMatrix(BiPolarUtil
                    .Bipolar2double(pattern));

            // Process each value in the pattern
            for (int col = 0; col < pattern.Length; col++)
            {
                Matrix.Matrix columnMatrix = this.weightMatrix.getCol(col);
                columnMatrix = MatrixMath.Transpose(columnMatrix);

                // The output for this input element is the dot product of the
                // input matrix and one column from the weight matrix.
                double dotProduct = MatrixMath.DotProduct(inputMatrix,
                        columnMatrix);

                // Convert the dot product to either true or false.
                if (dotProduct > 0)
                {
                    output[col] = true;
                }
                else
                {
                    output[col] = false;
                }
            }

            return output;
        }


        /// <summary>
        /// Train the neural network for the specified pattern. The neural network
        /// can be trained for more than one pattern. To do this simply call the
        /// train method more than once. 
        /// </summary>
        /// <param name="pattern">The pattern to train on.</param>
        public void Train(bool[] pattern)
        {
            if (pattern.Length != this.weightMatrix.Rows)
            {
                throw new NeuralNetworkError("Can't train a pattern of size "
                        + pattern.Length + " on a hopfield network of size "
                        + this.weightMatrix.Rows);
            }

            // Create a row matrix from the input, convert boolean to bipolar
            Matrix.Matrix m2 = Matrix.Matrix.CreateRowMatrix(BiPolarUtil
                    .Bipolar2double(pattern));
            // Transpose the matrix and multiply by the original input matrix
            Matrix.Matrix m1 = MatrixMath.Transpose(m2);
            Matrix.Matrix m3 = MatrixMath.Multiply(m1, m2);

            // matrix 3 should be square by now, so create an identity
            // matrix of the same size.
            Matrix.Matrix identity = MatrixMath.Identity(m3.Rows);

            // subtract the identity matrix
            Matrix.Matrix m4 = MatrixMath.Subtract(m3, identity);

            // now add the calculated matrix, for this pattern, to the
            // existing weight matrix.
            this.weightMatrix = MatrixMath.Add(this.weightMatrix, m4);

        }
    }
}

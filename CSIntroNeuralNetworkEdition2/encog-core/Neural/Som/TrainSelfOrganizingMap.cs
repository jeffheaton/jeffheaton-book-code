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


    public class TrainSelfOrganizingMap
    {
        /// <summary>
        /// Get the best error so far.
        /// </summary>
        public double BestError
        {
            get
            {
                return this.bestError;
            }
        }

        /// <summary>
        /// Get the error for this iteration.
        /// </summary>
        public double TotalError
        {
            get
            {
                return this.totalError;
            }
        }



        /// <summary>
        /// Small number, do not go below.
        /// </summary>
        public double VERYSMALL = Math.Pow(10, -30);
        /// <summary>
        /// The learning method, either additive or subtractive.
        /// </summary>
        public enum LearningMethod
        {
            ADDITIVE,
            SUBTRACTIVE
        }

        /// <summary>
        /// The self organizing map to train.
        /// </summary>
        private SelfOrganizingMap som;

        /// <summary>
        /// The learning method.
        /// </summary>
        protected LearningMethod learnMethod;

        /// <summary>
        /// The learning rate.
        /// </summary>
        protected double learnRate;

        /// <summary>
        /// Reduction factor.
        /// </summary>
        protected double reduction = .99;

        /// <summary>
        /// Mean square error of the network for the iteration.
        /// </summary>
        protected double totalError;


        /// <summary>
        /// Mean square of the best error found so far.
        /// </summary>
        protected double globalError;

        /// <summary>
        /// Keep track of how many times each neuron won.
        /// </summary>
        protected int[] won;

        /// <summary>
        /// The training sets.
        /// </summary>
        protected double[][] train;

        /// <summary>
        /// How many output neurons.
        /// </summary>
        private int outputNeuronCount;

        /// <summary>
        /// How many input neurons.
        /// </summary>
        private int inputNeuronCount;

        /// <summary>
        /// The best network found so far.
        /// </summary>
        private SelfOrganizingMap bestnet;


        /// <summary>
        /// The best error found so far.
        /// </summary>
        private double bestError;

        /// <summary>
        /// The work matrix, used to calculate corrections.
        /// </summary>
        private Matrix.Matrix work;

        /// <summary>
        /// The correction matrix, will be applied to the weight matrix after each
        /// training iteration.
        /// </summary>
        private Matrix.Matrix correc;


        /// <summary>
        /// Construct the trainer for a self organizing map.
        /// </summary>
        /// <param name="som">The self organizing map.</param>
        /// <param name="train">The training method.</param>
        /// <param name="learnMethod">The learning method.</param>
        /// <param name="learnRate">The learning rate.</param>
        public TrainSelfOrganizingMap(SelfOrganizingMap som,
                 double[][] train, LearningMethod learnMethod, double learnRate)
        {
            this.som = som;
            this.train = train;
            this.totalError = 1.0;
            this.learnMethod = learnMethod;
            this.learnRate = learnRate;

            this.outputNeuronCount = som.OutputNeuronCount;
            this.inputNeuronCount = som.InputNeuronCount;

            this.totalError = 1.0;

            for (int tset = 0; tset < train.Length; tset++)
            {
                Matrix.Matrix dptr = Matrix.Matrix.CreateColumnMatrix(train[tset]);
                if (MatrixMath.vectorLength(dptr) < VERYSMALL)
                {
                    throw (new System.Exception(
                            "Multiplicative normalization has null training case"));
                }

            }

            this.bestnet = new SelfOrganizingMap(this.inputNeuronCount,
                    this.outputNeuronCount, this.som.NormalizationType);

            this.won = new int[this.outputNeuronCount];
            this.correc = new Matrix.Matrix(this.outputNeuronCount,
                    this.inputNeuronCount + 1);
            if (this.learnMethod == LearningMethod.ADDITIVE)
            {
                this.work = new Matrix.Matrix(1, this.inputNeuronCount + 1);
            }
            else
            {
                this.work = null;
            }

            Initialize();
            this.bestError = Double.MaxValue;
        }

        /// <summary>
        /// Adjust the weights and allow the network to learn.
        /// </summary>
        protected void AdjustWeights()
        {
            for (int i = 0; i < this.outputNeuronCount; i++)
            {

                if (this.won[i] == 0)
                {
                    continue;
                }

                double f = 1.0 / this.won[i];
                if (this.learnMethod == LearningMethod.SUBTRACTIVE)
                {
                    f *= this.learnRate;
                }

                double length = 0.0;

                for (int j = 0; j <= this.inputNeuronCount; j++)
                {
                    double corr = f * this.correc[i, j];
                    this.som.OutputWeights.Add(i, j, corr);
                    length += corr * corr;
                }
            }
        }

        /// <summary>
        /// Copy the weights from one matrix to another.
        /// </summary>
        /// <param name="source">The source SOM.</param>
        /// <param name="target">The target SOM.</param>
        private void CopyWeights(SelfOrganizingMap source,
                 SelfOrganizingMap target)
        {

            MatrixMath.Copy(source.OutputWeights, target
                    .OutputWeights);
        }


        /// <summary>
        /// Evaludate the current error level of the network.
        /// </summary>
        public void EvaluateErrors()
        {

            this.correc.Clear();

            for (int i = 0; i < this.won.Length; i++)
            {
                this.won[i] = 0;
            }

            this.globalError = 0.0;
            // loop through all training sets to determine correction
            for (int tset = 0; tset < this.train.Length; tset++)
            {
                NormalizeInput input = new NormalizeInput(this.train[tset],
                       this.som.NormalizationType);
                int best = this.som.Winner(input);

                this.won[best]++;
                Matrix.Matrix wptr = this.som.OutputWeights.GetRow(best);

                double length = 0.0;
                double diff;

                for (int i = 0; i < this.inputNeuronCount; i++)
                {
                    diff = this.train[tset][i] * input.Normfac
                            - wptr[0, i];
                    length += diff * diff;
                    if (this.learnMethod == LearningMethod.SUBTRACTIVE)
                    {
                        this.correc.Add(best, i, diff);
                    }
                    else
                    {
                        this.work[0, i] = this.learnRate * this.train[tset][i]
                                * input.Normfac + wptr[0, i];
                    }
                }
                diff = input.Synth - wptr[0, this.inputNeuronCount];
                length += diff * diff;
                if (this.learnMethod == LearningMethod.SUBTRACTIVE)
                {
                    this.correc.Add(best, this.inputNeuronCount, diff);
                }
                else
                {
                    this.work[0, this.inputNeuronCount] =  this.learnRate
                                    * input.Synth
                                    + wptr[0, this.inputNeuronCount];
                }

                if (length > this.globalError)
                {
                    this.globalError = length;
                }

                if (this.learnMethod == LearningMethod.ADDITIVE)
                {
                    NormalizeWeight(this.work, 0);
                    for (int i = 0; i <= this.inputNeuronCount; i++)
                    {
                        this.correc.Add(best, i, this.work[0, i]
                                - wptr[0, i]);
                    }
                }

            }

            this.globalError = Math.Sqrt(this.globalError);
        }

        /// <summary>
        /// Force a win, if no neuron won.
        /// </summary>
        protected void ForceWin()
        {
            int best, which = 0;

            Matrix.Matrix outputWeights = this.som.OutputWeights;

            // Loop over all training sets.  Find the training set with
            // the least output.
            double dist = Double.MaxValue;
            for (int tset = 0; tset < this.train.Length; tset++)
            {
                best = this.som.Winner(this.train[tset]);
                double[] output = this.som.Output;

                if (output[best] < dist)
                {
                    dist = output[best];
                    which = tset;
                }
            }

            NormalizeInput input = new NormalizeInput(this.train[which],
                   this.som.NormalizationType);
            best = this.som.Winner(input);
            double[] output2 = this.som.Output;

            dist = Double.MinValue;
            int i = this.outputNeuronCount;
            while ((i--) > 0)
            {
                if (this.won[i] != 0)
                {
                    continue;
                }
                if (output2[i] > dist)
                {
                    dist = output2[i];
                    which = i;
                }
            }

            for (int j = 0; j < input.InputMatrix.Cols; j++)
            {
                outputWeights[which, j] =  input.InputMatrix[0, j];
            }

            NormalizeWeight(outputWeights, which);
        }


        /// <summary>
        /// Called to initialize the SOM.
        /// </summary>
        public void Initialize()
        {

            this.som.OutputWeights.Ramdomize(-1, 1);

            for (int i = 0; i < this.outputNeuronCount; i++)
            {
                NormalizeWeight(this.som.OutputWeights, i);
            }
        }

        /// <summary>
        /// This method is called for each training iteration. Usually this method is
        /// called from inside a loop until the error level is acceptable.
        /// </summary>
        public void Iteration()
        {

            EvaluateErrors();

            this.totalError = this.globalError;

            if (this.totalError < this.bestError)
            {
                this.bestError = this.totalError;
                CopyWeights(this.som, this.bestnet);
            }

            int winners = 0;
            for (int i = 0; i < this.won.Length; i++)
            {
                if (this.won[i] != 0)
                {
                    winners++;
                }
            }

            if ((winners < this.outputNeuronCount) && (winners < this.train.Length))
            {
                ForceWin();
                return;
            }

            AdjustWeights();

            if (this.learnRate > 0.01)
            {
                this.learnRate *= this.reduction;
            }
        }

        /// <summary>
        /// Normalize the specified row in the weight matrix.
        /// </summary>
        /// <param name="matrix">The weight matrix.</param>
        /// <param name="row">The row to normalize.</param>
        protected void NormalizeWeight(Matrix.Matrix matrix, int row)
        {

            double len = MatrixMath.vectorLength(matrix.GetRow(row));
            len = Math.Max(len, VERYSMALL);

            len = 1.0 / len;
            for (int i = 0; i < this.inputNeuronCount; i++)
            {
                matrix[row, i] =  matrix[row, i] * len;
            }
            matrix[row, this.inputNeuronCount] =  0;
        }
    }
}


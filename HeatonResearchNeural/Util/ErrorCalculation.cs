using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HeatonResearchNeural.Util
{
    public class ErrorCalculation
    {
        /// <summary>
        /// The current error level.
        /// </summary>
        private double globalError;

        /// <summary>
        /// The size of a training set.
        /// </summary>
        private int setSize;

        /// <summary>
        /// Returns the root mean square error for a complete training set.
        /// </summary>
        /// <returns>The current error for the neural network.</returns>
        public double CalculateRMS()
        {
            double err = Math.Sqrt(this.globalError / (this.setSize));
            return err;

        }

        /// <summary>
        /// Reset the error accumulation to zero.
        /// </summary>
        public void Reset()
        {
            this.globalError = 0;
            this.setSize = 0;
        }

        /// <summary>
        /// Called to update for each number that should be checked.
        /// </summary>
        /// <param name="actual">The actual number.</param>
        /// <param name="ideal">The ideal number.</param>
        public void UpdateError(double[] actual, double[] ideal)
        {
            for (int i = 0; i < actual.Length; i++)
            {
                double delta = ideal[i] - actual[i];
                this.globalError += delta * delta;
                this.setSize += ideal.Length;
            }
        }
    }
}

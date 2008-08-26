using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HeatonResearchNeural.Activation
{
    /// <summary>
    /// ActivationSigmoid: The sigmoid activation function takes on a
    /// sigmoidal shape.  Only positive numbers are generated.  Do not
    /// use this activation function if negative number output is desired.
    /// </summary>
    [Serializable]
    public class ActivationSigmoid : ActivationFunction
    {
        /// <summary>
        /// A activation function for a neural network.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        public double ActivationFunction(double d)
        {
            return 1.0 / (1 + Math.Exp(-1.0 * d));
        }

        /// <summary>
        /// Performs the derivative function of the activation function function on the input.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        public double DerivativeFunction(double d)
        {
            return d * (1.0 - d);
        }
    }
}

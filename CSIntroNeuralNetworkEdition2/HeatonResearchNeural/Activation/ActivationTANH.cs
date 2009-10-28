using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using HeatonResearchNeural.Util;

namespace HeatonResearchNeural.Activation
{

    /// <summary>
    /// ActivationTANH: The hyperbolic tangent activation function takes the
    /// curved shape of the hyperbolic tangent.  This activation function produces
    /// both positive and negative output.  Use this activation function if 
    /// both negative and positive output is desired.
    /// </summary>
    [Serializable]
    public class ActivationTANH : ActivationFunction
    {

        /// <summary>
        /// A activation function for a neural network.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        public double ActivationFunction(double d)
        {
            double result = (BoundNumbers.Exp(d * 2.0) - 1.0) / (BoundNumbers.Exp(d * 2.0) + 1.0);
            return result;
        }

        /// <summary>
        /// Performs the derivative function of the activation function function on the input.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        public double DerivativeFunction(double d)
        {
            return (1.0 - Math.Pow(ActivationFunction(d), 2.0));
        }

    }

}

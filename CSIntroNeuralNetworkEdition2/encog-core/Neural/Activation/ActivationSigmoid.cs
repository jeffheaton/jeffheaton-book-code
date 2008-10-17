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

namespace Encog.Neural.Activation
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

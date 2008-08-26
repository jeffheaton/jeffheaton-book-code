using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HeatonResearchNeural.Exception
{
    /// <summary>
    /// Thrown when a matrix error occured.
    /// </summary>
    class MatrixError: System.Exception
    {
        /// <summary>
        /// Constructor for a simple message exception.
        /// </summary>
        /// <param name="str">The message for the exception.</param>
        public MatrixError(String str): base(str)
        {
        }
    }
}

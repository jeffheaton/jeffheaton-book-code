// The Heaton Research Spider for .Net 
// Copyright 2007 by Heaton Research, Inc.
// 
// From the book:
// 
// HTTP Recipes for C# Bots, ISBN: 0-9773206-7-7
// http://www.heatonresearch.com/articles/series/20/
// 
// This class is released under the:
// GNU Lesser General Public License (LGPL)
// http://www.gnu.org/copyleft/lesser.html
//
using System;
using System.Collections.Generic;
using System.Text;

namespace HeatonResearch.Spider.Workload
{
    /// <summary>
    /// WorkloadException: This exception is thrown when the
    /// workload manager encounters an error.
    /// </summary>
    public class WorkloadException:Exception
    {
        /// <summary>
        /// Throw a workload exception.
        /// </summary>
        /// <param name="msg">The exception message.</param>
        public WorkloadException(String msg)
            : base(msg)
        {
        }

        /// <summary>
        /// Pass on an exception as a WorkloadException.
        /// </summary>
        /// <param name="e">The other exception.</param>
        public WorkloadException(Exception e)
            : base("Exception while processing workload", e)
        {
        }
    }
}

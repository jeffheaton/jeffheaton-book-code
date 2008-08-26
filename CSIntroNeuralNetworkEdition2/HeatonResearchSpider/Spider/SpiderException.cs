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

namespace HeatonResearch.Spider
{
    /// <summary>
    /// SpiderException: This exception is thrown when the spider
    /// encounters an error.
    /// </summary>
    public class SpiderException:Exception
    {
        /// <summary>
        /// Throw a spider exception.
        /// </summary>
        /// <param name="msg">The exception message.</param>
        public SpiderException(String msg)
            : base(msg)
        {
        }

        /// <summary>
        /// Pass on an exception as a SpiderException.
        /// </summary>
        /// <param name="e">The other exception.</param>
        public SpiderException(Exception e)
            : base("Exception while spidering", e)
        {
        }
    }
}

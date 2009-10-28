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

namespace HeatonResearch.Spider.Workload.SQL
{
    /// <summary>
    /// Status: This class defines the constant status values for
    /// both the spider_host and spider_workload tables.
    /// </summary>
    class Status
    {
        /// <summary>
        /// The item is waiting to be processed.
        /// </summary>
        public const String STATUS_WAITING = "W";

        /// <summary>
        /// The item was processed, but resulted in an error.
        /// </summary>
        public const String STATUS_ERROR = "E";

        /// <summary>
        /// The item was processed successfully.
        /// </summary>
        public const String STATUS_DONE = "D";

        /// <summary>
        /// The item is currently being processed.
        /// </summary>
        public const String STATUS_PROCESSING = "P";

        /// <summary>
        /// This item should be ignored, only applies to hosts.
        /// </summary>
        public const String STATUS_IGNORE = "I";
    }
}

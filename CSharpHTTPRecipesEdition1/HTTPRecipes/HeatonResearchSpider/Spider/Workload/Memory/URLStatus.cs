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
using System.Net;

namespace HeatonResearch.Spider.Workload.Memory
{
    /// <summary>
    /// URLStatus: This class holds in memory status information
    /// for URLs. Specifically it holds their processing status,
    /// depth and source URL.
    /// </summary>
    public class URLStatus
    {
        /// <summary>
        /// The current status of this URL.
        /// </summary>
        public Status CurrentStatus
        {
            get
            {
                return status;
            }
            set
            {
                status = value;
            }
        }

        /// <summary>
        /// The page that this URL was found on.
        /// </summary>
        public Uri Source
        {
            get
            {
                return source;
            }
            set
            {
                source = value;
            }
        }

        /// <summary>
        /// The depth of this URL from the starting URL.
        /// </summary>
        public int Depth
        {
            get
            {
                return depth;
            }
            set
            {
                depth = value;
            }
        }

        /// <summary>
        /// The values for URL statues.
        /// </summary>
        public enum Status
        {
            /// <summary>
            /// Waiting to be processed.
            /// </summary>
            WAITING, 
            /// <summary>
            /// Successfully processed.
            /// </summary>
            PROCESSED,
            /// <summary>
            /// Unsuccessfully processed.
            /// </summary>
            ERROR,
            /// <summary>
            /// Currently being processed.
            /// </summary>
            WORKING
        };


        private Status status;
        private int depth;
        private Uri source;
    }
}

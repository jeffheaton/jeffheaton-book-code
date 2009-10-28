// The Heaton Research Spider for .Net Copyright 2007 by Heaton
// Research, Inc.
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

namespace HeatonResearch.Spider.Filter
{
    /// <summary>
    /// SpiderFilter: Filters will cause the spider to skip
    /// URL's.
    /// </summary>
    public interface SpiderFilter
    {
        /// <summary>
        /// Check to see if the specified URL is to be excluded.
        /// </summary>
        /// <param name="url">The URL to be checked.</param>
        /// <returns>Returns true if the URL should be excluded.</returns>
        bool IsExcluded(Uri url);

        /// <summary>
        /// Called when a new host is to be processed. SpiderFilter
        /// classes can not be shared among hosts.
        /// </summary>
        /// <param name="host">The new host.</param>
        /// <param name="userAgent">The user agent being used by the spider. Leave
        /// null for default.</param>
        void NewHost(String host, String userAgent);
    }
}

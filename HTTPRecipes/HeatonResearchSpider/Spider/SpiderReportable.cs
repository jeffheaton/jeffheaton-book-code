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
using System.IO;

namespace HeatonResearch.Spider
{
    /// <summary>
    /// The SpiderReportable interface defines how the spider reports
    /// its findings to an outside class.
    /// </summary>
    public interface SpiderReportable
    {
        /// <summary>
        /// This function is called when the spider is ready to
        /// process a new host.
        /// </summary>
        /// <param name="host">The new host that is about to be processed.</param>
        /// <returns>True if this host should be processed, false otherwise.</returns>
        bool BeginHost(String host);

        /// <summary>
        /// Called when the spider is starting up. This method
        /// provides the SpiderReportable class with the spider
        /// object.
        /// </summary>
        /// <param name="spider">The spider that will be working with this object.</param>
        void Init(Spider spider);

        /// <summary>
        /// Called when the spider encounters a URL.
        /// </summary>
        /// <param name="url">The URL that the spider found.</param>
        /// <param name="source">The page that the URL was found on.</param>
        /// <param name="type">The type of link this URL is.</param>
        /// <returns>True if the spider should scan for links on this page.</returns>
        bool SpiderFoundURL(Uri url, Uri source, Spider.URLType type);

        /// <summary>
        /// Called when the spider is about to process a NON-HTML
        /// URL.
        /// </summary>
        /// <param name="url">The URL that the spider found.</param>
        /// <param name="stream">An InputStream to read the page contents from.</param>
        void SpiderProcessURL(Uri url, Stream stream);

        /// <summary>
        /// Called when the spider is ready to process an HTML
        /// URL.
        /// </summary>
        /// <param name="url">The URL that the spider is about to process.</param>
        /// <param name="parse">An object that will allow you you to parse the
        /// HTML on this page.</param>
        void SpiderProcessURL(Uri url, SpiderParseHTML parse);

        /// <summary>
        /// Called when the spider tries to process a URL but gets
        /// an error.
        /// </summary>
        /// <param name="url">The URL that generated an error.</param>
        void SpiderURLError(Uri url);



    }
}

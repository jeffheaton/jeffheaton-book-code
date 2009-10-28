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
    /// WorkloadManager: This interface defines a workload
    /// manager. A workload manager handles the lists of URLs
    /// that have been processed, resulted in an error, and 
    /// are waiting to be processed.
    /// </summary>
    public interface WorkloadManager
    {
        /// <summary>
        /// Add the specified URL to the workload.
        /// </summary>
        /// <param name="url">The URL to be added.</param>
        /// <param name="source">The page that contains this URL.</param>
        /// <param name="depth">The depth of this URL.</param>
        /// <returns>True if the URL was added, false otherwise.</returns>
        bool Add(Uri url, Uri source, int depth);

        /// <summary>
        /// Clear the workload.
        /// </summary>
        void Clear();

        /// <summary>
        /// Determine if the workload contains the specified URL.
        /// </summary>
        /// <param name="url">The URL to check.</param>
        /// <returns>True if the URL is contained in the workload.</returns>
        bool Contains(Uri url);

        /// <summary>
        /// Convert the specified String to a URL. If the string is too long or has other issues, throw a WorkloadException.
        /// </summary>
        /// <param name="url">A String to convert into a URL.</param>
        /// <returns>The URL.</returns>
        Uri ConvertURL(String url);

        /// <summary>
        /// Get the current host.
        /// </summary>
        /// <returns>The current host.</returns>
        String GetCurrentHost();

        /// <summary>
        /// Get the depth of the specified URL.
        /// </summary>
        /// <param name="url">The URL to get the depth of.</param>
        /// <returns>The depth of the specified URL.</returns>
        int GetDepth(Uri url);

        /// <summary>
        /// Get the source page that contains the specified URL.
        /// </summary>
        /// <param name="url">The URL to seek the source for.</param>
        /// <returns>The source of the specified URL</returns>
        Uri GetSource(Uri url);

        /// <summary>
        /// * Get a new URL to work on. Wait if there are no URL's currently available. Return null if done with the current host. The URL being returned will be marked as in progress.
        /// </summary>
        /// <returns>The next URL to work on.</returns>
        Uri GetWork();

        /// <summary>
        /// Setup this workload manager for the specified spider.
        /// </summary>
        /// <param name="spider">The spider using this workload manager.</param>
        void Init(Spider spider);

        /// <summary>
        /// Mark the specified URL as error.
        /// </summary>
        /// <param name="url">The URL that had an error.</param>
        void MarkError(Uri url);

        /// <summary>
        /// Mark the specified URL as successfully processed.
        /// </summary>
        /// <param name="url">The URL to mark as processed.</param>
        void MarkProcessed(Uri url);

        /// <summary>
        /// Move on to process the next host. This should only be called after getWork returns null.
        /// </summary>
        /// <returns>The name of the next host.</returns>
        String NextHost();

        /// <summary>
        /// Setup the workload so that it can be resumed from where the last spider left the workload.
        /// </summary>
        void Resume();

        /// <summary>
        /// If there is currently no work available, then wait until a new URL has been added to the workload.
        /// </summary>
        /// <param name="time">The amount of time to wait.</param>
        void WaitForWork(int time);

        /// <summary>
        /// Return true if there are no more workload units.
        /// </summary>
        /// <returns></returns>
        bool WorkloadEmpty();


    }
}

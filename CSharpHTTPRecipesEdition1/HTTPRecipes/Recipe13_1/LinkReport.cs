using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using HeatonResearch.Spider;
using HeatonResearch.Spider.Workload;
using HeatonResearch.Spider.Logging;

namespace Recipe13_1
{
    /// <summary>
    /// Recipe #13.1: Scanning for broken links 
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// The report class for the Link Checker.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class LinkReport : SpiderReportable
    {
        /// <summary>
        /// The host we are working with.
        /// </summary>
        private String baseHost;

        /// <summary>
        /// The Spider object that this object reports to.
        /// </summary>
        private Spider spider;

        /// <summary>
        /// The bad URL's.
        /// </summary>
        private List<String> bad = new List<String>();

        /// <summary>
        /// This function is called when the spider is ready to
        /// process a new host. This function simply stores the
        /// value of the current host.
        /// </summary>
        /// <param name="host"></param>
        /// <returns></returns>
        public bool BeginHost(String host)
        {
            if (this.baseHost == null)
            {
                this.baseHost = host;
                return true;
            }
            else
            {
                return false;
            }
        }

        /// <summary>
        /// The bad link's found.
        /// </summary>
        public List<String> Bad
        {
            get
            {
                return this.bad;
            }
            set
            {
                bad = value;
            }
        }

        /// <summary>
        /// Called when the spider is starting up. This method
        /// provides the SpiderReportable class with the spider
        /// object.
        /// </summary>
        /// <param name="spider">The spider that will be working with this object.</param>
        public void Init(Spider spider)
        {
            this.spider = spider;
        }


        /// <summary>
        /// Called when the spider finds a URL.
        /// </summary>
        /// <param name="url">The URL that was found.</param>
        /// <param name="source">Where the URL was found.</param>
        /// <param name="type">What sort of tag produced this URL.</param>
        /// <returns></returns>
        public bool SpiderFoundURL(Uri url, Uri source,
            Spider.URLType type)
        {
            if ((this.baseHost != null) && (String.Compare(this.baseHost, url.Host, true) != 0))
            {
                return false;
            }

            return true;
        }

        /// <summary>
        /// Not used by the link checker.
        /// </summary>
        /// <param name="url">Not used.</param>
        /// <param name="stream">Not used.</param>
        public void SpiderProcessURL(Uri url, Stream stream)
        {
        }

        /// <summary>
        /// Called when the spider is ready to process an HTML
        /// URL.
        /// </summary>
        /// <param name="url">The URL that the spider is about to process.</param>
        /// <param name="parse">An object that will allow you you to parse the HTML on this page.</param>
        public void SpiderProcessURL(Uri url, SpiderParseHTML parse)
        {
            try
            {
                parse.ReadAll();
            }
            catch (IOException)
            {
                spider.Logging.Log(Logger.Level.INFO, "Error reading page:" + url.ToString());
            }
        }

        /// <summary>
        /// Called when the spider tries to process a URL but gets
        /// an error.
        /// </summary>
        /// <param name="url">The URL that generated an error.</param>
        public void SpiderURLError(Uri url)
        {
            Uri source;
            try
            {
                source = this.spider.Workload.GetSource(url);
                StringBuilder str = new StringBuilder();
                str.Append("Bad URL:");
                str.Append(url.ToString());
                str.Append(" found at ");
                str.Append(source.ToString());
                this.bad.Add(str.ToString());
            }
            catch (WorkloadException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}

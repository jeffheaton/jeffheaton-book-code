using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;
using HeatonResearch.Spider;
using HeatonResearch.Spider.HTML;

namespace Recipe13_2
{
    /// <summary>
    /// Recipe #13.2: Download Site 
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// The report class for the Site Downloader.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class SpiderReport : SpiderReportable
    {
        /// <summary>
        /// The base host. Only URL's from this host will be downloaded. 
        /// </summary>
        private String baseHost;

        /// <summary>
        /// The local path to save downloaded files to.
        /// </summary>
        private String path;

        /// <summary>
        /// Construct a SpiderReport object.
        /// </summary>
        /// <param name="path">The local file path to store the files to.</param>
        public SpiderReport(String path)
        {
            this.path = path;
        }

        /// <summary>
        /// This function is called when the spider is ready to
        /// process a new host. This function simply stores the
        /// value of the current host.
        /// </summary>
        /// <param name="host">The new host that is about to be processed.</param>
        /// <returns>True if this host should be processed, false otherwise.</returns>
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
        /// Not used.
        /// </summary>
        /// <param name="spider">Not used.</param>
        public void Init(Spider spider)
        {
        }

        /// <summary>
        /// Called when the spider encounters a URL. If the URL is
        /// on the same host as the base host, then the function
        /// will return true, indicating that the URL is to be
        /// processed.
        /// </summary>
        /// <param name="url">The URL that the spider found.</param>
        /// <param name="source">The page that the URL was found on.</param>
        /// <param name="type">The URL type.</param>
        /// <returns>True if the spider should scan for links on this page.</returns>
        public bool SpiderFoundURL(Uri url, Uri source,
            Spider.URLType type)
        {

            if ((this.baseHost != null) && (string.Compare(this.baseHost, url.Host, true) != 0))
            {
                return false;
            }

            return true;
        }

        /// <summary>
        /// Called when the spider is about to process a NON-HTML
        /// URL.
        /// </summary>
        /// <param name="url">The URL that the spider found.</param>
        /// <param name="stream">An InputStream to read the page contents from.</param>
        public void SpiderProcessURL(Uri url, Stream stream)
        {
            byte[] buffer = new byte[1024];

            int length;
            String filename = URLUtility.convertFilename(this.path, url, true);

            Stream os = new FileStream(filename, FileMode.Create);
            do
            {
                length = stream.Read(buffer, 0, buffer.Length);
                if (length >0)
                {
                    os.Write(buffer, 0, length);
                }
            } while (length >0 );
            os.Close();
        }

        /// <summary>
        /// Called when the spider is ready to process an HTML
        /// URL. Download the contents of the URL to a local file.
        /// </summary>
        /// <param name="url">The URL that the spider is about to process.</param>
        /// <param name="parse">An object that will allow you you to parse the HTML on this page.</param>
        public void SpiderProcessURL(Uri url, SpiderParseHTML parse)
        {
            String filename = URLUtility.convertFilename(this.path, url, true);

            Stream os = new FileStream(filename, FileMode.Create);
            parse.Stream.OutputStream = os;
            parse.ReadAll();
            os.Close();

        }

        /// <summary>
        /// Not used.
        /// </summary>
        /// <param name="url">Not used.</param>
        public void SpiderURLError(Uri url)
        {
            
        }
    }
}

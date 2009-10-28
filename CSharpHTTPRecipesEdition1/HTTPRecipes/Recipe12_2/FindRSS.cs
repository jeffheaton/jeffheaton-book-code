using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;
using HeatonResearch.Spider.RSS;
using HeatonResearch.Spider.HTML;

namespace Recipe12_2
{
    /// <summary>
    /// Recipe #12.2: Find an RSS Feed
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to provide a base URL and find an
    /// RSS feed for that site, if one exists.  This recipe
    /// works by looking for the link tag that specifies the
    /// location of the RSS feed.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class FindRSS
    {

        /// <summary>
        /// Display an RSS feed.
        /// </summary>
        /// <param name="url">The URL of the RSS feed.</param>
        public void ProcessRSS(Uri url)
        {
            RSS rss = new RSS();
            rss.Load(url);
            Console.WriteLine(rss.ToString());
        }

        /// <summary>
        /// This method looks for a link tag at the specified URL.  If a link
        /// tag is found that specifies an RSS feed, then that feed is 
        /// displayed.
        /// </summary>
        /// <param name="url">The URL of the web site.</param>
        public void Process(Uri url)
        {
            String href = null;
            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 30000;
            WebResponse response = http.GetResponse();
            Stream stream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(stream);

            int ch;
            do
            {
                ch = parse.Read();
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "link", true) == 0)
                    {
                        String type = tag["type"];
                        if (type != null && type.IndexOf("rss") != -1)
                        {
                            href = tag["href"];
                        }
                    }
                }
            } while (ch != -1);

            if (href == null)
            {
                Console.WriteLine("No RSS link found.");
            }
            else
                ProcessRSS(new Uri(href));
        }

        static void Main(string[] args)
        {

            Uri url;

            if (args.Length != 0)
                url = new Uri(args[0]);
            else
                url = new Uri("http://www.httprecipes.com/");

            FindRSS load = new FindRSS();
            load.Process(url);
        }
    }
}

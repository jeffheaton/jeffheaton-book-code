using System;
using System.Collections.Generic;
using System.Text;
using HeatonResearch.Spider.RSS;

namespace Recipe12_1
{
    /// <summary>
    /// Recipe #12.1: Display an RSS Feed
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to use the RSS class to load a
    /// RSS feed.  The RSS feed is then displayed.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class LoadRSS
    {        
        /// <summary>
        /// Display an RSS feed.
        /// </summary>
        /// <param name="url">The URL of the RSS feed.</param>
        public void Process(Uri url)
        {
            RSS rss = new RSS();
            rss.Load(url);
            Console.WriteLine(rss.ToString());
        }

        static void Main(string[] args)
        {
            Uri url;

            if (args.Length != 0)
                url = new Uri(args[0]);
            else
                url = new Uri("http://www.httprecipes.com/1/12/rss1.xml");

            LoadRSS load = new LoadRSS();
            load.Process(url);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Text;
using HeatonResearch.Spider;

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
    /// The recipe downloads the contents of a web site. The
    /// spider starts with a single URL and downloads everything
    /// it can find that is on the same host.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class DownloadSite
    {
        /// <summary>
        /// Download an entire site.
        /// </summary>
        /// <param name="config">The spider configuration file to use.</param>
        /// <param name="baseURL">The URL to start from.></param>
        /// <param name="local">The local path to save files to.</param>
        public void Download(String config, Uri baseURL, String local)
        {
            SpiderReport report = new SpiderReport(local);
            SpiderOptions options = new SpiderOptions();
            options.Load(config);
            Spider spider = new Spider(options, report);
            spider.Logging.Console = true;
            spider.Logging.Filename = "c:\\spider.log";
            spider.Logging.Clear();

            spider.AddURL(baseURL, null, 1);
            spider.Process();
            Console.WriteLine(spider.Status);
        }


        static void Main(string[] args)
        {
            if (args.Length < 3)
            {
                Console.WriteLine(
                "Usage: Recipe13_2 [Path to spider.conf] [Path to download to] [URL to download]");
            }
            else
            {
                try
                {
                    DownloadSite download = new DownloadSite();
                    download.Download(args[0], new Uri(args[2]), args[1]);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);

                }


            }

        }
    }
}

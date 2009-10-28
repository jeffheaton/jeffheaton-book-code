using System;
using System.Collections.Generic;
using System.Text;
using HeatonResearch.Spider;

namespace Recipe13_3
{
    /// <summary>
    /// Recipe #13.3: World Spider 
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// The recipe downloads the contents of a web site. The
    /// spider starts with a single URL and downloads everything
    /// it will visit host after host and never likely finish.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class WorldSpider
    {
        /// <summary>
        /// Download an entire site.
        /// </summary>
        /// <param name="config">The spider configuration file to use.</param>
        /// <param name="baseHost">The URL to start from.</param>
        /// <param name="local">The local path to save files to.</param>
        public void Download(String config, Uri baseHost, String local)
        {
            WorldSpiderReport report = new WorldSpiderReport(local);
            SpiderOptions options = new SpiderOptions();
            options.Load(config);
            Spider spider = new Spider(options, report);
            spider.AddURL(baseHost, null, 1);
            spider.Process();
            Console.WriteLine(spider.Status);
        }

        static void Main(string[] args)
        {
            if (args.Length < 3)
            {
                Console.WriteLine(
                "Usage: Recipe13_3 [Path to spider.conf] [Path to download to] [URL to download]");
            }
            else
            {
                WorldSpider download = new WorldSpider();
                download.Download(args[0], new Uri(args[2]), args[1]);
            }
        }

    }
}

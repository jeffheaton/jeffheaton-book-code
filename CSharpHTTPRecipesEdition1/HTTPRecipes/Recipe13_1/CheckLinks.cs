using System;
using System.Collections.Generic;
using System.Text;
using HeatonResearch.Spider;
using HeatonResearch.Spider.Workload.Memory;

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
    /// This recipe will spider the given URL looking for bad
    /// links. Only URL's that have the same domain name are
    /// examined. There is no depth limitation on the spider, it
    /// will visit as much of the site as it can access.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class CheckLinks
    {
        /// <summary>
        /// This method is called by main to check a link. After
        /// spidering through the site, the final list of bad links
        /// is displayed.
        /// </summary>
        /// <param name="url">The URL to check for bad links.</param>
        public void check(Uri url)
        {
            SpiderOptions options = new SpiderOptions();
            options.WorkloadManager = typeof(MemoryWorkloadManager).FullName;
            LinkReport report = new LinkReport();
            Spider spider = new Spider(options, report);
            spider.AddURL(url, null, 1);

            spider.Process();
            Console.WriteLine(spider.Status);

            if (report.Bad.Count > 0)
            {
                Console.WriteLine("Bad Links Found:");
                foreach (String str in report.Bad)
                {
                    Console.WriteLine(str);
                }
            }
            else
            {
                Console.WriteLine("No bad links were found.");
            }

        }



        static void Main(string[] args)
        {

            if (args.Length != 1)
            {
                Console.WriteLine("Usage: Recipe13_1 [website to check]");
            }
            else
            {
                CheckLinks links = new CheckLinks();
                links.check(new Uri(args[0]));
            }

        }
    }
}

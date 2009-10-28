using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using System.Threading;

namespace Recipe4_4
{
    /// <summary>
    /// Recipe #4.4: Monitor a Site
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe will monitor the specified website to see
    /// if the site is still "up".  This recipe will scan the
    /// site once a minute.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class MonitorSite
    {
        /// <summary>
        /// Scan a URL every minute to make sure it is still up.
        /// </summary>
        /// <param name="url">The URL to monitor.</param>
        public void Monitor(Uri url)
        {
            while (true)
            {
                Console.WriteLine("Checking " + url + " at " + (new DateTime()));

                // Try to connect.
                try
                {
                    WebRequest http = HttpWebRequest.Create(url);
                    HttpWebResponse response = (HttpWebResponse)http.GetResponse();
                    Console.WriteLine("The site is up.");
                }
                catch (IOException)
                {
                    Console.WriteLine("The site is down!!!");
                }
                Thread.Sleep(60000);
            }
        }

        /// <summary>
        /// Download either a text or binary file from a URL.
        /// The URL's headers will be scanned to determine the
        /// type of tile.
        /// </summary>
        /// <param name="remoteURL">The URL to download from.</param>
        /// <param name="localFile">The local file to save to.</param>
        static void Main(string[] args)
        {
            if (args.Length != 1)
            {
                Console.WriteLine("Usage: Recipe4_4 [URL to Monitor]");
            }
            else
            {
                MonitorSite d = new MonitorSite();
                d.Monitor(new Uri(args[0]));
            }
        }

    }
}

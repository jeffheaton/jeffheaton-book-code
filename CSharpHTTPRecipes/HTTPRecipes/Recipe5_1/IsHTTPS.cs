using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;

namespace Recipe5_1
{
    /// <summary>
    /// Recipe #5.1: Is URL HTTPS?
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///     
    /// This recipe shows how to determine if a URL is using
    /// the HTTPS protocol.
    ///     
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    /// http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class Program
    {
        /// <summary>
        /// The main entry point for the program.
        /// </summary>
        /// <param name="args">Program arguments.</param>
        static void Main(string[] args)
        {
            String strURL = "";

            // obtain a URL to use
            if (args.Length < 1)
            {
                strURL = "https://www.httprecipes.com/1/5/";
            }
            else
            {
                strURL = args[0];
            }

            Uri url = new Uri(strURL);
            WebRequest http = HttpWebRequest.Create(url);
            http.PreAuthenticate = true;
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();

            Console.WriteLine(response.ToString());

            if (String.Compare(url.Scheme,"https",true)==0 )
            {
                Console.WriteLine("Successful HTTPS connection");
            }
            else
            {
                Console.WriteLine("Successful HTTP connection");
            }
        }
    }
}

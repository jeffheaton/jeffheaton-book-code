using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;


namespace Recipe10_1
{
    /// <summary>
    /// Recipe #10.1: Non-XML AJAX
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to access data from an AJAX website.
    /// The data that will be extracted is HTML.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class AjaxNonXML
    {
        /// <summary>
        /// This method downloads the specified URL into a C#
        /// String. This is a very simple method, that you can
        /// reused anytime you need to quickly grab all data from
        /// a specific URL.
        /// </summary>
        /// <param name="url">The URL to download.</param>
        /// <returns>The contents of the URL that was downloaded.</returns>
        public String DownloadPage(Uri url)
        {
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            StreamReader stream = new StreamReader(response.GetResponseStream(), System.Text.Encoding.ASCII);

            String result = stream.ReadToEnd();

            response.Close();
            stream.Close();
            return result;
        }

        /// <summary>
        /// This method is very useful for grabbing information from a
        /// HTML page.  It extracts text from between two tokens, the
        /// tokens need not be case sensitive.
        /// </summary>
        /// <param name="str">The string to extract from.</param>
        /// <param name="token1">The text, or tag, that comes before the desired text</param>
        /// <param name="token2">The text, or tag, that comes after the desired text</param>
        /// <param name="count">Which occurrence of token1 to use, 1 for the first</param>
        /// <returns></returns>
        public String ExtractNoCase(String str, String token1, String token2,
            int count)
        {
            int location1, location2;

            // convert everything to lower case
            String searchStr = str.ToLower();
            token1 = token1.ToLower();
            token2 = token2.ToLower();

            // now search
            location1 = location2 = 0;
            do
            {
                location1 = searchStr.IndexOf(token1, location1 + 1);

                if (location1 == -1)
                    return null;

                count--;
            } while (count > 0);

            // return the result from the original string that has mixed
            // case
            location1 += token1.Length;
            location2 = str.IndexOf(token2, location1 + 1);
            if (location2 == -1)
                return null;

            return str.Substring(location1, location2 - location1);
        }

        /// <summary>
        /// This method will download data from the specified state.
        /// This data will come in as a partial HTML document, 
        /// the necessary data will be extracted from there.
        /// </summary>
        /// <param name="state">The state you want to download (i.e. Missouri)</param>
        public void Process(String state)
        {
            Uri url = new Uri("http://www.httprecipes.com/1/10/statehtml.php?state=" + state);
            String buffer = DownloadPage(url);
            String name = this.ExtractNoCase(buffer, "<h1>", "</h1>", 0);
            String capital = this.ExtractNoCase(buffer, "Capital:<b></td><td>", "</td>", 0);
            String code = this.ExtractNoCase(buffer, "Code:<b></td><td>", "</td>", 0);
            String site = this.ExtractNoCase(buffer, "Official Site:<b></td><td><a href=\"", "\"", 0);

            Console.WriteLine("State name:" + name);
            Console.WriteLine("State capital:" + capital);
            Console.WriteLine("Code:" + code);
            Console.WriteLine("Site:" + site);
        }


        static void Main(string[] args)
        {
            if (args.Length != 1)
            {
                Console.WriteLine("Usage: Recipe10_1 [state, i.e. Missouri]");
            }
            else
            {
                AjaxNonXML d = new AjaxNonXML();
                d.Process(args[0]);
            }
        }
    }
}

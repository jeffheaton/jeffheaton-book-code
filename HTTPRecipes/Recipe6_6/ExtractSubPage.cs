using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe6_6
{
    /// <summary>
    /// Recipe #6.6: Extract Data from Subpages
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to parse a parent page, then visit
    /// each child page looking for data.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class ExtractSubPage
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
        /// Process each subpage. The subpages are where the data actually is.
        /// </summary>
        /// <param name="u">The URL of the subpage.</param>
        private void ProcessSubPage(Uri u)
        {
            String str = DownloadPage(u);
            String code = ExtractNoCase(str, "Code:<b></td><td>", "</td>", 0);
            if (code != null)
            {
                String capital = ExtractNoCase(str, "Capital:<b></td><td>", "</td>", 0);
                String name = ExtractNoCase(str, "<h1>", "</h1>", 0);
                String flag = ExtractNoCase(str, "<img src=\"", "\" border=\"1\">", 2);
                String site = ExtractNoCase(str, "Official Site:<b></td><td><a href=\"",
                    "\"", 0);

                Uri flagURL = new Uri(u, flag);

                StringBuilder buffer = new StringBuilder();
                buffer.Append("\"");
                buffer.Append(code);
                buffer.Append("\",\"");
                buffer.Append(name);
                buffer.Append("\",\"");
                buffer.Append(capital);
                buffer.Append("\",\"");
                buffer.Append(flagURL.ToString());
                buffer.Append("\",\"");
                buffer.Append(site);
                buffer.Append("\"");
                Console.WriteLine(buffer.ToString());
            }
        }

        /// <summary>
        /// Process the specified URL and extract data from all of the subpages
        /// that this page links to.
        /// </summary>
        /// <param name="url">The URL to process.</param>
        public void Process(Uri url)
        {
            String value = "";
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);

            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "a", true) == 0)
                    {
                        value = tag["href"];
                        Uri u = new Uri(url, value.ToString());
                        value = u.ToString();
                        ProcessSubPage(u);
                    }
                }
            }
        }

        static void Main(string[] args)
        {
            Uri u = new Uri("http://www.httprecipes.com/1/6/subpage.php");
            ExtractSubPage parse = new ExtractSubPage();
            parse.Process(u);
        }
    }
}

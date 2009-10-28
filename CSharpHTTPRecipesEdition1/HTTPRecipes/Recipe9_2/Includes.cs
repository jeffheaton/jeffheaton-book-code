using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe9_2
{
    /// <summary>
    /// Recipe #9.2: JavaScript Includes
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to access JavaScript includes.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class Includes
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
        /// Called to download the text from a page.  If any JavaScript
        /// include is found, the text from that page is read too.
        /// </summary>
        public void Process()
        {
            Uri url = new Uri("http://www.httprecipes.com/1/9/includes.php");
            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 30000;
            WebResponse response = http.GetResponse();
            Stream stream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(stream);
            StringBuilder buffer = new StringBuilder();

            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "script", true) == 0 && tag["src"] != null)
                    {
                        String src = tag["src"];
                        Uri u = new Uri(url, src);
                        String include = DownloadPage(u);
                        buffer.Append("<script>");
                        buffer.Append(include);
                        buffer.Append("</script>");
                    }
                    else
                    {
                        buffer.Append(tag.ToString());
                    }
                }
                else
                {
                    buffer.Append((char)ch);
                }
            }

            Console.WriteLine(buffer.ToString());
        }

        static void Main(string[] args)
        {
            Includes parse = new Includes();
            parse.Process();
        }
    }
}

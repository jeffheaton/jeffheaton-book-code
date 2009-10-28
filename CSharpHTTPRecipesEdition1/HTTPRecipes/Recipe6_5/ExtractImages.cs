using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe6_5
{

    /// <summary>
    /// Recipe #6.5: Parse and Extract Images
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to parse and extract(download) images
    /// from an HTML page.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class ExtractImages
    {
        /// <summary>
        /// Download the specified text page.
        /// </summary>
        /// <param name="response">The HttpWebResponse to download from.</param>
        /// <param name="filename">The local file to save to.</param>
        public void DownloadBinaryFile(HttpWebResponse response, String filename)
        {
            byte[] buffer = new byte[4096];
            FileStream os = new FileStream(filename, FileMode.Create);
            Stream stream = response.GetResponseStream();

            int count = 0;
            do
            {
                count = stream.Read(buffer, 0, buffer.Length);
                if (count > 0)
                    os.Write(buffer, 0, count);
            } while (count > 0);

            response.Close();
            stream.Close();
            os.Close();
        }

        /// <summary>
        /// Extract just the filename from a URL.
        /// </summary>
        /// <param name="u">The URL to extract from.</param>
        /// <returns>The filename.</returns>
        private String ExtractFile(Uri u)
        {
            String str = u.PathAndQuery;

            // strip off path information
            int i = str.LastIndexOf('/');
            if (i != -1)
                str = str.Substring(i + 1);
            return str;
        }

        /// <summary>
        /// Process the specified URL and download the images.
        /// </summary>
        /// <param name="url">The URL to process.</param>
        /// <param name="saveTo">A directory to save the images to.</param>
        public void Process(Uri url, String saveTo)
        {
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
                    if (String.Compare(tag.Name, "img", true) == 0)
                    {
                        String src = tag["src"];
                        Uri u = new Uri(url, src);
                        String filename = ExtractFile(u);
                        String saveFile = Path.Combine(saveTo, filename);
                        WebRequest http2 = HttpWebRequest.Create(u);
                        HttpWebResponse response2 = (HttpWebResponse)http2.GetResponse();
                        this.DownloadBinaryFile(response2, saveFile);
                        response2.Close();
                    }
                }
            }
        }

        static void Main(string[] args)
        {
            Uri u = new Uri("http://www.httprecipes.com/1/6/image.php");
            ExtractImages parse = new ExtractImages();
            parse.Process(u, ".");
        }
    }
}

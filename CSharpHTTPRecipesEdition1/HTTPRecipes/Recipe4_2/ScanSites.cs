using System;
using System.Net;
using System.IO;
using System.Collections.Generic;

namespace Recipe4_2
{
	/// <summary>
    /// Recipe #4.2: Scan IP's for Sites
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to scan a series of IP addresses
    /// for web sites.  This recipe is designed to take a IP
    /// address, such as 192.168.1 and then cycle through all
    /// 256 IP addresses by providing the fourth part of the
    /// IP address.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
	/// </summary>
	class ScanSites
	{
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

			return str.Substring(location1, location2-location1 );
		}

		/// <summary>
		/// This method downloads the specified URL into a C#
		/// String. This is a very simple method, that you can
		/// reused anytime you need to quickly grab all data from
		/// a specific URL.
		/// </summary>
		/// <param name="url">The URL to download.</param>
		/// <param name="timeout">The amount of time to wait before aborting.</param>
		/// <returns>The contents of the URL that was downloaded.</returns>
		public String DownloadPage(Uri url,int timeout)
		{
            try
            {
                WebRequest http = HttpWebRequest.Create(url);
                http.Timeout = timeout;
                HttpWebResponse response = (HttpWebResponse)http.GetResponse();
                StreamReader stream = new StreamReader(response.GetResponseStream(), System.Text.Encoding.ASCII);

                String result = stream.ReadToEnd();

                response.Close();
                stream.Close();
                return result;
            }
            catch (Exception)
            {
                return null;
            }
		}

		/// <summary>
		/// Scan the specified IP address and return the title of
		/// the webpage found there, or null if no connection can
		/// be made.
		/// </summary>
		/// <param name="ip">The IP address to scan.</param>
		/// <returns>The title of the webpage, or null if no website.</returns>
		private String scanIP(String ip)
		{
            String title = null;

			Console.WriteLine("Scanning: " + ip);
			String page = DownloadPage(new Uri("http://" + ip), 1000);
            if (page != null)
            {
                title = ExtractNoCase(page, "<title>", "</title>", 0);
                if (title == null)
                    title = "[Untitled site]";
            }
			return title;
		}


		/// <summary>
		/// Scan a range of 256 IP addressed.  Provide the prefix
		/// of the IP address, without the final fourth.  For 
		/// example "192.168.1".
		/// </summary>
		/// <param name="ip">The IP address prefix(i.e. 192.168.1)</param>
        public void scan(String ip)
        {
            if (!ip.EndsWith("."))
            {
                ip += ".";
            }

            // Create a list to hold sites found.
            List<String> list = new List<String>();

            // Scan through IP addresses ending in 0 - 255.
            for (int i = 1; i < 255; i++)
            {
                String address = ip + i;
                String title = scanIP(address);
                if (title != null)
                    list.Add(address + ":" + title);
            }

            // Now display the list of sites found.
            Console.WriteLine();
            Console.WriteLine("Sites found:");
            if (list.Count > 0)
            {
                foreach (String site in list)
                {
                    Console.WriteLine(site);
                }
            }
            else
            {
                Console.WriteLine("No sites found");
            }
        }

		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main(string[] args)
		{
            if (args.Length != 1)
            {
                Console.WriteLine("Usage: Recipe4_2 [IP prefix, i.e. 192.168.1]");
            }
            else
            {
                ScanSites d = new ScanSites();
                d.scan(args[0]);
            }
		}
	}
}

using System;
using System.IO;
using System.Net;

namespace Recipe4_1
{
	/// <summary>
	/// Recipe #4.1: Scan a URL's Headers
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
	///
	/// This recipe displays the headers provided by a web server.
	///
	/// This software is copyrighted. You may use it in programs
	/// of your own, without restriction, but you may not
	/// publish the source code without the author's permission.
	/// For more information on distributing this code, please
	/// visit:
	///    http://www.heatonresearch.com/hr_legal.php
	/// </summary>
	class ScanURL
	{
		/// <summary>
		/// Scan the URL and display headers.
		/// </summary>
		/// <param name="u">The URL to scan.</param>
		public void Scan(String u)
		{
			Uri url = new Uri(u);
			WebRequest http = HttpWebRequest.Create(url);
			WebResponse response = http.GetResponse();
			
			int count = 0;
			String key, value;

			for(count=0;count<response.Headers.Keys.Count;count++)
			{
				key = response.Headers.Keys[count];
				value = response.Headers[key];

				if (value != null)
				{
					if (key == null)
						Console.WriteLine(value);
					else
						Console.WriteLine(key + ": " + value);
				}
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
                Console.WriteLine("Usage: Recipe4_1 [URL to Scan]");
			} 
			else
			{
				ScanURL d = new ScanURL();
				d.Scan(args[0]);
			}
		}


	}
}

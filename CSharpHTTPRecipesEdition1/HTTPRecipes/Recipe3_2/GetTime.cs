using System;
using System.Net;
using System.IO;

namespace Recipe3_2
{
    /// <summary>
    /// Recipe #3.2: Extract Information from a Web Site
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// Access the httprecipes.com site and get the time in 
    /// St. Louis, MO.  Shows how to parse data from HTML.
    /// 
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    ///
    /// </summary>
	class GetTime
	{
		/// <summary>
		/// This method is very useful for grabbing information from a
		/// HTML page.  
		/// </summary>
		/// <param name="str">The string to parse.</param>
		/// <param name="token1">The text, or tag, that comes before the desired text</param>
		/// <param name="token2">The text, or tag, that comes after the desired text</param>
		/// <param name="count">Which occurrence of token1 to use, 1 for the first</param>
		/// <returns>The contents of the URL that was downloaded.</returns>
		public String Extract(String str, String token1, String token2, int count)
		{
			int location1, location2;

			location1 = location2 = 0;
			do
			{
				location1 = str.IndexOf(token1, location1+1);

				if (location1 == -1)
					return null;

				count--;
			} while (count > 0);

			location2 = str.IndexOf(token2, location1 + 1);
			if (location2 == -1)
				return null;

			location1+=token1.Length;
			return str.Substring(location1, location2-location1 );
		}

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
			StreamReader stream = new StreamReader(response.GetResponseStream(),System.Text.Encoding.ASCII   );

			String result = stream.ReadToEnd();
            
			response.Close();
			stream.Close();
			return result;
		}

		/// <summary>
		/// Run the example.
		/// </summary>
		public void Go()
		{
			Uri u = new Uri("http://www.httprecipes.com/1/3/time.php");
			String str = DownloadPage(u);

			Console.WriteLine(Extract(str, "<b>", "</b>", 1));
		}


		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main(string[] args)
		{
			GetTime module = new GetTime();
			module.Go();
		}
	}
}

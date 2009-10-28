using System;
using System.Net;
using System.IO;

namespace Recipe3_1
{
	/// <summary>
    /// Recipe #3.1: Downloading the Contents of a Web Page
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
	///
	/// Simple class that demonstrates how to download a web
	/// page, and display it to the console.	
    /// 
	/// This software is copyrighted. You may use it in programs
	/// of your own, without restriction, but you may not
	/// publish the source code without the author's permission.
	/// For more information on distributing this code, please
	/// visit:
	///    http://www.heatonresearch.com/hr_legal.php
	///
	/// </summary>
	class GetPage
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
			StreamReader stream = new StreamReader(response.GetResponseStream(),System.Text.Encoding.ASCII   );

			String result = stream.ReadToEnd();

			response.Close();
			stream.Close();
			return result;
		}

		/// <summary>
		/// Run the example.
		/// </summary>
		/// <param name="page">The page to download.</param>
		public void Go(String page)
		{
			Uri u = new Uri(page);
			String str = DownloadPage(u);
			Console.WriteLine(str);
		}

		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main(string[] args)
		{
			GetPage module = new GetPage();
			String page;
			if (args.Length == 0)
				page = "http://www.httprecipes.com/1/3/time.php";
			else
				page = args[0];
			module.Go(page);
		}
	}
}

using System;
using System.IO;
using System.Net;

namespace Recipe3_5
{
	/// <summary>
	/// Recipe #3.5: Downloading a Text File
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
	///
	/// Download a text file, such as a HTML page, from a URL.
	///
	/// This software is copyrighted. You may use it in programs
	/// of your own, without restriction, but you may not
	/// publish the source code without the author's permission.
	/// For more information on distributing this code, please
	/// visit:
	///    http://www.heatonresearch.com/hr_legal.php
	/// </summary>
	class DownloadText
	{
		/// <summary>
		/// Download the specified text page.
		/// </summary>
		/// <param name="page">The URL to download from.</param>
		/// <param name="filename">The local file to save to.</param>
		public void DownloadTextFile(String page, String filename)
		{
			Uri u = new Uri(page);
			FileStream os = new FileStream(filename,FileMode.Create);
            HttpWebRequest http = (HttpWebRequest)HttpWebRequest.Create(u);
			HttpWebResponse response = (HttpWebResponse)http.GetResponse();
			StreamReader reader = new StreamReader(response.GetResponseStream(),System.Text.Encoding.ASCII   );
			StreamWriter writer = new StreamWriter(os,System.Text.Encoding.ASCII   );
            http.AllowAutoRedirect = false;
			String line;
			do
			{
				line = reader.ReadLine();
				if( line!=null )
					writer.WriteLine(line);

			} while(line!=null);

			reader.Close();
			writer.Close();
			os.Close();
		}


		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main(string[] args)
		{
			if (args.Length != 2)
			{
				DownloadText d = new DownloadText();
				d.DownloadTextFile("http://www.httprecipes.com/1/3/text.php", "./text.html");
			} 
			else
			{
				DownloadText d = new DownloadText();
				d.DownloadTextFile(args[0], args[1]);
			}
		}
	}
}

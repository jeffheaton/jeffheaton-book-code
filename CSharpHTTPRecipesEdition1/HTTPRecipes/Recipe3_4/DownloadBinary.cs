using System;
using System.IO;
using System.Net;

namespace Recipe3_4
{
	/// <summary>
	/// Recipe #3.4: Downloading a Binary File
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
	///
	/// Download a binary file, such as an image, from a URL.
	///
	/// This software is copyrighted. You may use it in programs
	/// of your own, without restriction, but you may not
	/// publish the source code without the author's permission.
	/// For more information on distributing this code, please
	/// visit:
	///    http://www.heatonresearch.com/hr_legal.php
	/// </summary>
	class DownloadBinary
	{
		/// <summary>
		/// Used to convert strings to byte arrays.
		/// </summary>
		private System.Text.UTF8Encoding  encoding=new System.Text.UTF8Encoding();

		/// <summary>
		/// This method downloads the specified URL into a C#
		/// String. This is a very simple method, that you can
		/// reused anytime you need to quickly grab all data from
		/// a specific URL.
		/// </summary>
		/// <param name="url">The URL to download.</param>
		/// <returns>The contents of the URL that was downloaded.</returns>
		public void DownloadBinaryFile(Uri url,String filename)
		{
			byte []buffer = new byte[4096];
			FileStream os = new FileStream(filename,FileMode.Create);
			WebRequest http = HttpWebRequest.Create(url);
			HttpWebResponse response = (HttpWebResponse)http.GetResponse();
			Stream stream = response.GetResponseStream();
			
			int count = 0;
			do
			{
				count = stream.Read(buffer,0,buffer.Length);
				if(count>0)
					os.Write(buffer,0,count);
			} while(count>0);
			
			response.Close();
			stream.Close();
			os.Close();
		}



        /// <summary>
        /// The main entry point for the program.
        /// </summary>
        /// <param name="args">Program arguments.</param>
		static void Main(string[] args)
		{
			if (args.Length != 2)
			{
				DownloadBinary d = new DownloadBinary();
				d.DownloadBinaryFile(new Uri("http://www.httprecipes.com/1/3/sea.jpg"), "./sea2.jpg");
			} 
			else
			{
				DownloadBinary d = new DownloadBinary();
				d.DownloadBinaryFile(new Uri(args[0]), args[1]);
			}
		}
	}
}

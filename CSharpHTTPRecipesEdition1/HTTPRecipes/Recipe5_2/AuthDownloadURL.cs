using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;

namespace Recipe5_2
{
    /// <summary>
    /// Recipe #5.2: Downloading a URL(text or binary)
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    /// 
    /// This recipe shows how to download a text or binary file,
    /// using HTTP authentication.
    /// 
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class AuthDownloadURL
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
        /// Download the specified text page.
        /// </summary>
        /// <param name="response">The HttpWebResponse to download from.</param>
        /// <param name="filename">The local file to save to.</param>
        public void DownloadTextFile(HttpWebResponse response, String filename)
        {
            byte[] buffer = new byte[4096];
            FileStream os = new FileStream(filename, FileMode.Create);
            StreamReader reader = new StreamReader(response.GetResponseStream(), System.Text.Encoding.ASCII);
            StreamWriter writer = new StreamWriter(os, System.Text.Encoding.ASCII);

            String line;
            do
            {
                line = reader.ReadLine();
                if (line != null)
                    writer.WriteLine(line);

            } while (line != null);

            reader.Close();
            writer.Close();
            os.Close();
        }


        /// <summary>
        /// Download either a text or binary file from a URL.
        /// The URL's headers will be scanned to determine the
        /// type of tile.  The user id and password are authenticated.
        /// </summary>
        /// <param name="remoteURL">The URL to download from.</param>
        /// <param name="localFile">The local file to save to.</param>
        /// <param name="uid">The user id to use.</param>
        /// <param name="pwd">The password to use.</param>
        public void Download(Uri remoteURL, String localFile,String uid,String pwd)
        {
            NetworkCredential networkCredential = new NetworkCredential(uid, pwd);
            WebRequest http = HttpWebRequest.Create(remoteURL);
            http.PreAuthenticate = true;
            http.Credentials = networkCredential;
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();

            String type = response.Headers["Content-Type"].ToLower().Trim();
            if (type.StartsWith("text"))
                DownloadTextFile(response, localFile);
            else
                DownloadBinaryFile(response, localFile);

        }


        /// <summary>
        /// The main entry point for the program.
        /// </summary>
        /// <param name="args">Program arguments.</param>
        static void Main(string[] args)
        {
            AuthDownloadURL d = new AuthDownloadURL();
            if (args.Length != 4)
            {
                d.Download(new Uri("https://www.httprecipes.com/1/5/secure/"),
                    "test.html",
                    "testuser",
                    "testpassword");
            }
            else
            {

                d.Download(new Uri(args[0]), args[1], args[2], args[3]);
            }
        }
    }
}

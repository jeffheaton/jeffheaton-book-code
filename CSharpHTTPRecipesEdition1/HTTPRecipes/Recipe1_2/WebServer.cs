using System;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.IO;

namespace HeatonResearch.httprecipes.ch1.Recipe1_2
{
	/// <summary>
	/// Recipe #1.2: Simple File Based Web Server
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
	/// 
	/// A simple web server that exposes a single directory as the
	/// root of a web site.  Only the most basic web server functions
	/// are provided, such as index.html redirect.
	/// 
	/// This software is copyrighted. You may use it in programs
	/// of your own, without restriction, but you may not
	/// publish the source code without the author's permission.
	/// For more information on distributing this code, please
	/// visit:
	///    http://www.heatonresearch.com/hr_legal.php
	/// 
	/// @author Jeff Heaton
	/// @version 1.1
	/// </summary>
	class WebServer
	{
		/// <summary>
		/// The server socket that will listen for connections
		/// </summary>
		private Socket server;

		/// <summary>
		/// Used to convert strings to byte arrays.
		/// </summary>
		private System.Text.ASCIIEncoding  encoding=new System.Text.ASCIIEncoding();

		/// <summary>
		/// Where the HTML files are stored.
		/// </summary>
		private String httproot;

		/// <summary>
		/// Construct the web server to listen on the specified 
		/// port.
		/// </summary>
		/// <param name="port">The port to use for the server.</param>
		public WebServer(int port,String httproot)
		{
			server = new Socket(AddressFamily.InterNetwork,SocketType.Stream,ProtocolType.IP);
			server.Bind(new IPEndPoint(IPAddress.Loopback,80));
			this.httproot = httproot;
		}

		/// <summary>
		/// The run method endlessly waits for connections 
		///	as each connection is opened(from web browsers)
		/// the connection is passed off to handleClientSession.
		/// </summary>
		public void Run()
		{
			server.Listen(10);
			for(;;)
			{
				Socket socket = server.Accept();
				HandleClientSession(socket);
			}
		}

		/// <summary>
		/// Read a string from the socket.
		/// </summary>
		/// <param name="socket">The socket to read from.</param>
		/// <returns>THe string read.</returns>
		private String SocketRead(Socket socket)
		{
			StringBuilder result = new StringBuilder();
			byte []buffer = new byte[1];
			
			while( socket.Receive(buffer)>0 )
			{
				char ch = (char)buffer[0];
				if( ch=='\n')
					break;
				if( ch!='\r')
					result.Append(ch);
			}

			return result.ToString();
	
		}

		/// <summary>
		/// Write a string to the socket, followed by a line break.
		/// </summary>
		/// <param name="socket">The socket to write to.</param>
		/// <param name="str">What to write to the socket.</param>
		private void SocketWrite(Socket socket,String str)
		{
			socket.Send(encoding.GetBytes(str) );
			socket.Send(encoding.GetBytes("\r\n"));
		}

		/// <summary>
		///  Handle a client session. This method displays the incoming
		///  HTTP request and responds with a "Hello World" response.
		/// </summary>
		/// <param name="socket">The client socket.</param>
		private void HandleClientSession(Socket socket)
		{
			// read in the first line
			Console.WriteLine("**New Request**");
			String first = SocketRead(socket);
			Console.WriteLine(first);

			// read in headers and post data
			String line;
			do
			{
				line = SocketRead(socket);
				if(line!=null)
					Console.WriteLine(line);
			} while (line!=null && line.Length>0 );

			// write the HTTP response
			char []delim = {' '};
			String []tok = first.Split();
			String verb = tok[0]; 
			String path = tok[1]; 
			String version = tok[2]; 

			if ( String.Compare(verb,"GET",true)==0 )
				SendFile(socket, path);
			else
				Error(socket, 500, "Unsupported command");

			// close everything up
			socket.Close();
		}
		/// <summary>
		/// Add a slash to the end of a path, if there is not a slash
		/// there already.  This method adds the correct type of slash,
		///	depending on the operating system.
		/// </summary>
		/// <param name="path">The path to add a slash to.</param>
		private void AddSlash(StringBuilder path)
		{
			if (!path.ToString().EndsWith("\\"))
				path.Append("\\");
		}

		/// <summary>
		/// Determine the correct "content type" based on the file
		/// extension.
		/// </summary>
		/// <param name="path">The file being transfered.</param>
		/// <returns>The correct content type for this file.</returns>
		private String GetContent(String path)
		{
			path = path.ToLower ();
			if (path.EndsWith(".jpg") || path.EndsWith(".jpeg"))
				return "image/jpeg";
			else if (path.EndsWith(".gif"))
				return "image/gif";
			else if (path.EndsWith(".png"))
				return "image/png";
			else
				return "text/html";
		}
		
		/// <summary>
		/// Transmit a HTTP response.  All responses are handled by
		/// this method.
		/// </summary>
		/// <param name="socket">The socket to transmit to.</param>
		/// <param name="code">The response code, i.e. 404 for not found.</param>
		/// <param name="message">The message, usually OK or error message.</param>
		/// <param name="body">The data to be transfered.</param>
		/// <param name="content">The content type.</param>
		private void Transmit(
			Socket socket, 
			int code, 
			String message,										
			byte []body, 
			String content) 
		{
			StringBuilder headers = new StringBuilder();
			headers.Append("HTTP/1.1 ");
			headers.Append(code);
			headers.Append(' ');
			headers.Append(message);
			headers.Append("\n");
			headers.Append("Content-Length: " + body.Length + "\n");
			headers.Append("Server: Heaton Research Example Server\n");
			headers.Append("Connection: close\n");
			headers.Append("Content-Type: " + content + "\n");
			headers.Append("\n");
			socket.Send(encoding.GetBytes(headers.ToString()));
			socket.Send(body);
		}

		/// <summary>
		/// Display an error to the web browser.
		/// </summary>
		/// <param name="socket">The socket to display the error to.</param>
		/// <param name="code">The response code, i.e. 404 for not found.</param>
		/// <param name="message">The error that occured.</param>
		private void Error(Socket socket, int code, String message)
		{
			StringBuilder body = new StringBuilder();
			body.Append("<html><head><title>");
			body.Append(code + ":" + message);
			body.Append("</title></head><body><p>An error occurred.</p><h1>");
			body.Append(code);
			body.Append("</h1><p>");
			body.Append(message);
			body.Append("</p></body></html>");
			Transmit(socket, code, message, encoding.GetBytes(body.ToString()), "text/html");
		}



		/// <summary>
		/// Send a disk file.  The path passed in is from the URL, this
		/// URL is translated into a local disk file, which is then
		///	transfered.
		/// </summary>
		/// <param name="socket">The socket to send to.</param>
		/// <param name="path">The file requested from the URL.</param>
		private void SendFile(Socket socket, String path)
		{
			char []delim = { '/' };


			// parse the file by /'s and build a local file
			String []tok = path.Split(delim);
			Console.WriteLine(path);
			StringBuilder physicalPath = new StringBuilder(httproot);
			AddSlash(physicalPath);

			foreach(String e in tok)
			{
				if (!e.Trim().Equals("\\") )
				{
					if (e.Equals("..") || e.Equals("."))
					{
						Error(socket, 500, "Invalid request");
						return;
					}
					AddSlash(physicalPath);
					physicalPath.Append(e);
				} 
			}

			

			// if there is no file specified, default
			// to index.html

			if (physicalPath.ToString().EndsWith("\\"))
			{
				physicalPath.Append("index.html");
			}

			String filename = physicalPath.ToString();
			// open the file and send it if it exists
			FileInfo file = new FileInfo(filename);
			if (file.Exists)
			{
				// send the file
				FileStream fis = File.Open(filename,FileMode.Open);
				byte []buffer = new byte[(int) file.Length];
				fis.Read(buffer,0,buffer.Length);
				fis.Close();
				this.Transmit(socket, 200, "OK", buffer, GetContent(filename));
			}
				// file does not exist, so send file not found
			else
			{
				this.Error(socket, 404, "File Not Found");
			}
		}


		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main(string[] args)
		{
			if (args.Length < 2)
			{
				Console.WriteLine("Usage:\nRecipe1_2 [port] [http root path]");
			} 
			else
			{
				int port;
				try
				{
					port = int.Parse(args[0]);
					WebServer server = new WebServer(port, args[1]);
					server.Run();
				} 
				catch (ArgumentNullException)
				{
					Console.WriteLine("Invalid port number");
				}
				catch(FormatException)
				{
					Console.WriteLine("Invalid port number");
				}
				catch(OverflowException)
				{
					Console.WriteLine("Invalid port number");
				}
			}
		}
	}
}
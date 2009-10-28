using System;
using System.Net.Sockets;
using System.Net;
using System.Text;

namespace HeatonResearch.httprecipes.ch1.Recipe1_1
{
	/// <summary>
	/// Recipe #1.1: Very Simple Web Server
	/// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
	///
	/// HTTP Programming Recipes for C# Bots
	/// ISBN: 0-9773206-7-7
	/// http://www.heatonresearch.com/articles/series/20/
	///
	/// A simple web server that will respond to every request
	/// with "Hello World".
	///
	/// This software is copyrighted. You may use it in programs
	/// of your own, without restriction, but you may not
	/// publish the source code without the author's permission.
	/// For more information on distributing this code, please
	/// visit:
	///    http://www.heatonresearch.com/hr_legal.php
	/// </summary>
	class SimpleWebServer
	{
		/// <summary>
		/// The server socket that will listen for connections
		/// </summary>
		private Socket server;


		/// <summary>
		/// Construct the web server to listen on the specified 
		/// port.
		/// </summary>
		/// <param name="port">The port to use for the server.</param>
		public SimpleWebServer(int port)
		{
			server = new Socket(AddressFamily.InterNetwork,SocketType.Stream,ProtocolType.IP);
			server.Bind(new IPEndPoint(IPAddress.Loopback,port));
		}

		/// <summary>
		/// The run method endlessly waits for connections 
		///	as each connection is opened(from web browsers)
		/// the connection is passed off to handleClientSession.
		/// </summary>
		public void run()
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
			System.Text.ASCIIEncoding  encoding=new System.Text.ASCIIEncoding();
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
			SocketWrite(socket,"HTTP/1.1 200 OK");
			SocketWrite(socket,"");
			SocketWrite(socket,"<html>");
			SocketWrite(socket,"<head><title>Simple Web Server</title></head>");
			SocketWrite(socket,"<body>");
			SocketWrite(socket,"<h1>Hello World</h1>");
			SocketWrite(socket,"<//body>");
			SocketWrite(socket,"</html>");

		    // close everything up
		    socket.Close();
		}


		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main(string[] args)
		{
			SimpleWebServer webServer = new SimpleWebServer(80);
			webServer.run();
		}
	}
}

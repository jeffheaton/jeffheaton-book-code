package com.heaton.bot;
import java.net.*;
import java.io.*;

public class HTTPSocket extends HTTP {
  public void writeString(OutputStream out,String str)
  throws java.io.IOException
  {
    out.write( str.getBytes() );
    out.write( "\r\n".getBytes() );
    Log.log(Log.LOG_LEVEL_TRACE,"Socket Out:" + str );
  }

  synchronized public void lowLevelSend(String url,String post)
  throws java.net.UnknownHostException, java.io.IOException
  {
    String command;// What kind of send POST or GET
    StringBuffer headers;// Used to hold outgoing client headers
    byte buffer[]=new byte[1024];//A buffer for reading
    int l,i;// Counters
    Attribute a;// Used to process incoming attributes
    int port=80;// What port, default to 80
    boolean https = false;// Are we using HTTPS?
    URL u;// Used to help parse the url parameter
    Socket socket = null;
    OutputStream out = null;
    InputStream in = null;

// parse the URL
    try {
      if ( url.toLowerCase().startsWith("https") ) {
        url = "http" + url.substring(5);// so it can be parsed
        u = new URL(url);
        if ( u.getPort()==-1 )
          port=443;
        https = true;
      } else
        u = new URL(url);

      if ( u.getPort()!=-1 )
        port = u.getPort();

// connect
      if ( https )
        socket = SSL.getSSLSocket(u.getHost(),port);
      else
        socket = new Socket(u.getHost(),port);

      socket.setSoTimeout(_timeout);
      out = socket.getOutputStream();
      in = socket.getInputStream();

// send command, i.e. GET or POST
      if ( post == null )
        command = "GET ";
      else
        command = "POST ";

      String file = u.getFile();
      if ( file.length()==0 )
        file="/";
      command = command + file + " HTTP/1.0";
      writeString(out,command);

// Process client headers

      if ( post!=null )
        writeString(out,"Content-Length: " + post.length());

      writeString(out,"Host: " + u.getHost() );

      i=0;
      headers = new StringBuffer();
      do {
        a = _clientHeaders.get(i++);
        if ( a!=null ) {
          headers.append(a.getName());
          headers.append(':');
          headers.append(a.getValue());
          headers.append("\r\n");
          Log.log(Log.LOG_LEVEL_TRACE,"Client Header:" + a.getName() + "=" + a.getValue() );
        }
      } while ( a!=null );

      if ( headers.length()>=0 )
        out.write(headers.toString().getBytes() );

// Send a blank line to signal end of HTTP headers
      writeString(out,"");
      if ( post!=null ) {
        Log.log(Log.LOG_LEVEL_TRACE,"Socket Post(" + post.length() + " bytes):" + new String(post) );
        out.write( post.getBytes()  );
      }

/* Read the result */
/* First read HTTP headers */

      _header.setLength(0);
      int chars = 0;
      boolean done = false;

      while ( !done ) {
        int ch;

        ch = in.read();
        if ( ch==-1 )
          done=true;

        switch ( ch ) {
        case '\r':
          break;
        case '\n':
          if ( chars==0 )
            done =true;
          chars=0;
          break;
        default:
          chars++;
          break;
        }

        _header.append((char)ch);
      }

// now parse the headers and get content length
      parseHeaders();
      Attribute acl = _serverHeaders.get("Content-length");
      int contentLength=0;
      try {
        if ( acl!=null )
          contentLength = Integer.parseInt(acl.getValue());
      } catch ( Exception e ) {
        Log.logException("Bad value for content-length:",e);
      }

      _body.setLength(0);

      int max = _maxBodySize;

      if ( contentLength!=0 ) {
        // read in using content length
        while ( (contentLength--)>0 ) {
          l = in.read(buffer);
          if ( l<0 )
            break;
          _body.append(new String(buffer,0,l,"8859_1"));
          max--;
          if ( max<0 && (_maxBodySize!=-1) )
            break;
        }
      } else {
        // read in with no content length
        do {
          l = in.read(buffer);
          if ( l<0 )
            break;
          _body.append(new String(buffer,0,l,"8859_1"));
          max--;
          if ( max<0 && (_maxBodySize!=-1) )
            break;
        } while ( l!=0 );
      }

      Log.log(Log.LOG_LEVEL_DUMP,"Socket Page Back:" + _body + "\r\n" );
    }

// Cleanup
    finally {
      if ( out!=null ) {
        try {
          out.close();
        } catch ( Exception e ) {
        }
      }

      if ( in!=null ) {
        try {
          in.close();
        } catch ( Exception e ) {
        }
      }

      if ( socket!=null ) {
        try {
          socket.close();
        } catch ( Exception e ) {
        }
      }
    }
  }

  HTTP copy()
  {
    return new HTTPSocket();
  }
}

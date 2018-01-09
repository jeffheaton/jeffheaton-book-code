package com.heaton.bot;
import java.net.*;

/**
 * This class implements an HTTP handler.  The low-level
 * details are left to a derived class.  Most likely you
 * will be using the HTTPSocket class, which derives from
 * this one.  This class handles these specific issues:
 *
 * + Cookies
 * + The referrer tag
 * + HTTP User Authentication
 * + Automatic Redirection
 * + Parsing headers
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public abstract class HTTP {

  /**
   * The body data that was downloaded during the last send.
   */
  protected StringBuffer _body = new StringBuffer();

  /**
   * The raw header text that was recieved during the last
   * send.
   */
  protected StringBuffer _header = new StringBuffer();

  /**
   * The URL that was ultimately used during the last send.
   */
  protected String _url;

  /**
   * True if the HTTP class should automatically follow HTTP
   * redirects.
   */
  protected boolean _autoRedirect = true;

  /**
   * The cookies that are being tracked by this HTTP session.
   */
  protected static AttributeList _cookieStore = new AttributeList();

  /**
   * The client headers, these are the headers that are sent
   * to the server.
   */
  protected AttributeList _clientHeaders = new AttributeList();

  /**
   * The server headers, these are the the headers that the
   * server sends back.
   */
  protected AttributeList _serverHeaders = new AttributeList();

  /**
   * True if session cookies should be used.
   */
  protected boolean _useCookies = false;

  /**
   * True if permanent cookies should be used.
   */
  protected boolean _usePermCookies = false;

  /**
   * The HTTP response(i.e. OK).  If NOT okay, this response is
   * thrown as an exception.
   */
  protected String _response;

  /**
   * The timeout in milliseconds.
   */
  protected int _timeout = 30000;

  /**
   * The page previously on.  Used to accurately represent the
   * referrer header.
   */
  protected String _referrer = null;

  /**
   * The agent(or browser) that the HTTP class is reporting
   * to the server.
   */
  protected String _agent = "Mozilla/4.0";

  /**
   * The user being reported for HTTP authentication.
   */
  protected String _user = "";

  /**
   * The password being reported for user authentication.
   */
  protected String _password = "";

  /**
   * The max size that a HTTP body can be.
   */
  protected int _maxBodySize = -1;

  /**
   * The copy method is used to create a new copy of this HTTP handler.
   * This method is implemented abstract and should be implemented by
   * derived classes.
   *
   * @return A new HTTP handler.
   */
  abstract HTTP copy();

  /**
   * This method actually sends the data.  This is where derived
   * classes implement their own low-level send.
   *
   * @param url The URL to send data to.
   * @param post Any data that is to be posted.  If no data is posted, this method
   * does an HTTP GET.
   * @exception java.net.UnknownHostException Thrown if the host can not be located.
   * @exception java.io.IOException Thrown if a network error occurs.
   */
  abstract protected void lowLevelSend(String url,String post)
  throws java.net.UnknownHostException, java.io.IOException;


  /**
   * This method is called to clear any cookies that this HTTP
   * object might be holding.
   */
  public void clearCookies()
  {
    _cookieStore.clear();
  }

  /**
   * This method is called to add any applicable cookies to the
   * header.
   */
  protected void addCookieHeader()
  {
    int i=0;
    String str="";

    if ( _cookieStore.get(0)==null )
      return;

    while ( _cookieStore.get(i) != null ) {
      CookieParse cookie;
      if ( str.length()!=0 )
        str+="; ";// add on ; if needed
      cookie = (CookieParse)_cookieStore.get(i);
      str+=cookie.toString();
      i++;
    }

    _clientHeaders.set("Cookie",str);
    Log.log(Log.LOG_LEVEL_TRACE,"Send cookie: " + str );
  }

  /**
   * This method is called to add the user authorization headers
   * to the HTTP request.
   */
  protected void addAuthHeader()
  {
    if ( (_user.length()==0) || (_password.length()==0) )
      return;
    String hdr = _user + ":" + _password;
    String encode = URLUtility.base64Encode(hdr);
    _clientHeaders.set("Authorization","Basic " + encode );
  }

  /**
   * Send is the public interface to this class that actually sends
   * an HTTP request.
   *
   * @param url The URL being sent to.
   * @param post Any data to post.
   * @exception java.net.UnknownHostException
   * @exception java.io.IOException
   */
  public void send(String url,String post)
  throws java.net.UnknownHostException,java.io.IOException
  {
    int rtn; // the return value

    if ( post==null )
      Log.log(Log.LOG_LEVEL_NORMAL,"HTTP GET " + url );
    else
      Log.log(Log.LOG_LEVEL_NORMAL,"HTTP POST " + url );

    setURL(url);
    if ( _referrer!=null )
      _clientHeaders.set("referrer",_referrer.toString());

    _clientHeaders.set("Accept","image/gif,"
                       + "image/x-xbitmap,image/jpeg, "
                       + "image/pjpeg, application/vnd.ms-excel,"
                       + "application/msword,"
                       + "application/vnd.ms-powerpoint, */*");
    _clientHeaders.set("Accept-Language","en-us");
    _clientHeaders.set("User-Agent",_agent);

    while ( true ) {
      if ( _useCookies )
        addCookieHeader();
      addAuthHeader();
      lowLevelSend(_url,post);

      if ( _useCookies )
        parseCookies();
      Attribute a = _serverHeaders.get("Location");

      if ( (a==null) || !_autoRedirect ) {
        _referrer = getURL();
        return;
      }

      URL u = new URL(new URL(_url),a.getValue());
      Log.log(Log.LOG_LEVEL_NORMAL,"HTTP REDIRECT to " + u.toString() );
      post=null;// don't redirect as a post
      setURL(u.toString());
    }
  }

  /**
   * This method is called to get the body data that was returned
   * by this request.
   *
   * @return The body data for the last request.
   */
  public String getBody()
  {
    return new String(_body);
  }

  /**
   * Get the URL that was ultimately requested. You might get a
   * different URL than was requested if auto redirection is
   * enabled.
   */
  public String getURL()
  {
    return _url;
  }

  /**
   * Called to set the internal URL that is kept for the last
   * request.
   *
   * @param u The new URL.
   */
  public void setURL(String u)
  {
    _url = u;
  }

  /**
   * Used to determine if the HTTP class should automatically
   * resolve any HTTP redirects.
   *
   * @param b True to resolve redirects, false otherwise.
   */
  public void SetAutoRedirect(boolean b)
  {
    _autoRedirect = b;
  }

  /**
   * This method will return an AttributeLIst of client headers.
   *
   * @return An AttributeList of client headers.
   */
  public AttributeList getClientHeaders()
  {
    return _clientHeaders;
  }
  /**
   * This method will return an AttributeList of server headers.
   *
   * @return An AttributeList of server headers.
   */

  public AttributeList getServerHeaders()
  {
    return _serverHeaders;
  }
  /**
   * This method is called internally to parse any cookies and add
   * them to the cookie store.
   */

  protected void parseCookies()
  {
    Attribute a;

    int i=0;
    while ( (a = _serverHeaders.get(i++)) != null ) {
      if ( a.getName().equalsIgnoreCase("set-cookie") ) {
        CookieParse cookie = new CookieParse();

        cookie._source=new StringBuffer(a.getValue());
        cookie.get();
        cookie.setName(cookie.get(0).getName());
        if ( _cookieStore.get(cookie.get(0).getName())==null ) {
          if ( (cookie.get("expires")==null) ||
               ( cookie.get("expires")!=null) && _usePermCookies )
            _cookieStore.add(cookie);
        }
        Log.log(Log.LOG_LEVEL_TRACE,"Got cookie: " + cookie.toString() );
      }
    }
  }
  /**
   * This method is used to access a specific cookie.
   *
   * @param name The name of the cookie being sought.
   * @return The cookie being sought, or null.
   */

  public CookieParse getCookie(String name)
  {
    return((CookieParse)_cookieStore.get(name));
  }
  /**
   * This method controls the use of cookies by this HTTP object.
   *
   * @param session True to use session cookies, false not to.
   * @param perm True to use permanent cookies, false not to.
   */

  public void setUseCookies(boolean session,boolean perm)
  {
    _useCookies = session;
    _usePermCookies = perm;
  }
  /**
   * This method allows you to determine if session cookies
   * are being used.
   *
   * @return True if session cookies are being used.
   */

  public boolean getUseCookies()
  {
    return _useCookies;
  }
  /**
   * This method allows you to determine if permanent cookies
   * are being used.
   *
   * @return Returns true if permanent cookies are being used, false otherwise.
   */

  public boolean getPerminantCookies()
  {
    return _usePermCookies;
  }

  protected void processResponse(String name)
  throws java.io.IOException
  {
    int i1,i2,err=0;
    _response = name;
    i1 = _response.indexOf(' ');
    if ( i1!=-1 ) {
      i2 = _response.indexOf(' ',i1+1);
      if ( i2!=-1 ) {
        try {
          err=Integer.parseInt(_response.substring(i1+1,i2));
        } catch ( Exception e ) {
        }
        if ( (err>=400) && (err<=599) )
          throw new java.io.IOException(_response);
      }
    }
    Log.log(Log.LOG_LEVEL_TRACE,"Response: " + name );
  }

  /**
   * This method is called internally to process user headers.
   *
   * @exception java.io.IOException Thrown if a network error occurs.
   */
  protected void parseHeaders()
  throws java.io.IOException
  {
    boolean first;
    String name,value;
    int l;

    first = true;

    _serverHeaders.clear();
    name = "";

    String parse = new String(_header);

    for ( l=0;l<parse.length();l++ ) {
      if ( parse.charAt(l)=='\n' ) {
        if ( name.length()==0 )
          return;
        else {
          if ( first ) {
            first = false;
            processResponse(name);
          } else {
            int ptr=name.indexOf(':');
            if ( ptr!=-1 ) {
              value = name.substring(ptr+1).trim();
              name = name.substring(0,ptr);
              Attribute a = new Attribute(name,value);
              _serverHeaders.add(a);
              Log.log(Log.LOG_LEVEL_TRACE,"Sever Header:" + name + "=" + value);
            }
          }
        }
        name = "";
      } else if ( parse.charAt(l)!='\r' )
        name+=parse.charAt(l);
    }
  }

  /**
   * This method is called to set the amount of time that the
   * HTTP class will wait for the requested resource.
   *
   * @param i The timeout in milliseconds.
   */
  public void setTimeout(int i)
  {
    _timeout = i;
  }

  /**
   * This method will return the amount of time in milliseconds
   * that the HTTP object will wait for a timeout.
   */
  public int getTimeout()
  {
    return _timeout;
  }

  /**
   * This method will set the maximum body size
   * that will be downloaded.
   *
   * @param i The maximum body size, or -1 for unlimted.
   */
  public void setMaxBody(int i)
  {
    _maxBodySize = i;
  }

  /**
   * This method will return the maximum body size
   * that will be downloaded.
   *
   * @return The maximum body size, or -1 for unlimted.
   */
  public int getMaxBody()
  {
    return _maxBodySize;
  }


  /**
   * Returns the user agent being reported by this HTTP class.
   * The user agent allows the web server to determine what
   * software the web-browser is using.
   *
   * @return The agent string being presented to web servers.
   */
  public String getAgent()
  {
    return _agent;
  }

  /**
   * This method is called to set the agent reported to the browser.
   *
   * @param a The user agent string to be reported to servers.
   */
  public void setAgent(String a)
  {
    _agent = a;
  }

  /**
   * This method is called to set the user id for HTTP user
   * authentication.
   *
   * @param u The user id.
   */
  public void setUser(String u)
  {
    _user = u;
  }

  /**
   * This method is used to set the user's password for HTTP
   * user authentificaiton.
   *
   * @param p The password.
   */
  public void setPassword(String p)
  {
    _password = p;
  }

  /**
   * This method is used to return the user id that is being
   * used for HTTP authentification.
   *
   * @return The user id.
   */
  public String getUser()
  {
    return _user;
  }

  /**
   * This method is used to get the password that is being used
   * for user authentication.
   *
   * @return The password.
   */
  public String getPassword()
  {
    return _password;
  }

  /**
   * This method is used to get the referrer header
   *
   * @return The referrer header.
   */
  public String getReferrer()
  {
    return _referrer;
  }

  /**
   * This method is used to set the referrer header
   *
   * @param p The referrer header.
   */
  public void setReferrer(String p)
  {
    _referrer = p;
  }

  /**
   * This method will return an AttributeList of cookies.
   *
   * @return An AttributeList of server headers.
   */

  public AttributeList getCookies()
  {
    return _cookieStore;
  }



}

import com.heaton.bot.*;


/**
 * Example program from Intro
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
class HTTPGet
{

  /**
   * Main function, startup application.
   * 
   * @param args The command line parameters.
   */
  public static void main(String args[])
  {
    String url;
    
    try
    {
      if( args.length!=1 )
      {
        System.out.println("This program must be called " +
          "with one paramter that specifies the URL to " +
          "retrieve.  For example:\nHTTPGet " +
          "http://www.yahoo.com\n\n");
        System.exit(0);
      }
      url = args[0];

      HTTPSocket http = new HTTPSocket();
      http.send(url,null);
      
      // Read in the headers
      
      System.out.println(
        "* * Headers from: " + url + "* *");
      for(int i=0;i<http.getClientHeaders().length();i++)
      {
        Attribute a = 
          http.getServerHeaders().get(i);
        System.out.println( 
          a.getName() + "=" + a.getValue() );
      }


      // Read in the body
      System.out.println(
        "* * * Data from: " + url + "* * *");
      System.out.println(http.getBody() );
      
      System.exit(0);
    }
    catch(Exception e)
    {
      System.out.println("Exception thrown: " 
        + e.getMessage() );
    }
  }
}
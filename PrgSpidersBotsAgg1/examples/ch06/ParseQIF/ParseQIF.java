import java.io.*;
import java.net.*;
import java.util.*;
import com.heaton.bot.*;

/**
 * Example program from Chapter 6
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 *
 * This example program parses a QIF file. This file
 * may come from either the Internet or a local drive.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class ParseQIF {


  /**
   * This method will read lines from a QIF file,
   * line by line. Each time that this method is
   * called a new QIFElement is returned that
   * contains all of the fields read from that
   * entry.
   *
   * @param r A BufferedReader to read the QIF from,
   * line by line.
   * @return An array of fields read from one line
   * of the QIF file.
   */
  public static QIFElement parseQIFLine(BufferedReader r)
  {
    boolean done=false;
    QIFElement rtn = new QIFElement();

    try {
      while ( !done ) {
        String str = r.readLine();
        if ( str==null )
          return null;
        switch ( str.charAt(0) ) {
        case '^':
          return rtn;
        case 'P':
          rtn.payee = str.substring(1);
          break;
        case 'D':
          rtn.date = str.substring(1);
          break;
        case 'U':
          rtn.amount = str.substring(1);
          break;
        case 'N':
          rtn.number = str.substring(1);
          break;
        case 'A':
          int i=0;
          for ( i=0;i<3;i++ ) {
            if ( rtn.address[i]==null )
              break;
          }
          if ( i<3 )
            rtn.address[i] = str.substring(1);
          break;
        }
      }
    } catch ( IOException e ) {
      return null;
    }
    return null;
  }

  /**
   * Main entry point.
   *
   * @param args The first command line parameter specifies the URL or filename.
   */
  public static void main(String args[])
  {
    FileReader f=null;
    BufferedReader r=null;

    if ( args.length!=1 ) {
      // display usage
      System.out.println("Usage:");
      System.out.println("javac ParseQIF [qif file or URL to parse]");
      System.exit(0);
    }
    try {
      // first try and open it as a file
      f = new FileReader( new File(args[0]) );
      r = new BufferedReader(f);
    } catch ( FileNotFoundException e ) {
      // if it fails as a file, try as a URL
      try {
        HTTPSocket http = new HTTPSocket();
        http.send(args[0],null);
        StringReader sr = new StringReader(http.getBody());
        r = new BufferedReader(sr);
      } catch ( UnknownHostException ee ) {
        // if it fails as a URL too, give up
        System.out.println("Can't open file or URL named: "
                           + args[0] + "(" + ee + ")");
        System.exit(0);
      } catch ( IOException ee ) {
        System.out.println( e );
        System.exit(0);
      }
    }

    // loop through each line and display
    QIFElement a;
    while ( (a=parseQIFLine(r)) != null ) {
      System.out.println("Date:" + a.date );
      System.out.println("Amount:" + a.amount );
      System.out.println("Number:" + a.number );
      System.out.println("Payee:" + a.payee );
      System.out.println("Address Line 1:" + a.address[0] );
      System.out.println("Address Line 2:" + a.address[1] );
      System.out.println("Address Line 3:" + a.address[2] );
      System.out.println("Category:" + a.category );

      System.out.println("-------------");
    }

    // close out the file
    try {
      if ( r!=null )
        r.close();
      if ( f!=null )
        f.close();
    } catch ( IOException e ) {
      System.out.println("Error:" + e );
      System.exit(0);
    }
  }
}

/**
 * Used to hold one element from a QIF file.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
class QIFElement {
  public String date;
  public String amount;
  public String number;
  public String payee;
  public String address[] = new String[3];
  public String category;
}

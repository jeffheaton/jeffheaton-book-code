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
 * This example program parses a CSV file. This file
 * may come from either the Internet or a local drive.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class ParseCSV {

  /**
   * This method will read lines from a CSV file,
   * line by line. Each time that this method is
   * called an array of strings is returned that
   * contains all of the fields read from that
   * line.
   *
   * @param r A BufferedReader to read the CSV from,
   * line by line.
   * @return An array of fields read from one line
   * of the CSV file.
   */
  public static String []parseCSVLine(BufferedReader r)
  {
    String str,rtn;
    boolean quote = false;
    int i;
    Vector vec = new Vector();

    // first read in a line
    try {
      str = r.readLine();
    } catch ( Exception e ) {
      return null;
    }
    // if its null we have reached the end
    if ( str==null )
      return null;

    // now loop through this line
    rtn = "";
    for ( i=0;i<str.length();i++ ) {
      if ( str.charAt(i)=='\"' ) {
        // found a ", check to see if
        // there is a second one
        if ( (i+1)<str.length()&&
             (str.charAt(i+1)=='\"') ) {
          // found a "" so just insert a single "
          rtn+="\"";
          i++;
          continue;
        }
        // toggle quote mode
        quote=!quote;
      } else if ( (str.charAt(i)==',') &&
                  (!quote) ) {
        // found a comma and we're not in quote mode
        vec.addElement(rtn);
        rtn = "";
      } else {
        // append character
        rtn+=str.charAt(i);
      }

    }

    // add in the last element, if present
    if ( rtn.length()>0 )
      vec.addElement(rtn);
    // create an array
    String arr[] = new String[vec.size()];
    vec.copyInto(arr);
    return arr;
  }

  /**
   * Main entry point.
   *
   * @param args The first command line parameter specifies
   * the URL or filename.
   */
  public static void main(String args[])
  {
    FileReader f=null;
    BufferedReader r=null;

    if ( args.length!=1 ) {
      // display usage
      System.out.println("Usage:");
      System.out.println(
        "javac ParseCSV [csv file or URL to parse]");
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
    String a[];
    while ( (a=parseCSVLine(r)) != null ) {
      for ( int i=0;i<a.length;i++ ) {
        System.out.println("Field #"
                           + i + ":" + a[i] );
      }
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


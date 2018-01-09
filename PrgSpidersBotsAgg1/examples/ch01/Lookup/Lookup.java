import java.net.*;

/**
 * Example program from Chapter 1
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 * A simple class used to lookup a host name using either
 * an IP address or a host name and to display the IP
 * address and hostname for this address. This class can
 * be used both to display the IP address for a host name,
 * as well as do a reverse IP lookup and  * give the host
 * name for an IP address.

 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class Lookup {

  /**
   * The main function.
   *
   * @param args The first argument should be the
   * address to lookup.
   */
  public static void main(String[] args)
  {
    try {
      if ( args.length==0 ) {
        System.out.println(
                          "Call with one parameter that specifies the host " +
                          "to lookup.");
      } else {
        InetAddress address = InetAddress.getByName(args[0]);
        System.out.println(address);
      }
    } catch ( Exception e ) {
      System.out.println("Could not find " + args[0] );
    }
  }
}

import com.heaton.bot.translate.*;

/**
 * Example program from Chapter 4
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 *
 * This is just a simple class used to test the
 * Translate class. Not used by this example.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class Test {

  /**
   * Program entry point.
   *
   * @param args No command line paramters are used.
   */
  public static void main(String args[])
  {
    try {
      String str = Translate.translate("http://www.jeffheaton.com","trans.jsp");
      System.out.println( str );
    } catch ( Exception e ) {
    }
  }
}

package com.heaton.bot;
import java.io.*;
import java.util.*;

/**
 * The log class is used to write out log
 * information.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class Log {
  /**
   * Display the greatest amort of log information
   * all info packets will be displayed.
   */
  public final static int LOG_LEVEL_DUMP = 1;
  /**
   *        Display enough information so that the
   *        operation of the program could be traced.
   */
  public final static int LOG_LEVEL_TRACE = 2;
  /**
   * Normal level of logging.
   */
  public final static int LOG_LEVEL_NORMAL= 3;
  /**
   * Log only errors.
   */
  public final static int LOG_LEVEL_ERROR = 4;
  /**
   * Log nothing.
   */
  public final static int LOG_LEVEL_NONE = 5;

  /**
   * Are we logging to the console?
   */
  protected static boolean _log2console = true;
  /**
   * Are we logging to a file.
   */
  protected static boolean _log2file = false;
  /**
   * The path of the log file.
   */
  protected static String _path = "." +
                                  File.pathSeparator + "log.txt";
  /**
   * What level to log to.
   */
  protected static int _level = LOG_LEVEL_NONE;

  /**
   * Construct the log object.
   */
  private Log()
  {
  }

  /**
   * Set the logging level.
   *
   * @param l      The logging level.
   */
  static public void setLevel(int l)
  {
    if ( (l==LOG_LEVEL_TRACE) ||
         (l==LOG_LEVEL_NORMAL) ||
         (l==LOG_LEVEL_NONE) ||
         (l==LOG_LEVEL_DUMP) ||
         (l==LOG_LEVEL_ERROR) ) {
      _level = l;
    } else {
      _level = LOG_LEVEL_NORMAL;
    }
  }

  /**
   * Set the path to log to.
   *
   * @param s      The path to log to.
   */
  static public void setPath(String s)
  {
    _path = s;
  }

  /**
   * Determines if file logging is
   * enabled.
   *
   * @param b
   */
  static public void setFile(boolean b)
  {
    _log2file = b;
  }

  /**
   * Determines if console logging
   * is enabled.
   *
   * @param b      True if console logging is enabled.
   */
  static public void setConsole(boolean b)
  {
    _log2console = b;
  }

  /**
   * Get the current logging level.
   *
   * @return The current logging level.
   */
  static public int getLevel()
  {
    return _level;
  }

  /**
   * The log file path.
   *
   * @return The log file path.
   */
  static public String getPath()
  {
    return _path;
  }

  /**
   * Get if console logging is enabled.
   *
   * @return Returns true if console logging
   *         is enabled.
   */
  static public boolean getConsole()
  {
    return _log2console;
  }

  /**
   * Determine if file logging is being used.
   *
   * @return True if file logging is enabled.
   */
  static public boolean getFile()
  {
    return _log2file;
  }

  /**
   * Log an exception.
   *
   * @param event  The text to describe this log event.
   * @param e      The exception.
   */
  static public void logException(String event,Exception e)
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bos);
    e.printStackTrace(ps);
    ps.close();
    log(LOG_LEVEL_ERROR,event + e + ":" + bos);
    try {
      bos.close();
    } catch ( IOException f ) {
    }
  }

  /**
   * Used to actually log an event.
   *
   * @param level  The level of this event.
   * @param event  The text to be logged.
   */
  synchronized static public void log(int level,String event)
  {
    if ( level == LOG_LEVEL_NONE )
      return;

    if ( level<_level )
      return;

    Date dt = new Date();
    String log = "[" + dt.toString() + "] [";

    switch ( level ) {
    case LOG_LEVEL_TRACE:log+="TRACE";break;
    case LOG_LEVEL_NORMAL:log+="NORMAL";break;
    case LOG_LEVEL_ERROR:log+="ERROR";break;
    case LOG_LEVEL_NONE:log+="NONE?";break;
    case LOG_LEVEL_DUMP:log+="DUMP";break;
    }

    log+="][" + Thread.currentThread().getName() + "] " + event;

    if ( _log2console )
      System.out.println( log );
    if ( _log2file ) {
      try {
        FileOutputStream fw = new FileOutputStream(_path,true);
        PrintStream ps = new PrintStream(fw);
        ps.println(log);
        ps.close();
        fw.close();
      } catch ( IOException e ) {
      }
    }
  }
}

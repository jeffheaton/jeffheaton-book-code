package com.heaton.bot;
import java.util.*;
import java.sql.*;


/**
 * This class uses a JDBC database
 * to store a spider workload.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class SpiderSQLWorkload implements IWorkloadStorable {

  /**
   * The JDBC connection.
   */
  Connection _connection;

  /**
   * A prepared SQL statement to clear the workload.
   */
  PreparedStatement _prepClear;

  /**
   * A prepared SQL statement to assign a workload.
   */
  PreparedStatement _prepAssign;

  /**
   * A prepared SQL statement to get the status of
   * a URL.
   */
  PreparedStatement _prepGetStatus;

  /**
   * A prepared SQL statement to set the status.
   */
  PreparedStatement _prepSetStatus1;

  /**
   * A prepared SQL statement to set the status.
   */
  PreparedStatement _prepSetStatus2;

  /**
   * A prepared SQL statement to set the status.
   */
  PreparedStatement _prepSetStatus3;

  /**
   * Create a new SQL workload store and
   * connect to a database.
   *
   * @param driver The JDBC driver to use.
   * @param source The driver source name.
   * @exception java.sql.SQLException
   * @exception java.lang.ClassNotFoundException
   */
  public SpiderSQLWorkload(String driver, String source)
  throws SQLException, ClassNotFoundException
  {
    Class.forName(driver);
    _connection = DriverManager.getConnection(source);
    _prepClear = _connection.prepareStatement("DELETE FROM tblWorkload;");
    _prepAssign = _connection.prepareStatement("SELECT URL FROM tblWorkload WHERE Status = 'W';");
    _prepGetStatus = _connection.prepareStatement("SELECT Status FROM tblWorkload WHERE URL = ?;");
    _prepSetStatus1 = _connection.prepareStatement("SELECT count(*) as qty FROM tblWorkload WHERE URL = ?;");
    _prepSetStatus2 = _connection.prepareStatement("INSERT INTO tblWorkload(URL,Status) VALUES (?,?);");
    _prepSetStatus3 = _connection.prepareStatement("UPDATE tblWorkload SET Status = ? WHERE URL = ?;");
  }

  /**
   * Call this method to request a URL
   * to process. This method will return
   * a WAITING URL and mark it as RUNNING.
   *
   * @return The URL that was assigned.
   */
  synchronized public String assignWorkload()
  {
    ResultSet rs = null;

    try {
      rs = _prepAssign.executeQuery();

      if ( !rs.next() )
        return null;
      String url = rs.getString("URL");
      setStatus(url,RUNNING);
      return url;
    } catch ( SQLException e ) {
      Log.logException("SQL Error: ",e );
    } finally {
      try {
        if ( rs!=null )
          rs.close();
      } catch ( Exception e ) {
      }
    }
    return null;
  }

  /**
   * Add a new URL to the workload, and
   * assign it a status of WAITING.
   *
   * @param url The URL to be added.
   */
  synchronized public void addWorkload(String url)
  {
    if ( getURLStatus(url)!=UNKNOWN )
      return;
    setStatus(url,WAITING);

  }

  /**
   * Called to mark this URL as either
   * COMPLETE or ERROR.
   *
   * @param url The URL to complete.
   * @param error true - assign this workload a status of ERROR.
   * false - assign this workload a status of COMPLETE.
   */
  synchronized public void completeWorkload(String url,boolean error)
  {
    if ( error )
      setStatus(url,ERROR);
    else
      setStatus(url,COMPLETE);

  }




  /**
   * This is an internal method used to set the status
   * of a given URL. This method will create a record
   * for the URL of one does not currently exist.
   *
   * @param url The URL to set the status for.
   * @param status What status to set.
   */
  protected void setStatus(String url,char status)
  {
    ResultSet rs = null;

    try {
      // first see if one exists
      _prepSetStatus1.setString(1,url);
      rs = _prepSetStatus1.executeQuery();
      rs.next();
      int count = rs.getInt("qty");

      if ( count<1 ) {// Create one
        _prepSetStatus2.setString(1,url);
        _prepSetStatus2.setString(2,(new Character(status)).toString());
        _prepSetStatus2.executeUpdate();
      } else {// Update it
        _prepSetStatus3.setString(1,(new Character(status)).toString());
        _prepSetStatus3.setString(2,url);
        _prepSetStatus3.executeUpdate();
      }
    } catch ( SQLException e ) {
      Log.logException("SQL Error: ",e );
    } finally {
      try {
        if ( rs!=null )
          rs.close();
      } catch ( Exception e ) {
      }
    }
  }


  /**
   * Get the status of a URL.
   *
   * @param url Returns either RUNNING, ERROR
   * WAITING, or COMPLETE. If the URL
   * does not exist in the database,
   * the value of UNKNOWN is returned.
   * @return Returns either RUNNING,ERROR,
   * WAITING,COMPLETE or UNKNOWN.
   */
  synchronized public char getURLStatus(String url)
  {
    ResultSet rs = null;

    try {
      // first see if one exists
      _prepGetStatus.setString(1,url);
      rs = _prepGetStatus.executeQuery();

      if ( !rs.next() )
        return UNKNOWN;

      return rs.getString("Status").charAt(0);
    } catch ( SQLException e ) {
      Log.logException("SQL Error: ",e );
    } finally {
      try {
        if ( rs!=null )
          rs.close();
      } catch ( Exception e ) {
      }
    }
    return UNKNOWN;
  }

  /**
   * Clear the contents of the workload store.
   */
  synchronized public void clear()
  {
    try {
      _prepClear.executeUpdate();
    } catch ( SQLException e ) {
      Log.logException("SQL Error: ",e );
    }
  }
}


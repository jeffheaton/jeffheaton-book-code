package com.heatonresearch.httprecipes.spider.workload.sql.oracle;

import java.sql.*;

import com.heatonresearch.httprecipes.spider.workload.sql.*;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * OracleWorkloadManager: This workload manager is compatible
 * with the Oracle database.
 * 
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class OracleWorkloadManager extends SQLWorkloadManager
{
  public SQLHolder createSQLHolder()
  {
    return new OracleHolder();
  }

  /**
   * Return the size of the specified column.
   *
   * @param table
   *          The table that contains the column.
   * @param column
   *          The column to get the size for.
   * @return The size of the column.
   * @throws SQLException
   *           For SQL errors.
   */
  public int getColumnSize(String table, String column) throws SQLException
  {
    ResultSet rs = this.getConnection().getMetaData().getColumns(null, null,
        table, "%");
    while (rs.next())
    {

      String c = rs.getString("COLUMN_NAME");
      int size = rs.getInt("COLUMN_SIZE");
      if (c.equalsIgnoreCase(column))
      {
        return size;
      }
    }
    return -1;
  }
}

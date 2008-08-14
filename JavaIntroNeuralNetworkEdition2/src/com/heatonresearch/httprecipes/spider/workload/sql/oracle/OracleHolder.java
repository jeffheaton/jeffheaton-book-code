package com.heatonresearch.httprecipes.spider.workload.sql.oracle;

import com.heatonresearch.httprecipes.spider.workload.sql.*;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * OracleHolder: This class is an adaption of the SQLHolder
 * class designed for Oracle.  Oracle uses sequences rather
 * than specific Autonumber types.  The SQL below supports
 * this.
 *
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 *
 * @author Jeff Heaton
 * @version 1.1
 */
public class OracleHolder extends SQLHolder
{
  public String getSQLAdd()
  {
    return "INSERT INTO spider_workload(workload_id,host,url,status,depth,url_hash,source_id) VALUES(spider_workload_seq.NEXTVAL,?,?,?,?,?,?)";
  }

  public String getSQLAdd2()
  {
    return "INSERT INTO spider_host(host_id,host,status,urls_done,urls_error) VALUES(spider_host_seq.NEXTVAL,?,?,?,?)";
  }
}

package com.heatonresearch.httprecipes.spider.workload.sql;

/**
 * The Heaton Research Spider 
 * Copyright 2007 by Heaton Research, Inc.
 * 
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * SQLHolder: This class holds the SQL statements used
 * by the Heaton Research Spider.  This is the generic 
 * set that should work with most DBMS systems.  However
 * you will also find customized versions of this class
 * to support specific DBMS systems, when it is needed.
 *
 *
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 *
 * @author Jeff Heaton
 * @version 1.1
 */
public class SQLHolder
{

  public String getSQLClear()
  {
    return "DELETE FROM spider_workload";
  }

  public String getSQLClear2()
  {
    return "DELETE FROM spider_host WHERE status <> 'I'";
  }

  public String getSQLAdd()
  {
    return "INSERT INTO spider_workload(host,url,status,depth,url_hash,source_id) VALUES(?,?,?,?,?,?)";
  }

  public String getSQLAdd2()
  {
    return "INSERT INTO spider_host(host,status,urls_done,urls_error) VALUES(?,?,?,?)";
  }

  public String getSQLGetWork()
  {
    return "SELECT workload_id,URL FROM spider_workload WHERE status =  ? AND host = ?";
  }

  public String getSQLGetWork2()
  {
    return "UPDATE spider_workload SET status =  ? WHERE workload_id = ?";
  }

  public String getSQLWorkloadEmpty()
  {
    return "SELECT COUNT(*) FROM spider_workload WHERE status in ('P','W') AND host =  ?";
  }

  public String getSQLSetWorkloadStatus()
  {
    return "UPDATE spider_workload SET status =  ? WHERE workload_id =  ?";
  }

  public String getSQLSetWorkloadStatus2()
  {
    return "UPDATE spider_host SET urls_done =  urls_done + ?, urls_error =  urls_error + ? WHERE host =  ?";
  }

  public String getSQLGetDepth()
  {
    return "SELECT url,depth FROM spider_workload WHERE url_hash =  ?";
  }

  public String getSQLGetSource()
  {
    return "SELECT w.url,s.url FROM spider_workload w,spider_workload s WHERE w.source_id =  s.workload_id AND w.url_hash =  ?";
  }

  public String getSQLResume()
  {
    return "SELECT distinct host FROM spider_workload WHERE status =  'P'";
  }

  public String getSQLResume2()
  {
    return "UPDATE spider_workload SET status =  'W' WHERE status =  'P'";
  }

  public String getSQLGetWorkloadID()
  {
    return "SELECT workload_id,url FROM spider_workload WHERE url_hash =  ?";
  }

  public String getSQLGetHostID()
  {
    return "SELECT host_id,host FROM spider_host WHERE host =  ?";
  }

  public String getSQLGetNextHost()
  {
    return "SELECT host_id,host FROM spider_host WHERE status =  ?";
  }

  public String getSQLSetHostStatus()
  {
    return "UPDATE spider_host SET status =  ? WHERE host_id =  ?";
  }

  public String getSQLGetHost()
  {
    return "SELECT host FROM spider_host WHERE host_id =  ?";
  }

}

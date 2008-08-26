// The Heaton Research Spider for .Net 
// Copyright 2007 by Heaton Research, Inc.
// 
// From the book:
// 
// HTTP Recipes for C# Bots, ISBN: 0-9773206-7-7
// http://www.heatonresearch.com/articles/series/20/
// 
// This class is released under the:
// GNU Lesser General Public License (LGPL)
// http://www.gnu.org/copyleft/lesser.html
//
using System;
using System.Collections.Generic;
using System.Text;
using HeatonResearch.Spider.Workload.SQL;

namespace HeatonResearch.Spider.Workload.SQL.Oracle
{
    /// <summary>
    /// OracleHolder: Contains SQL statements customized to Oracle.
    /// </summary>
    class OracleHolder:SQLHolder
    {
        public override String getSQLAdd()
        {
            return "INSERT INTO spider_workload(workload_id,host,url,status,depth,url_hash,source_id) VALUES(spider_workload_seq.NEXTVAL,?,?,?,?,?,?)";
        }

        public override String getSQLAdd2()
        {
            return "INSERT INTO spider_host(host_id,host,status,urls_done,urls_error) VALUES(spider_host_seq.NEXTVAL,?,?,?,?)";
        }
    }
}

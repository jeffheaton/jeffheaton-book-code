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
using System.Data.Common;
using System.Data;

namespace HeatonResearch.Spider.Workload.SQL.Oracle
{
    /// <summary>
    /// OracleWorkloadManager: Contains a workload manager 
    /// customized to Oracle.
    /// </summary>
    class OracleWorkloadManager : SQLWorkloadManager
    {
        /// <summary>
        /// Creates an instance of the OracleHolder class.
        /// </summary>
        /// <returns>An SQLHolder derived object.</returns>
        public override SQLHolder CreateSQLHolder()
        {
            return new OracleHolder();
        }


        /// <summary>
        /// Return the size of the specified column.  Oracle
        /// requires the "%" parameter to be specified.
        /// </summary>
        /// <param name="table">The table that contains the column.</param>
        /// <param name="column">The column to get the size for.</param>
        /// <returns>The size of the column.</returns>
        public override int GetColumnSize(String table, String column)
        {
            String[] restriction = { null, null, table, "%" };
            DataTable dt = this.Connection.GetSchema("Columns", restriction);
            foreach (System.Data.DataRow row in dt.Rows)
            {

                if (String.Compare(row["column_name"].ToString(), column, true) == 0)
                {
                    return int.Parse(row["column_size"].ToString());
                }
            }

            return -1;
        }
    }
}

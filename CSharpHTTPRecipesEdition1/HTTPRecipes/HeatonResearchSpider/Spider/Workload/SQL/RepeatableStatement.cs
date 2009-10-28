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
using System.Threading;

using HeatonResearch.Spider.Logging;



namespace HeatonResearch.Spider.Workload.SQL
{
    /// <summary>
    /// RepeatableStatement: Holds a SQL command that can
    /// be repeated if the SQL connection fails.  
    /// </summary>
    class RepeatableStatement
    {
        /// <summary>
        /// The results of a query.
        /// </summary>
        public class Results
        {
            /// <summary>
            /// The DataReader associated with these results.
            /// </summary>
            public DbDataReader DataReader
            {
                get
                {
                    return resultSet;
                }
            }

            /// <summary>
            /// The PreparedStatement that generated these results.
            /// </summary>
            private DbCommand statement;

            /// <summary>
            /// The ResultSet that was generated.
            /// </summary>
            private DbDataReader resultSet;

            /// <summary>
            /// The RepeatableStatement object that this belongs to.
            /// </summary>
            private RepeatableStatement parent;

            /// <summary>
            /// Construct a Results object.
            /// </summary>
            /// <param name="parent">The parent object, the RepeatableStatement.</param>
            /// <param name="statement">The PreparedStatement for these results.</param>
            /// <param name="resultSet">The ResultSet.</param>
            public Results(RepeatableStatement parent, DbCommand statement, DbDataReader resultSet)
            {
                this.statement = statement;
                this.resultSet = resultSet;
                this.parent = parent;
            }

            /// <summary>
            /// Close the ResultSet.
            /// </summary>
            public void Close()
            {
                this.resultSet.Close();
                parent.ReleaseStatement(this.statement);
            }

        }

     
        /// <summary>
        /// The SQLWorkloadManager that created this object.
        /// </summary>
        private SQLWorkloadManager manager;


        /// <summary>
        /// The SQL for this statement.
        /// </summary>
        private String sql;


        /// <summary>
        /// The PreparedStatements that are assigned to each thread.
        /// </summary>
        private List<DbCommand> statementCache = new List<DbCommand>();


        /// <summary>
        /// Construct a repeatable statement based on the specified SQL. 
        /// </summary>
        /// <param name="sql">The SQL to base this statement on.</param>
        public RepeatableStatement(String sql)
        {
            this.sql = sql;
        }


        /// <summary>
        /// Close the statement.
        /// </summary>
        public void Close()
        {
            try
            {
                Monitor.Enter(this);


                foreach (DbCommand statement in this.statementCache)
                {

                    statement.Dispose();

                }
            }
            finally
            {
                Monitor.Exit(this);
            }
        }


        /// <summary>
        /// Create the statement, so that it is ready to assign
        /// PreparedStatements.
        /// </summary>
        /// <param name="manager">The manager that created this statement.</param>
        public void Create(SQLWorkloadManager manager)
        {
            Close();
            this.manager = manager;
        }


        /// <summary>
        /// Execute SQL that does not return a result set. If an
        /// error occurs, the statement will be retried until it is
        /// successful. This handles broken connections.
        /// </summary>
        /// <param name="parameters">The parameters for this SQL.</param>
        public void Execute(params Object[] parameters)
        {
            DbCommand statement = null;

            try
            {
                statement = ObtainStatement();

                for (; ; )
                {
                    try
                    {
                        statement.Parameters.Clear();
                        foreach (Object parameter in parameters)
                        {
                            DbParameter dbParam = statement.CreateParameter();

                            if (parameter == null)
                            {
                                dbParam.Value = DBNull.Value;
                            }
                            else
                            {
                                dbParam.Value = parameter;
                            }
                            statement.Parameters.Add(dbParam);
                        }

                        statement.ExecuteNonQuery();
                        return;
                    }
                    catch (Exception e)
                    {
                        this.manager.Spider.Logging.Log(Logger.Level.ERROR,
                       "SQL Exception", e);

                        this.manager.TryOpen();
                    }
                }
            }
            catch (Exception e)
            {
                throw (new WorkloadException(e));
            }
            finally
            {
                if (statement != null)
                {
                    ReleaseStatement(statement);
                }
            }
        }


        /// <summary>
        /// Execute an SQL query that returns a result set. If an
        /// error occurs, the statement will be retried until it is
        /// successful. This handles broken connections.
        /// </summary>
        /// <param name="parameters">The parameters for this SQL.</param>
        /// <returns>The results of the query.</returns>
        public Results ExecuteQuery(params Object[] parameters)
        {
            DbCommand statement = null;
            try
            {
                statement = ObtainStatement();

                for (; ; )
                {
                    try
                    {
                        statement.Parameters.Clear();
                        foreach (Object parameter in parameters)
                        {
                            DbParameter dbParam = statement.CreateParameter();

                            if (parameter == null)
                            {
                                dbParam.Value = DBNull.Value;
                            }
                            else
                            {
                                dbParam.Value = parameter;
                            }
                            statement.Parameters.Add(dbParam);
                        }

                        DbDataReader reader = statement.ExecuteReader();
                        Results results = new Results(this, statement, reader);
                        return results;
                    }
                    catch (Exception e)
                    {
                        this.manager.Spider.Logging.Log(Logger.Level.ERROR,
                         "SQL Exception", e);

                        this.manager.TryOpen();
                    }
                }
            }
            catch (Exception e)
            {
                throw (new WorkloadException(e));
            }

        }

        /// <summary>
        /// Obtain a statement. Each thread should use their own
        /// statement, and then call the releaseStatement method
        /// when they are done.
        /// </summary>
        /// <returns>A PreparedStatement object.</returns>
        private DbCommand ObtainStatement()
        {
            DbCommand result = null;

            try
            {
                Monitor.Enter(this);
                if (this.statementCache.Count == 0)
                {
                    result = this.manager.Connection.CreateCommand();
                    result.CommandText = this.sql;
                    result.Prepare();
                }
                else
                {
                    result = this.statementCache[0];
                    this.statementCache.Remove(result);
                    result.Parameters.Clear();
                }

            }
            finally
            {
                Monitor.Exit(this);
            }

            return result;
        }

        /// <summary>
        /// This method releases statements after the thread is
        /// done with them. These statements are not closed, but
        /// rather cached until another thread has need of them.
        /// </summary>
        /// <param name="stmt">The statement that is to be released.</param>
        private void ReleaseStatement(DbCommand stmt)
        {
            try
            {
                Monitor.Enter(this);
                if( !this.statementCache.Contains(stmt))
                    this.statementCache.Add(stmt);
            }
            finally
            {
                Monitor.Exit(this);
            }
        }
    }
}

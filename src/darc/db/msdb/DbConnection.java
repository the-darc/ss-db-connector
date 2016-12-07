package darc.db.msdb;

//import java.sql.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

//import com.microsoft.sqlserver.jdbc.*;

import darc.db.msdb.DbConnectionBean;
import darc.db.msdb.DbException;

/**
 * Facade Class to expose query functions of java.sql.*
 * 
 * Attention: Requires the specific JDBC driver to work.
 * For MS SQL Server JDBC Driver: @see https://msdn.microsoft.com/en-us/library/mt484311(v=sql.110).aspx
 * 
 * @author danielcampos
 */
public class DbConnection {

	private DbConnectionBean connectionBean;
	private Connection connection;
	private Statement statement;
	private Exception lastException;

	public DbConnection(DbConnectionBean connectionBean) {
		super();
		this.connectionBean = connectionBean;
	}
	
	/**
	 * Try to establish a connection with the database
	 * @return The same current instance of DbConnection
	 * @throws SQLException If it fails to check for existent connections
	 * @throws DbException If it fails to create a new connection
	 */
	public DbConnection connect() throws SQLException, DbException {
		if (this.isConnected()) {
			return this;
		}
		this.disconnect();

		try {
			String connectionString = this.connectionBean.getConnectionString();
			this.connection = DriverManager.getConnection(connectionString);
			this.statement = this.connection.createStatement();
		} catch (SQLException e) {
			this.disconnect();
			this.lastException = e;
			throw e;
		}

		return this;
	}
	
	
	/**
	 * Close the current connection with the database.
	 * Do nothing if there isn't a connection opened.
	 * @return The same current instance of DbConnection
	 */
	public DbConnection disconnect() {
		if (this.connection != null) {
			try { connection.close(); } catch(Exception e) {}
		}
		if (statement != null) {
			try { statement.close(); } catch (Exception e) {}
		}
		return this;
	}

	/**
	 * Check if it is connected to the database
	 * @return <code>True</code> if connected and <code>False</code> if not connected
	 * @throws SQLException If it fails to check for existent connections
	 */
	public boolean isConnected() throws SQLException {
		return this.statement != null && !this.statement.isClosed() &&
				this.connection != null && !this.connection.isClosed() && this.connection.isValid(5);
	}

	/**
	 * Cancels the current execution if both the DBMS and driver support aborting an SQL statement.
	 * @throws SQLException If it fails to check for existent connections
	 */
	public void cancelCurrentExecution() throws SQLException {
		if (this.isConnected()) {
			this.statement.cancel();
		}
	}
	
	/**
	 * Executes the given SQL statement, which returns a single ResultSet object
	 * @param selectSql An SQL Select statement to be sent to the database 
	 * @return A ResultSet object that contains the data produced by the given query (never null) 
	 * @throws SQLException if a database access error occurs
	 * @throws DbException if it is not connected to the database
	 */
	public ResultSet runSelect(String selectSql) throws SQLException, DbException {
		if (!this.isConnected()) {
			throw new DbException("Not connected. You must connect in a database before call 'DbConnection#runSelect()' method");
		}
		return this.statement.executeQuery(selectSql);
	}
	
	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or
	 * an SQL statement that returns nothing, such as an SQL DDL statement.
	 * @param sqlStatement An SQL Data Manipulation Language (DML) statement, such as INSERT, UPDATE or DELETE;
	 *                     or an SQL statement that returns nothing, such as a DDL statement.
	 * @return The row count for SQL Data Manipulation Language (DML) statements; or 0 for SQL statements that return nothing
	 * @throws SQLException if a database access error occurs
	 * @throws DbException if it is not connected to the database
	 */
	public int run(String sqlStatement) throws SQLException, DbException {
		if (!this.isConnected()) {
			throw new DbException("Not connected. You must connect in a database before call 'DbConnection#run()' method");
		}
		return this.statement.executeUpdate(sqlStatement);
	}

	public Exception getLastException() {
		return lastException;
	}
}

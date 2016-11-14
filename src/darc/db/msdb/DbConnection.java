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
	private Exception e;

	public DbConnection(DbConnectionBean connectionBean) {
		super();
		this.connectionBean = connectionBean;
	}
	
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
			this.e = e;
			throw e;
		}

		return this;
	}
	
	
	
	public DbConnection disconnect() {
		if (this.connection != null) {
			try { connection.close(); } catch(Exception e) {}
		}
		if (statement != null) {
			try { statement.close(); } catch (Exception e) {}
		}
		return this;
	}

	public boolean isConnected() throws SQLException {
		return this.statement != null && !this.statement.isClosed() &&
				this.connection != null && !this.connection.isClosed() && this.connection.isValid(5);
	}

	public void cancelCurrentExecution() throws SQLException {
		if (this.isConnected()) {
			this.statement.cancel();
		}
	}
	
	public ResultSet runSelect(String selectSql) throws SQLException, DbException {
		if (!this.isConnected()) {
			throw new DbException("Not connected. You must connect in a database before call 'DbConnection#runSelect()' method");
		}
		return this.statement.executeQuery(selectSql);
	}

	public Exception getLastException() {
		return e;
	}
}

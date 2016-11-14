package darc.connector;

import darc.db.msdb.DbConnection;
import darc.db.msdb.DbConnectionBean;

/**
 * Factory Class to create {@link DbConnection} instances
 * 
 * Use it as a Sydle SEED connector to simplifly the access
 * to a DataBase from Sydle SEED proccesses. It works as a facade
 * exposing the mainly functionalities of "java.sql.*" library.
 * 
 * Attention: Requires the specific JDBC driver to work.
 * For MS SQL Server JDBC Driver: @see https://msdn.microsoft.com/en-us/library/mt484311(v=sql.110).aspx
 * 
 * @author danielcampos
 */
public class ConnectionFactory {	
	public ConnectionFactory() {
		super();
	}

	/**
	 * Create a new DataBase connection based on the passed Data Base connection
	 * configuration bean.
	 * @param connectionBean A Data Base connection configuration bean
	 * @return A new instance of a connection with the data base
	 */
	public DbConnection createDbConnectionFromBean(DbConnectionBean connectionBean) {
		return new DbConnection(connectionBean);
	}
	
	/**
	 * Create a new DataBase connection based on the passed connection properties.
	 * @param host Host name of the connection (ex.: "yourserver.database.windows.net")
	 * @param database database name (ex.: "AdventureWorks")
	 * @param user user (ex.: "yourusername@yourserver")
	 * @param pwd password (ex.: "yourpassword")
	 * @return A new instance of a connection with the data base
	 */
	public DbConnection createDbConnection(String host, String database, String user, String pwd) {
		return new ConnectionFactory().createDbConnectionFromBean(new DbConnectionBean(host, database, user, pwd));
	}
}

package darc.db.msdb;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Connection Configuration Bean for MSSQL
 * 
 * Manage the database connection configuration to generate a valid StringConnection
 * 
 * MSSQL StringConnection example:
 *   jdbc:sqlserver://yourserver.database.windows.net:1433;database=AdventureWorks;
 *     user=yourusername@yourserver;password=yourpassword;encrypt=true;
 *     trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
 *
 * @author danielcampos
 */
public class DbConnectionBean {

	private final HashMap<String, String> config;
	
	/**
	 * Create a new connection configuration bean
	 * @param host Host name of the connection (ex.: "yourserver.database.windows.net")
	 * @param database database name (ex.: "AdventureWorks")
	 * @param user user (ex.: "yourusername@yourserver")
	 * @param pwd password (ex.: "yourpassword")
	 */
	public DbConnectionBean(String host, String database, String user, String pwd) {
		config = new HashMap<String, String>();
		this.set("host", host);
		this.set("database", database);
		this.set("user", user);
		this.set("password", pwd);
	}
	public DbConnectionBean(String host, String database, String user) {
		this(host, database, user, null);
	}
	public DbConnectionBean(String host, String database) {
		this(host, database, null, null);
	}
	public DbConnectionBean(String host) {
		this(host, null, null, null);
	}
	public DbConnectionBean() {
		this(null, null, null, null);
	}
	
	/**
	 * Set an genneric connection configuration
	 * @param key The configuration name (Ex.: "port")
	 * @param value The configuration value (Ex.: "1433")
	 * @return This connection bean
	 */
	public DbConnectionBean set(String key, String value) {
		if (value != null) {
			this.config.put(key, value);
		}
		return this;
	}
	
	/**
	 * Alias for: connectionBeam.set("user", <userName>);
	 */
	public DbConnectionBean setUser(String user) {
		this.config.put("user", user);
		return this;
	}

	/**
	 * Alias for: connectionBeam.set("databse", <databse>);
	 */
	public DbConnectionBean setDatabase(String database) {
		this.config.put("database", database);
		return this;
	}

	/**
	 * Alias for: connectionBeam.set("password", <password>);
	 */
	public DbConnectionBean setPassword(String password) {
		this.config.put("password", password);
		return this;
	}

	public String getConnectionString() throws DbException {
		if (this.config.get("host") == null) {
			throw new DbException("Invalid database connection configuration: Db host not informed.");
		}
		
		String host = this.config.get("host");
		String port = this.config.get("port");
		port = port == null ? "1433" : port; 
		
		String connectionString = String.format("jdbc:sqlserver://%s:%s;", host, port);
		
	    Set<Entry<String, String>> keys = this.config.entrySet();
	    for(Entry<String, String> c : keys) {
	    	if (c.getKey() != "host" && c.getKey() != "port") {
	    		connectionString += c.getKey() + "=" + c.getValue() + ";";
	    	}
	    }
		
		return connectionString;
	}
}

package darc;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import darc.db.msdb.DbConnection;
import darc.db.msdb.DbConnectionBean;
import darc.db.msdb.DbException;

public class Main {
	public static void main(String[] args) throws SQLException, DbException, IOException {
		PropertiesLoader properties = new PropertiesLoader();
		String host = properties.get("host");
		String database = properties.get("database");
		String user = properties.get("user");
		String pwd = properties.get("pwd");

		DbConnectionBean connectionBean = new DbConnectionBean(host).setUser(user).setDatabase(database).setPassword(pwd);
		DbConnection connection = new DbConnection(connectionBean).connect();

		ResultSet resultSet;

		// Run the first query 
		String query1 = "SELECT COUNT(1) FROM treinamentoseed.ProductType;";
		resultSet = connection.runSelect(query1);
		System.out.println(query1);
		System.out.println("Cloumns found: " + resultSet.getMetaData().getColumnCount());
		if (resultSet.isBeforeFirst()) {
			resultSet.next();
			System.out.println("Count result: " + resultSet.getString(1));
		}
		System.out.println();
		
		// Run the second query
		String query2 = "SELECT * FROM treinamentoseed.ProductType;";
		resultSet = connection.runSelect(query2);
		System.out.println(query2);
		System.out.println("Cloumns found: " + resultSet.getMetaData().getColumnCount());
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
		}
	}
}

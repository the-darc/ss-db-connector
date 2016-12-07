package darc;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import darc.connector.ConnectionFactory;
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

		// Run the query
		String query1 = "SELECT * FROM treinamentoseed.ProductType;";
		resultSet = connection.runSelect(query1);
		System.out.println(query1);
		System.out.println("Cloumns found: " + resultSet.getMetaData().getColumnCount());
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
		}
		
		// Run the count query 
		System.out.println();
		String query2 = "SELECT COUNT(1) FROM treinamentoseed.ProductType;";
		resultSet = connection.runSelect(query2);
		System.out.println(query2);
		System.out.println("Cloumns found: " + resultSet.getMetaData().getColumnCount());
		if (resultSet.isBeforeFirst()) {
			resultSet.next();
			System.out.println("Count result: " + resultSet.getString(1));
		}
		
		// Run insert query
		System.out.println();
		DbConnection dbConnection = new ConnectionFactory().createDbConnection(host, database, user, pwd).connect();
		int insertedValues = dbConnection.run("INSERT INTO treinamentoseed.ProductType VALUES ('teste1 insert', 'teste de insert pelo connector');");
		System.out.println("Registros inseridos: " + insertedValues + "\n");
		insertedValues = dbConnection.run("INSERT INTO treinamentoseed.ProductType VALUES ('teste2 insert', 'teste2 de insert pelo connector');");
		System.out.println("Registros inseridos: " + insertedValues + "\n");
		
		// Print new table content
		System.out.println();
		resultSet = dbConnection.runSelect(query1);
		System.out.println(query1);
		System.out.println("Cloumns found: " + resultSet.getMetaData().getColumnCount());
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
		}
		
		// Run update query
		System.out.println();
		int updatedValues = dbConnection.run("UPDATE treinamentoseed.ProductType SET description='updated' where name like '%insert%';");
		System.out.println("Registros atualizados: " + updatedValues + "\n");
		
		// Print new table content
		System.out.println();
		resultSet = dbConnection.runSelect(query1);
		System.out.println(query1);
		System.out.println("Cloumns found: " + resultSet.getMetaData().getColumnCount());
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3));
		}
		
		// Delete inserted lines
		System.out.println();
		int removedValues = dbConnection.run("DELETE FROM treinamentoseed.ProductType WHERE name like '%insert%';");
		System.out.println("Registros removidos: " + removedValues + "\n");
	}
}

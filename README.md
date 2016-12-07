# ss-db-connector

A Java Connector for connection with external databases from a [Sydle Seed](http://www.sydle.com/br/bpm/) process

## How to use it

### Adding the connector in Sydle SEED

The [Sydle SEED](http://www.sydle.com/br/bpm/) Connector could be downloaded in [the-darc/ss-db-connector/release/ss-db-connector.jar](https://github.com/the-darc/ss-db-connector/blob/master/release/ss-db-connector.jar).

To use it is necessary to create a new Java Connector in Sydle SEED adding the files:

 - ss-db-connector.jar _Package Jar of this library_
 - sqljdbc4.jar _JDBC connector to Microsoft SQL Server_
 
To use it just create a Java Connector in Sydle SEED:

1. Create a new Java Connector in Sydle SEED
2. Add the ss-db-connector.jar and the jdbc jar  in the new connector
3. In the field _Qualified class name_ of the new connector form use the value _**darc.connector.ConnectionFactory**_

If you need help in how to create the Java Connector see [Help Sydle SEED - Integrate with external systems](https://secure.sydle.com/seed/cm/help/en/using/modelingAndAutomation/integrateWithExternalSystems.html?q=conector%20java).

### Using it in a expression

To use it in your process create a **service task** with the created connector and write a expression like the example bellow:

``` javascript
var host = "host.for.the.sqlserver";
var database = "databasename";
var user = "username";
var pwd = "user.password";

var conn = dbConnector.createDbConnection(host, database, user, pwd);
conn.connect();

// Insert two regs
var countInserted = conn.run("INSERT INTO treinamentoseed.ProductType VALUES ('teste1 insert', 'teste de insert pelo connector');");
countInserted += conn.run("INSERT INTO treinamentoseed.ProductType VALUES ('teste2 insert', 'teste de insert pelo connector');");
Utils.debug('Count inserted: ' + countInserted);

// List all regs in the table
var result = conn.runSelect("SELECT * FROM treinamentoseed.ProductType;");
while(result.next()) {
  var line = {
    id: result.getInt('id'),
    nome: result.getString('name'),
    descricao: result.getString('description')
  };
  Utils.debug(line);
  seed.tiposprodutos.push(line);
}

// Update inserted regs
var countUpdated = conn.run("UPDATE treinamentoseed.ProductType SET description='updated' WHERE name like '%insert';");
Utils.debug('Count updated: ' + countUpdated);

// Remove inserted regs
var countRemoved = conn.run("DELETE FROM treinamentoseed.ProductType WHERE name like '%insert%';");
Utils.debug('Count removed: ' + countRemoved);
``` 

## DbConnector API

### dbConnector.createDbConnection(string, string, string, string)

Create a new DataBaseConnection based on the passed connection properties.

**Parameters:**

- **host:** Host name of the connection (ex.: "yourserver.database.windows.net")
- **database:** database name (ex.: "AdventureWorks")
- **user:** user (ex.: "yourusername@yourserver")
- **pwd:** password (ex.: "yourpassword")

**Return:**

A new instance of the DbConnection with the specific data base connection configuration

### DbConnection.connect()

Try to establish a connection with the database

**Return:**

The same current instance of DbConnection, but now connected to the database.

**Throws:**

- **SQLException:** If it fails to check for existent connections
- **DbException:** If it fails to create a new connection

### DbConnection.runSelect(string)

Executes the given SQL statement, which returns a single [ResultSet](https://docs.oracle.com/javase/6/docs/api/java/sql/ResultSet.html) object

**Parameters:**

- **selectSql:** An SQL Select statement to be sent to the database

**Return:**

A Java [ResultSet](https://docs.oracle.com/javase/6/docs/api/java/sql/ResultSet.html) object that contains the data produced by the given query (never null)

**Throws:**

- **SQLException:** if a database access error occurs
- **DbException:** if it is not connected to the database

### DbConnection.run(string)

Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement.

**Parameters:**

- **sqlStatement:** An SQL Data Manipulation Language (DML) statement, such as INSERT, UPDATE or DELETE; or an SQL statement that returns nothing, such as a DDL statement.

**Return:**

The row count for SQL Data Manipulation Language (DML) statements; or 0 for SQL statements that return nothing.

**Throws:**

- **SQLException:** if a database access error occurs
- **DbException:** if it is not connected to the database

### DbConnection.isConnected()

Check if it is connected to the database.

**Return:**

`True` if it is connected and `False` if it is not.

**Throws:**

- **SQLException:** If it fails to check for existent connections

### DbConnection.disconnect()

Close the current connection with the database. Do nothing if there isn't a connection opened.

**Return:**

The same current instance of DbConnection.

### DbConnection.cancelCurrentExecution()

Cancels the current execution if both the DBMS and driver support aborting an SQL statement.

**Throws:**

- **SQLException:** If it fails to check for existent connections

## Contributting

Clone this repository and import it in eclipse as a java project.

You can test the code running the `src.darc.Main.java` class. 

### TODO List

 - Add support to others databases like _Oracle_, _MySQL_ and others... 
 - Add docs to help in the use of this connector with the `DbConnectionBean` class
 - Add some unity tests
 

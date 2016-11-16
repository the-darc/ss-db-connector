# ss-db-connector

A Java Connector for connection with external databases from a [Sydle Seed](http://www.sydle.com/br/bpm/) process

## How to use it

### Adding the connector in Sydle SEED

The [Sydle SEED](http://www.sydle.com/br/bpm/) Connector could be downloaded in [the-darc/ss-db-connector/release/ss-db-connector.jar](https://github.com/the-darc/ss-db-connector/blob/master/release/ss-db-connector.jar).

To use it is necessary to create a new Java Connector in Sydle SEED adding the files:

 - ss-db-connector.jar _Package Jar of this library_
 - sqljdbc4.jar _JDBC connector to Microsoft SQL Server_

If you need help in how to create the Java Connector see [Help Sydle SEED - Integrate with external systems](https://secure.sydle.com/seed/cm/help/en/using/modelingAndAutomation/integrateWithExternalSystems.html?q=conector%20java).

### Using it in a expression

To use it in your process create a **service task** with the created connector and write a expression like the example bellow:

``` javascript
var host = "host.forthe.sqlserver";
var database = "databasename";
var user = "username";
var pwd = "user.password";

var conn = dbConnector.createDbConnection(host, database, user, pwd);
conn.connect();

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
``` 

## Contributting

Clone this repository and import it in eclipse as a java project.

You can test the code running the `src.darc.Main.java` class. You are welcome to add some unity tests if you wish. 

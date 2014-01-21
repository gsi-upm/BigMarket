package simulation.util;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;



public class Neo4JManageTool {
	
	public static final String DB_PATH = "/home";
	
	GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);

	Transaction tx = graphDB.beginTx();
	
	
	
}

package simulation.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;



public class Neo4JManageTool {
	
	public static final String DB_PATH = "/home/dlara/Neo4J195/data/graph.db/neostore";
	private Logger logger = Logger.getLogger(this.getClass().getName());
	GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;
	
	private static enum RelTypes implements RelationshipType{
	    FOLLOW
	}
	
	public void launchDatabaseTool(){
        createDb();
        //removeData();
        shutDown();
	}
	
	void createDb(){
		clearDb();
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		registerShutdownHook( graphDb );

		try ( Transaction tx = graphDb.beginTx() ){
			firstNode = graphDb.createNode();
			firstNode.setProperty( "message", "Hello, " );
			secondNode = graphDb.createNode();
			secondNode.setProperty( "message", "World!" );

			relationship = firstNode.createRelationshipTo( secondNode, RelTypes.FOLLOW );
			relationship.setProperty( "message", "brave Neo4j " );

			tx.success();
		}

	}

	private void clearDb(){
		try{
			FileUtils.deleteRecursively( new File( DB_PATH ) );
		}
		catch ( IOException e ){
			throw new RuntimeException( e );
		}
	}

	void removeData(){
		try ( Transaction tx = graphDb.beginTx() ){

			firstNode.getSingleRelationship( RelTypes.FOLLOW, Direction.OUTGOING ).delete();
			firstNode.delete();
			secondNode.delete();

			tx.success();
		}
	}

	void shutDown(){
		logger.info("Shutting down database...");

		graphDb.shutdown();
		logger.info("Database sucessfuly shutting down");

	}


	private static void registerShutdownHook( final GraphDatabaseService graphDb ){

		Runtime.getRuntime().addShutdownHook( new Thread(){
			@Override
			public void run(){
				graphDb.shutdown();
			}
		} 	);
	}
	
}


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

import simulation.Simulation;
import simulation.model.User;



public class Neo4JManageTool {
	
	public static final String DB_PATH = "/home/dlara/neo4j/data/graph.db";
	public String path;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;
    private Simulation sim;
    
	public Neo4JManageTool(Simulation sim){
		this.sim = sim;
	}
    
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
		if(path == null){
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		}else{
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path);
		}
			registerShutdownHook( graphDb );

		try ( Transaction tx = graphDb.beginTx() ){
			for(User u : sim.getUsers()){
				logger.info("Creating user " + u.getUserName());
				Node nNeo = graphDb.createNode();
				nNeo.setProperty("ID", u.getUserID());
				nNeo.setProperty("Name", u.getUserName());
				logger.info("User " + u.getUserName() + " created");
			}

			for(User u : sim.getUsers()){
				for(User user : u.getFollowed()){
					Node origin = graphDb.getNodeById(u.getId());
					Node target = graphDb.getNodeById(user.getId());
					Relationship rel = origin.createRelationshipTo(target, RelTypes.FOLLOW);
				}
			}


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

	public void setPath(String s){
		this.path = s;
	}
	
	public String getPath(){
		return this.path;
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


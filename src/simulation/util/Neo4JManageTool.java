package simulation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.neo4j.graphdb.RelationshipType;
import simulation.Simulation;
import simulation.model.User;



public class Neo4JManageTool {
	
	public static final String DB_PATH = "/home/dlara/neo4j/data/graph.db";
	public final String SERVER_ROOT_URI = "http://localhost:7474";
	public final String DATASET_NAMES_PATH = "/home/dlara/Gitted/WebContent/datasets";
	public String path;
	private Logger logger = Logger.getLogger(this.getClass().getName());
    public HashMap<String, List<User>> relations;
    public HashMap<String, String> nodeUris;
    private Simulation sim;
    
	public Neo4JManageTool(Simulation sim){
		this.sim = sim;
		this.relations = new HashMap<>();
		this.nodeUris = new HashMap<>();
	}
	
	public Neo4JManageTool(){
		this.relations = new HashMap<>();
		this.nodeUris = new HashMap<>();
	}
	
	public void launchDatabaseTool(){
		createNodes();
		createRealtions();
	}
			
	private void createNodes(){
		String location = null;
		nodeUris.clear();
		System.out.println("SIMULATION NODES : " + sim.getGraphManager().getGraph().getNodeCount());
		for(org.graphstream.graph.Node n : sim.getGraphManager().getGraph().getNodeSet()){
			System.out.println("NODE " + n.getId());
			try{
				String nodePointUrl = this.SERVER_ROOT_URI + "/db/data/node/";
				String dataset = "dataset";
				HttpClient client = new HttpClient();
				PostMethod mPost = new PostMethod(nodePointUrl);
			
				Header mtHeader = new Header();
				mtHeader.setName("content-type");
				mtHeader.setValue("application/json");
				mtHeader.setName("accept");
				mtHeader.setValue("application/json");
				mPost.addRequestHeader(mtHeader);
			
				StringRequestEntity requestEntity = new StringRequestEntity("{}",
                                                                        "application/json",
                                                                        "UTF-8");
				mPost.setRequestEntity(requestEntity);
				client.executeMethod(mPost);
				mPost.getResponseBodyAsString( );
				Header locationHeader =  mPost.getResponseHeader("location");
				location = locationHeader.getValue();
				mPost.releaseConnection( );
				
				String data = sim.getSimDataset();
				
				nodeUris.put(n.getId(), location);
				
				
				
				this.saveNodeRelations(location, sim.getUsers().get(n.getIndex()).getFollowers());
				this.addProperty(location, dataset, data);
				
			}catch(Exception e){
				System.out.println("Exception in creating node in neo4j : " + e);
			}
		}
		
	}
	
	private void createRealtions(){
		String data = sim.getSimDataset();
		String relat = "FOLLOW" + data;
		for(String key : nodeUris.keySet()){
			String loc1 = nodeUris.get(key);
			System.out.println(loc1);
			System.out.println("NODE 1 " + loc1);
			List<User> users = relations.get(loc1);
			for(User u : users){
				org.graphstream.graph.Node node = sim.getGraphManager().getGraph().getNode(u.getId());
				String loc2 = nodeUris.get(node.getId());
				this.addRelationship(loc2, loc1, relat, null);
			}
		}
	}
	
	private void saveNodeRelations(String nodeURI, List<User> followers){
		relations.put(nodeURI, followers);
	}
	

	public void addProperty(String nodeURI, String propertyName, String propertyValue){		
		try{
				String nodePointUrl = nodeURI + "/properties/" + propertyName;
				System.out.println(nodePointUrl);
				HttpClient client = new HttpClient();
				PutMethod mPut = new PutMethod(nodePointUrl);
				


				Header mtHeader = new Header();
				mtHeader.setName("content-type");
				mtHeader.setValue("application/json");
				mtHeader.setName("accept");
				mtHeader.setValue("application/json");
				mPut.addRequestHeader(mtHeader);

				String jsonString = "\"" + propertyValue + "\"";
				StringRequestEntity requestEntity = new StringRequestEntity(jsonString,
                                                        "application/json",
                                                        "UTF-8");
				mPut.setRequestEntity(requestEntity);
				client.executeMethod(mPut);
				mPut.getResponseBodyAsString( );

				mPut.releaseConnection( );

			}catch(Exception e){
				System.out.println("Exception in creating node in neo4j : " + e);
			}

	}
	
	public String addRelationship(String startNodeURI, String endNodeURI, String relationshipType,
            						String jsonAttributes){
		String location = null;
		try{
			String fromUrl = startNodeURI + "/relationships";
			System.out.println("from url : " + fromUrl);

			String relationshipJson = generateJsonRelationship( endNodeURI,
                                         	relationshipType,
                                         	jsonAttributes );

			System.out.println("relationshipJson : " + relationshipJson);

			HttpClient client = new HttpClient();
			PostMethod mPost = new PostMethod(fromUrl);


			Header mtHeader = new Header();
			mtHeader.setName("content-type");
			mtHeader.setValue("application/json");
			mtHeader.setName("accept");
			mtHeader.setValue("application/json");
			mPost.addRequestHeader(mtHeader);

			StringRequestEntity requestEntity = new StringRequestEntity(relationshipJson,
                                                 "application/json",
                                                 "UTF-8");
			mPost.setRequestEntity(requestEntity);
			client.executeMethod(mPost);
			mPost.getResponseBodyAsString( );
			Header locationHeader =  mPost.getResponseHeader("location");
			location = locationHeader.getValue();
			mPost.releaseConnection( );
		}catch(Exception e){
			System.out.println("Exception in creating node in neo4j : " + e);
		}

		return location;

	}
	
	private String generateJsonRelationship(String endNodeURL,
            String relationshipType,
            String ... jsonAttributes) {

		StringBuilder sb = new StringBuilder();
		sb.append("{ \"to\" : \"");
		sb.append(endNodeURL);
		sb.append("\", ");

		sb.append("\"type\" : \"");
		sb.append(relationshipType);
		
		if(jsonAttributes == null || jsonAttributes.length < 1) {
			sb.append("\"");
		} else {
			sb.append("\", \"data\" : ");
			for(int i = 0; i < jsonAttributes.length; i++) {
				sb.append(jsonAttributes[i]);
				if(i < jsonAttributes.length -1) { // Miss off the final comma
					sb.append(", ");
				}
			}
		}

		sb.append(" }");
		return sb.toString();
	}
	
	public String searchDatabase(String nodeURI, String relationShip){
        String output = null;
 
        try{
 
            TraversalDescription t = new TraversalDescription();
            t.setOrder( TraversalDescription.DEPTH_FIRST );
            t.setUniqueness( TraversalDescription.NODE );
            t.setMaxDepth( 10 );
            t.setReturnFilter( TraversalDescription.ALL );
            t.setRelationships( new simulation.util.Relationship( relationShip, simulation.util.Relationship.OUT ) );
 
            System.out.println(t.toString());
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(nodeURI+"/traverse/node");
 
            /**
             * set headers
             */
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
 
            /**
             * set json payload
             */
            StringRequestEntity requestEntity = new StringRequestEntity(t.toJson(),
                                                                        "application/json",
                                                                        "UTF-8");
            mPost.setRequestEntity(requestEntity);
            int satus = client.executeMethod(mPost);
            output = mPost.getResponseBodyAsString( );
            mPost.releaseConnection( );
            System.out.println("satus : " + satus);
            System.out.println("output : " + output);
        }catch(Exception e){
        	System.out.println("Exception in creating node in neo4j : " + e);
        }
 
        return output;
    }
	
	public void readDataSetName() throws IOException{
		File archivo = new File(DATASET_NAMES_PATH);
		FileReader fileReader = new FileReader(archivo);
		BufferedReader buffReader = new BufferedReader(fileReader);
		String linea = null;
		
		while((linea=buffReader.readLine()) != null){
			System.out.println(linea);
		}
		
		if(null!=fileReader){
			fileReader.close();
		}
		
	}
	
	public void saveDataSetName(String s) throws IOException{
		File datasetFile = new File(DATASET_NAMES_PATH);
		FileWriter file = new FileWriter(datasetFile, true);
		file.write(s);
		file.write("\n");
		file.close();
	}


	public void setPath(String s){
		this.path = s;
	}
	
	public String getPath(){
		return this.path;
	}

	public void setSim(Simulation s){
		this.sim = s;
	}
	
	public Simulation getSim(){
		return this.sim;
	}
//	public static void main (String[] args) throws IOException{
//		Neo4JManageTool n = new Neo4JManageTool();
//		n.saveDataSetName("Dani6");
//		n.saveDataSetName("Dani7");
//		n.saveDataSetName("Dani8");
//		n.saveDataSetName("Dani9");
//		n.saveDataSetName("Dani10");
//		n.saveDataSetName("Dani11");
//		n.readDataSetName();
//	}

	
}


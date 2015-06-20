package simulation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulation.Simulation;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;



public class Neo4JDBAccess{
	
	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	private Simulation sim;
	private HashMap<String, URI> nodeURI;
	private List<String> listNodes;
	private int nodes;
	private HashMap<String, String> relations;
	
	public Neo4JDBAccess(){
		nodeURI = new HashMap<String, URI>();
		this.listNodes = new ArrayList<String>();
		this.relations = new HashMap<String, String>();
	}
	
	public void saveInDB(){
		createNodes();
		createRelationships();
	}
	
	public void loadFromDB(String label){
		String jsonString = getStringFromLabel(label);
		List<JSONObject> jsonList = extractNodesFromString(jsonString);
		fillNodesList(jsonList);
	}
	
	private void createNodes()
    {
        // START SNIPPET: createNode
        final String nodeEntryPointUri = SERVER_ROOT_URI + "node";
        // http://localhost:7474/db/data/node

        WebResource resource = Client.create()
                .resource( nodeEntryPointUri );
        // POST {} to the node entry point URI
        for(int i = 0; i < this.sim.getGraphManager().getGraph().getNodeCount(); i++){
        	Node node = this.sim.getGraphManager().getGraph().getNode(i);
	        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
	                .type( MediaType.APPLICATION_JSON )
	                .entity( "{}" )
	                .post( ClientResponse.class );
	
	        final URI location = response.getLocation();
	        System.out.println( String.format(
	                "POST to [%s], status code [%d], location header [%s]",
	                nodeEntryPointUri, response.getStatus(), location.toString() ) );
	        addLabel(location, this.sim.getSimDataset());
//	        addProperty(location, "Id", node.getId());
	        response.close();
	        
	        nodeURI.put(node.getId(), location);
        }

    }
	
	private void createRelationships(){
		for(Edge e : this.sim.getGraphManager().getGraph().getEdgeSet()){
			Node source = e.getSourceNode();
			Node target = e.getTargetNode();
			URI sourceURI = nodeURI.get(source.getId());
			URI targetURI = nodeURI.get(target.getId());
			String sourceId = extractNodeIdFromURI(sourceURI);
			String targetId = extractNodeIdFromURI(targetURI);
			String json = generateJSONString(sourceId, targetId);
			try {
				addRelationship(sourceURI, targetURI, "follows", json);
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}
	
	private String extractNodeIdFromURI(URI nodeURI){
		String id = "";
		String[] splited = nodeURI.getPath().split("/");
		id = splited[splited.length - 1];
		return id;
		
	}
	//"{ \"from\" : \"1976\", \"until\" : \"1986\" }"
	private String generateJSONString(String idSource, String idTarget){
		String json = String.format("{ \"from\" : \"[%s]\", \"until\" : \"[%s]\" }", idSource, idTarget);
		return json;
	}
	
	// START SNIPPET: insideAddRel
    private static URI addRelationship( URI startNode, URI endNode,
            String relationshipType, String jsonAttributes )
            throws URISyntaxException
    {
        URI fromUri = new URI( startNode.toString() + "/relationships" );
        String relationshipJson = generateJsonRelationship( endNode,
                relationshipType, jsonAttributes );

        WebResource resource = Client.create()
                .resource( fromUri );
        // POST JSON to the relationships URI
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
                .type( MediaType.APPLICATION_JSON )
                .entity( relationshipJson )
                .post( ClientResponse.class );

        final URI location = response.getLocation();
        System.out.println("RESPONSE STATUS: " + response.getStatus());
        System.out.println( String.format(
                "POST to [%s], status code [%d], location header [%s]",
                fromUri, response.getStatus(), location.toString() ) );

        response.close();
        return location;
    }
    // END SNIPPET: insideAddRel

    private static String generateJsonRelationship( URI endNode,
            String relationshipType, String... jsonAttributes )
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "{ \"to\" : \"" );
        sb.append( endNode.toString() );
        sb.append( "\", " );

        sb.append( "\"type\" : \"" );
        sb.append( relationshipType );
        if ( jsonAttributes == null || jsonAttributes.length < 1 )
        {
            sb.append( "\"" );
        }
        else
        {
            sb.append( "\", \"data\" : " );
            for ( int i = 0; i < jsonAttributes.length; i++ )
            {
                sb.append( jsonAttributes[i] );
                if ( i < jsonAttributes.length - 1 )
                { // Miss off the final comma
                    sb.append( ", " );
                }
            }
        }

        sb.append( " }" );
        return sb.toString();
    }

	
	private void addProperty( URI nodeUri, String propertyName,
            String propertyValue )
    {
        // START SNIPPET: addProp
        String propertyUri = nodeUri.toString() + "/properties/" + propertyName;
        // http://localhost:7474/db/data/node/{node_id}/properties/{property_name}

        WebResource resource = Client.create()
                .resource( propertyUri );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
                .type( MediaType.APPLICATION_JSON )
                .entity( "\"" + propertyValue + "\"" )
                .put( ClientResponse.class );
        System.out.println( String.format( "PUT to [%s], status code [%d]",
                propertyUri, response.getStatus() ) );
        response.close();
    }
	
	private void addLabel( URI nodeUri, String labelName)
    {
        // START SNIPPET: addProp
        String labelUri = nodeUri.toString() + "/labels";
        // http://localhost:7474/db/data/node/{node_id}/properties/{property_name}

        WebResource resource = Client.create()
                .resource( labelUri );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
                .type( MediaType.APPLICATION_JSON )
                .entity( "\"" + labelName + "\"" )
                .post( ClientResponse.class );

        System.out.println( String.format( "PUT to [%s], status code [%d]",
                labelUri, response.getStatus() ) );
        response.close();
    }
	
	private String getStringFromLabel(String label){
		final String uri = SERVER_ROOT_URI + "label/" + label + "/nodes";

		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();  
			HttpGet getRequest = new HttpGet(uri);
			getRequest.addHeader("accept", "application/json");
			
			HttpResponse response = httpClient.execute(getRequest);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response.getStatusLine().getStatusCode());
			}
			
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

			String output;
			StringBuilder builder = new StringBuilder();
			int i = 0;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				builder.append(output);
				builder.append("\n");
				i++;
			}
			httpClient.close();
//			System.out.println(builder.toString());
			return builder.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
		
		
	}
	
	private List<JSONObject> extractNodesFromString(String stringJson){
		try {
			JSONArray jsonArray = new JSONArray(stringJson);
			List<JSONObject> listJson= new ArrayList<JSONObject>();
			for(int i = 0; i < jsonArray.length(); i++){
				listJson.add((JSONObject) jsonArray.get(i));
			}
			setNodes(listJson.size());
			return listJson;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void fillNodesList(List<JSONObject> jsonObject){
		for(JSONObject j : jsonObject){
			try {
				JSONObject json = j.getJSONObject("metadata");
				int nodeId = json.getInt("id");
				this.listNodes.add(""+nodeId);
				String relationsJSON = getOutRelations(j.getString("outgoing_relationships"));
				JSONArray relationsArray = new JSONArray(relationsJSON);
				List<JSONObject> jsonList = new ArrayList<JSONObject>();
				for(int i = 0; i < relationsArray.length(); i++){
					jsonList.add((JSONObject) relationsArray.get(i));
				}
				
				
				for(JSONObject jRelation : jsonList){
					JSONObject data = jRelation.getJSONObject("data");
					String from = (String) data.get("from");
					String until = (String) data.get("until");
					String fromSplitted = from.substring(1, from.length()-1);
					String untilSplitted = until.substring(1, until.length()-1);
					this.relations.put(fromSplitted, untilSplitted);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(this.listNodes.toString());
		System.out.println(this.relations.toString());
	}
	
	public String getOutRelations(String uri){
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();  
			HttpGet getRequest = new HttpGet(uri);
			getRequest.addHeader("accept", "application/json");
			
			HttpResponse response = httpClient.execute(getRequest);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response.getStatusLine().getStatusCode());
			}
			
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

			String output;
			StringBuilder builder = new StringBuilder();
			int i = 0;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				builder.append(output);
				builder.append("\n");
				i++;
			}
			httpClient.close();
			System.out.println(builder.toString());
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public int getNodes() {
		return nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}

	public Simulation getSim() {
		return sim;
	}

	public void setSim(Simulation sim) {
		this.sim = sim;
	}
	
	
	public static void main (String[] args){
		Neo4JDBAccess n = new Neo4JDBAccess();
		n.loadFromDB("Label1");
	}

	public List<String> getListNodes() {
		return listNodes;
	}

	public void setListNodes(List<String> listNodes) {
		this.listNodes = listNodes;
	}

	public HashMap<String, String> getRelations() {
		return relations;
	}

	public void setRelations(HashMap<String, String> relations) {
		this.relations = relations;
	}
	


}

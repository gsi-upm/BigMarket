package simulation.util;

import java.io.FileWriter;
import java.io.IOException;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GraphJSONParser {
	
	private Graph graph;
	private JsonObject json;
	private StringBuilder jsonString;
	
	public GraphJSONParser(Graph g){
		this.graph = g;
		this.json = new JsonObject();
		this.jsonString = new StringBuilder();
	}
	
	public void parser(){
		int a = 1;
		int b = 1;
		jsonString.append("{" + "\n" + "\"nodes\":[" + "\n");
		for(Node n : graph.getNodeSet()){
			String s = n.getId();
			if(a < graph.getNodeSet().size()){
				String s2 = "{\"name\":\"" + s + "\"}," + "\n";
				jsonString.append(s2);
			}else{
				String s2 = "{\"name\":\"" + s + "\"}" + "\n";
				jsonString.append(s2);
			}
			a++;
		}
		
		jsonString.append("],\"links\":[");
		for(Edge e: graph.getEdgeSet()){
			int n = e.getSourceNode().getIndex();
			int n2 = e.getTargetNode().getIndex();
			if(b < graph.getEdgeSet().size()){
				jsonString.append("{\"source\":" + n + ",\"target\":" + n2 + "}," + "\n");
			}else{
				jsonString.append("{\"source\":" + n + ",\"target\":" + n2 + "}" +"\n");
			}
			b++;
		}
		jsonString.append("]}");
		
		System.out.println(jsonString.toString());
	}
	
	private void finalParser(){
		Gson gson = new Gson();
		String s = jsonString.toString();
		JsonElement element = gson.fromJson (s, JsonElement.class);
		json = element.getAsJsonObject();
		
		try {
			 
			FileWriter file = new FileWriter("/home/dlara/Gitted/WebContent/g.json");
			file.write(json.toString());
			file.flush();
			file.close();
			 
			} catch (IOException e) {
			//manejar error
			}
	}
	
	public void launchParser(){
		this.parser();
		this.finalParser();
	}
	
	public JsonObject getJson(){
		return this.json;
	}

}

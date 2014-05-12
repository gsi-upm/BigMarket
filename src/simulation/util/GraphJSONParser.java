/**
*
*
* This file is part of BigMarket.
*
* BigMarket has been developed by members of the research Group on
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes),
* acknowledged group by the Technical University of Madrid [UPM]
* (Universidad Politécnica de Madrid)
*
* Authors:
* Daniel Lara
* Carlos A. Iglesias
* Emilio Serrano
*
* Contact:
* http://www.gsi.dit.upm.es/;
*
*
*
* BigMarket is free software:
* you can redistribute it and/or modify it under the terms of the GNU
* General Public License as published by the Free Software Foundation,
* either version 3 of the License, or (at your option) any later version.
*
*
* BigMarket is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with VoteSim. If not, see <http://www.gnu.org/licenses/>
*/

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

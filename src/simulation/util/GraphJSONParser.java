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
	private FileWriter file;
	
	public GraphJSONParser(Graph g){
		this.graph = g;
		this.json = new JsonObject();
		this.jsonString = new StringBuilder();
	}
	
	public void parser(){
		int a = 1;
		int b = 1;
		
		jsonString.append("{" + "\n" + "\"nodes\": [" + "\n");
		for(Node n : graph.getNodeSet()){
			String s = n.getId();
			int posX = (int) (Math.random()*graph.getNodeCount());
			int posY = (int) (Math.random()*graph.getNodeCount());
			if(a < graph.getNodeSet().size()){
				String s2 = "{ " + "\n" + "\"id\": \"" + s + "\"," + "\n" +
						"\"label\": \"" + s + "\"," + "\n" +
						"\"x\": " + posX + "," + "\n" +
						"\"y\": " + posY + "," + "\n" +
						"\"size\": " + (n.getInDegree() + 3)*2  + "\n" +
						"},";
				jsonString.append(s2);
			}else{
				String s2 = "{ " + "\n" + "\"id\": \"" + s + "\"," + "\n" +
						"\"label\": \"" + s + "\"," + "\n" +
						"\"x\": " + posX + "," + "\n" +
						"\"y\": " + posY + "," + "\n" +
						"\"size\": " + (n.getInDegree() + 3)*2  + "\n" +
						"}" + "\n";
				jsonString.append(s2);
			}
			a++;
		}
		String q = "]," + "\n" +
				"\"edges\": [" + "\n";
		jsonString.append(q);
		for(Edge e: graph.getEdgeSet()){
			int n = e.getSourceNode().getIndex();
			int n2 = e.getTargetNode().getIndex();
			Node source = e.getSourceNode();
			Node target = e.getTargetNode();
			if(b < graph.getEdgeSet().size()){
				String h = "{" + "\n" +
						"\"id\": \"" + e.getId() + "\"," + "\n" +
						"\"source\": \"" + source.getId() +  "\"," + "\n" +
						"\"target\": \"" + target.getId() + "\"" + "\n" +
						"}," + "\n";		
				jsonString.append(h);
			}else{
				String h = "{" + "\n" +
						"\"id\": \"" + e.getId() + "\"," + "\n" +
						"\"source\": \"" + source.getId() +  "\"," + "\n" +
						"\"target\": \"" + target.getId() + "\"" + "\n" +
						"}" + "\n";	
				jsonString.append(h);
			}
			b++;
		}
		String w = "]" + "\n" + "}";
		jsonString.append(w);
		
		System.out.println(jsonString.toString());
	}
	
	private void finalParser(String path){
//		Gson gson = new Gson();
		String s = jsonString.toString();
//		JsonElement element = gson.fromJson (s, JsonElement.class);
//		json = element.getAsJsonObject();
		
		try {
			file = new FileWriter(path);
			file.write(s);
			file.flush();
			file.close();
			System.out.println("File saved");
			} catch (IOException e) {
			//manejar error
			}
	}
	
	public void launchParser(String path){
		this.parser();
		this.finalParser(path);
	}
	
	public JsonObject getJson(){
		return this.json;
	}

}

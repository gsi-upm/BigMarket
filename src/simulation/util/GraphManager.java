package simulation.util;

/**This file is part of TweetSim.

TweetSim has been developed by members of the research Group on
Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes),
acknowledged group by the Universidad Politécnica de Madrid [UPM]
(Technical University of Madrid)

Authors:
Álvaro Carrera 
Carlos A. Iglesias
Daniel Lara
Emilio Serrano

 
Contact:
http://www.gsi.dit.upm.es/;
 
 
 
TweetSim is free software:
you can redistribute it and/or modify it under the terms of the GNU

General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.


 
TweetSim is distributed in the hope that it will be useful,

but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the

GNU General Public License for more details.
 
You should have received a copy of the GNU General Public License

along with TweetSim. If not, see <http://www.gnu.org/licenses/>
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.gephi.JSONSender;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.Viewer;

import simulation.Simulation;
import simulation.SimulationGUI;
import simulation.model.User;
import simulation.model.event.Follow;

/**
 * This class manage the graphic representation of the graph
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 */
public class GraphManager {
	
	private Graph graph;
	private Simulation sim;
	private SimulationGUI gui;
	private Graph finalGraph;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Main constructor of the class 
	 * 
	 * @param sim
	 */
	public GraphManager(Simulation sim){
		this.sim = sim;
	}
	
	
	/**
	 * This method sends the graph to gephi in order to represent it in that platform
	 * 
	 * @param graph
	 */
	public void sendGraphToGephi(Graph graph){
		this.setGraph(graph);
		try{
			JSONSender sender = new JSONSender("localhost", 8081, "workspace0"); 
			graph.addSink(sender);	
	
		}catch(Exception e){
			System.out.println("Master server in Gephi isn't started");
			
		}
	}
	
	/**
	 * This method execute the methods that create the graphics, send the graph to gephi
	 * and create the graph 
	 * 
	 * @param st
	 */
	public void runGraphManager(Statistics st){
		createTheViewers();
		//sendGraphToGephi(finalGraph);
		createInitialNetwork();
	}
	
	public void runGraphManagerWithoutDataSets(Statistics st){
		createTheViewers();
		//sendGraphToGephi(finalGraph);
		createScaleFreeNetwork(st);
	}
	
	/**
	 * This method create the graphic visualization of the graph
	 */
	public void createTheViewers(){
		graph = new MultiGraph("");
		finalGraph = new MultiGraph("Random Graph to test");
		setStyleSheet();
		this.setGui(sim.getGui());
		Viewer viewer = finalGraph.display();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		DefaultView view = (DefaultView) viewer.getDefaultView();
		view.getJFrame().setVisible(false);
		gui.getNetworkFrame2D().setSize(750, 750);		
		gui.getNetworkFrame2D().add(view);	
		view.getJFrame().setVisible(false);
		
	}
	
	/**
	 * This method set the stylsheet of the graph visualization
	 */
	public void setStyleSheet(){
		String style = "node {size:10px;fill-color:blue;}" 
				+ "node.sender{size:10px;fill-color:red;}"
				+ "node.reciever{size:10px;fill-color:black;}"
				+ "edge {arrow-size: 5px, 2px;}";
		finalGraph.addAttribute("stylesheet", style);
		finalGraph.addAttribute("ui.antialias", true);
		finalGraph.addAttribute("layout.stabilization-limit", 1000);	
	}

	/**
	 * This method create the graph using the number of agents given it by statistics
	 * 
	 * @param st
	 */
	public void createScaleFreeNetwork(Statistics st){
		int numberOfAgents = st.numberOfAgents();
		Generator gen = new BarabasiAlbertGenerator(1);
		gen.addSink(graph);
		gen.begin();
		for(int i=0; i<numberOfAgents; i++) {
		    gen.nextEvents();
		}
		gen.end();
		for(int n = 0; n < graph.getNodeCount(); n++){
			Node node = graph.getNode(n);
			finalGraph.addNode(node.getId());
			User user = new User(n, "id" + n, "User" + n);
		    sim.addUser(user);
			
		}
		for(int m = 0; m < graph.getEdgeCount(); m++){
			Edge e = graph.getEdge(m);
			finalGraph.addEdge(e.getId(), e.getSourceNode(), e.getTargetNode(), true);
			createInitialFollows(e);
		}
	}
	
	public void createInitialNetwork(){
		for(int n = 0; n < sim.getEventManager().getNumberOfAgents(); n++){
			finalGraph.addNode("Node"+n);
			User user = new User(n, "id" + n, "User" + n);
		    sim.addUser(user);
			System.out.println("FIRST " + n);
		}
		
		File fileEdges = new File("/home/dlara/PFC/BigMarketv2/BigMarketv2/src/simulation/util/edges.txt");
		//File fileEdges = new File("/home/dlara/Twitter Dataset/edges.csv");
		int n = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileEdges))){
			while(true){
				String line = reader.readLine();
				if(line==null || line.equals("")){
					break;
				}
				n++;
				System.out.println(n);
				String[] fields = line.split(",");
				int target = Integer.parseInt(fields[0]);
				int origin = Integer.parseInt(fields[1]);
				logger.info("Target " + target);
				logger.info("Origin " + origin);
				finalGraph.addEdge("Edge" + fields[1] + "-->" + fields[0], finalGraph.getNode(origin)
						, finalGraph.getNode(target), true);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int m = 0; m < finalGraph.getEdgeCount(); m++){
			Edge e = finalGraph.getEdge(m);
			createInitialFollows(e);
		}
	}
	
	/**
	 * This method create the initial follows of the graph
	 * 
	 * @param e
	 */
	public void createInitialFollows(Edge e){
		User origin = sim.getUsers().get(e.getSourceNode().getIndex());
		User target = sim.getUsers().get(e.getTargetNode().getIndex());
		Follow f = new Follow("Follow " + e.getId(), "TS " + e.getId(), target, origin);
		
	}
	

	
	/**
	 * Get the graph
	 * 
	 * @return finalGraph
	 */
	public Graph getGraph() {
		return finalGraph;
	}

	/**
	 * Set the graph
	 * 
	 * @param graph
	 */
	public void setGraph(Graph graph) {
		this.finalGraph = graph;
	}
	
	/**
	 * Set the gui
	 * 
	 * @param gui
	 */
	public void setGui(SimulationGUI gui){
		this.gui = gui;
	}

	/**
	 * Get the gui
	 * 
	 * @return gui
	 */
	public SimulationGUI getGui(){
		return this.gui;
	}
}

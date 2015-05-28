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
package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkGEXF;
import org.openide.util.Lookup;

import simulation.Launcher;
import simulation.Simulation;
import simulation.model.User;
import simulation.util.Constants;
import simulation.util.GraphJSONParser;
import simulation.util.Neo4JManageTool;

/**
 * Servlet implementation class BigMarketServlet
 */
public class BigMarketServlet extends HttpServlet {
     /**
	 * 
	 */
	private static final long serialVersionUID = -7634769531465961909L;
	
	private Simulation sim;
	private Neo4JManageTool neoDB;
	private String datasetName;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public BigMarketServlet() {
        super();
        this.neoDB = new Neo4JManageTool();
        System.out.println("SERVLET CREATED");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGetB(request, response);	
	}
	
	private void launchSimulation(HttpServletRequest request, HttpServletResponse response, int numNodes,
			String networkName) throws ServletException, IOException{
		sim = new Simulation(System.currentTimeMillis());
		sim.setFlag(1);		
		sim.setNumberOfNodes(numNodes);
		sim.setSimDataSet(networkName);
				
		Launcher launcher = new Launcher(sim);
		launcher.start();
		
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("acqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.getRequestDispatcher(Constants.RUNNING_PAGE).forward(request, response);
		
		
	
		
	}
	
	private void clickRunOneStep(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(sim.getGui().getConsole().getPlayState() == 2){
			sim.getGui().getConsole().pressPlay();
		}else{
			sim.getGui().getConsole().pressPause();
			sim.getGui().getConsole().pressPlay();
		}
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.setAttribute("oddTweets", sim.getEventManager().getStatistics().getOddTweets());
		request.setAttribute("broadTweets", sim.getEventManager().getStatistics().getBroadTweets());
		request.setAttribute("acqTweets", sim.getEventManager().getStatistics().getAcqTweets());
		request.getRequestDispatcher(Constants.RUNNING_PAGE).forward(request, response);
	}
	
	private void clickRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(sim.getGui().getConsole().getPlayState() == 0){
			sim.getGui().getConsole().pressPlay();
		}else if(sim.getGui().getConsole().getPlayState() == 2){
			sim.getGui().getConsole().pressPause();
		}
		
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.setAttribute("oddTweets", sim.getEventManager().getStatistics().getOddTweets());
		request.setAttribute("broadTweets", sim.getEventManager().getStatistics().getBroadTweets());
		request.setAttribute("acqTweets", sim.getEventManager().getStatistics().getAcqTweets());
		request.getRequestDispatcher(Constants.RUNNING_PAGE).forward(request, response);
		
		
	}
	
	private void clickPause(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(sim.getGui().getConsole().getPlayState() != 2){
			sim.getGui().getConsole().pressPause();
		}
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.setAttribute("oddTweets", sim.getEventManager().getStatistics().getOddTweets());
		request.setAttribute("broadTweets", sim.getEventManager().getStatistics().getBroadTweets());
		request.setAttribute("acqTweets", sim.getEventManager().getStatistics().getAcqTweets());
		request.getRequestDispatcher(Constants.RUNNING_PAGE).forward(request, response);
	}
	
	private void clickStop(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		sim.getGui().getConsole().pressStop();
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		
		sim.finish();
		neoDB.setSim(sim);
		neoDB.launchDatabaseTool();
		GraphJSONParser g = new GraphJSONParser(sim.getGraphManager().getGraph());
		String path = getServletContext().getRealPath("/") + "networkGraph.json";
		g.launchParser(path);
		exportGraphGEXF();
		calculateCloseness(request, response);
		calculateBetweenness(request, response);
		request.getRequestDispatcher(Constants.ACTIONS_PAGE).forward(request, response);
	}
	
	
	private void calculateBetweenness(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		String path = getServletContext().getRealPath("/") + "grafoInicial.gexf";
		FileSinkGEXF out = new FileSinkGEXF();
		out.writeAll(sim.getGraphManager().getGraph(), path);
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;
		try{
			File file = new File(path);
			container = importController.importFile(file);
			container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
			container.setAllowAutoNode(false);
		}catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		importController.process(container,new DefaultProcessor(), workspace);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
	
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);
		
		
		AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
		AttributeModel model = ac.getModel();
		AttributeColumn col = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		System.out.println(col);
		
		HashMap<Double, Integer> bet = new HashMap<Double, Integer>();
		List<Double> finalBet = new ArrayList<Double>();
		List<Integer> finalNodes = new ArrayList<Integer>();
		for(Node n : graphModel.getGraph().getNodes()) {
			 Double centrality = (Double)n.getNodeData().getAttributes().getValue(col.getTitle());
//			 System.out.println("Node " + n.getId() + " betweeness " + centrality);
			 bet.put(centrality, n.getId());
		}
		List<Double> betPerNode = new ArrayList<Double>(bet.keySet());

    
	    for(double p : betPerNode){
	    	finalBet.add(p);
	    	finalNodes.add(bet.get(p));  	
	    }
	    Double[] arrayBet = new Double[finalBet.size()];
	    int[] arrayNodes = new int[finalNodes.size()];
	    
	    for(int i = 0; i < arrayBet.length; i++){
	    	arrayBet[i] = finalBet.get(i);
	    }
	    
	    for(int j = 0; j < arrayNodes.length; j++){
	    	arrayNodes[j] = finalNodes.get(j);
	    }
	    System.out.println("Size bet " + arrayBet.length);
	    System.out.println("Size betnodes " + arrayNodes.length);
		request.setAttribute("bet", arrayBet);
		request.setAttribute("betnodes", arrayNodes);

	}
	
	private void calculateCloseness(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String path = getServletContext().getRealPath("/") + "grafoInicial.gexf";		
		FileSinkGEXF out = new FileSinkGEXF();
		out.writeAll(sim.getGraphManager().getGraph(), path);
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;
		try{
			File file = new File(path);
			container = importController.importFile(file);
			container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
			container.setAllowAutoNode(false);
		}catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		importController.process(container,new DefaultProcessor(), workspace);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
	
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);
		
		
		AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
		AttributeModel model = ac.getModel();
		AttributeColumn col = attributeModel.getNodeTable().getColumn(GraphDistance.CLOSENESS);
		System.out.println(col);
		SortedMap<Integer, Double> close = new TreeMap<Integer, Double>(java.util.Collections.reverseOrder());
		HashMap <Integer, Integer> outdegree = new HashMap<Integer, Integer>();
		HashMap <Integer, Integer> indegree = new HashMap<Integer, Integer>();
		for(Node a : graphModel.getGraph().getNodes()) {
			 Double centrality = (Double)a.getNodeData().getAttributes().getValue(col.getTitle());
			 System.out.println("Node " + a.getId() + " closeness " + centrality);
			 indegree.put(a.getId(), graphModel.getGraph().getEdges(a).toArray().length);
			 outdegree.put(a.getId(), graphModel.getGraph().getDegree(a));
			 close.put(a.getId(), centrality);
		}
	

		request.setAttribute("outdegree", outdegree);
		request.setAttribute("indegree", indegree);
		request.setAttribute("close", close);
	}
	
	private void clickSeeNetwork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		GraphJSONParser g = new GraphJSONParser(sim.getGraphManager().getGraph());
		g.launchParser("A");
		request.getRequestDispatcher(Constants.ACTIONS_PAGE).forward(request, response);
	}
	
	
	
	private void exportGraphGEXF(){
		String path = getServletContext().getRealPath("/") + "grafoInicial.gexf";
		FileSinkGEXF file = new FileSinkGEXF();
		try {
			file.writeAll(sim.getGraphManager().getGraph(), path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getBroadUsers(Simulation sim){
		int result = 0;
		for(User u : sim.getUsers()){
			if(u.getType() != null){
				if(u.getType().equals(Constants.USER_TYPE_BROADCASTER)){
					result++;
			
				}
			}
		}
		return result;
	}
	
	public int getAqUsers(Simulation sim){
		int result = 0;
		for(User u : sim.getUsers()){
			if(u.getType() != null){
				if(u.getType().equals(Constants.USER_TYPE_ACQUAINTANCES)){
					result++;
			
				}
			}
		}
		return result;
	}
	
	public int getOddUsers(Simulation sim){
		int result = 0;
		for(User u : sim.getUsers()){
			if(u.getType() != null){
				if(u.getType().equals(Constants.USER_TYPE_ODDUSERS)){
					result++;
			
				}
			}
		}
		return result;
	}
	
	
	protected void doGetB(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String formName = request.getParameter(Constants.FORM_NAME);
		if(formName.equals(Constants.SETUP_FORM_NAME)){
			String radioButtons = request.getParameter(Constants.RADIO_BUTTONS_NAME);
			if(radioButtons.equals(Constants.RANDOM_SELECTED)){
				int numberOfNodes = Integer.parseInt(request.getParameter(Constants.NUMBER_OF_NODES));
				String randomNetworkName = request.getParameter(Constants.RANDOM_NETWORK_NAME);	
				launchSimulation(request, response, numberOfNodes, randomNetworkName);
			}else if(radioButtons.equals(Constants.LOAD_SELECTED)){
				Neo4JManageTool n = new Neo4JManageTool();
				n.launchLoad(request.getParameter("datasetIdentifier"));
				sim = new Simulation(System.currentTimeMillis());
				sim.setDataBase(n);
				sim.setFlag(2);
				String data = request.getParameter("newLoadName");
				sim.setSimDataSet(data);
				Launcher launcher = new Launcher(sim);
				launcher.start();
				
				request.setAttribute("broadUsers",getBroadUsers(sim));
				request.setAttribute("acqUsers", getAqUsers(sim));
				request.setAttribute("oddUsers", getOddUsers(sim));
				request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
				request.setAttribute(Constants.SIM, sim);
				request.getRequestDispatcher(Constants.RUNNING_PAGE).forward(request, response);
			}
		}else if(formName.equals(Constants.RUNNING_FORM_NAME)){
			String actionSelected = request.getParameter(Constants.ACTION_SELECTED);
			if(actionSelected.equals(Constants.RUN_ONE_STEP)){	
				clickRunOneStep(request, response);
			}else if(actionSelected.equals(Constants.RUN)){
				clickRun(request, response);
			}else if(actionSelected.equals(Constants.PAUSE)){
				clickPause(request, response);
			}else{
				clickStop(request, response);
			}
		}
	}
	
	

}

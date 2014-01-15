package simulation.util;

import java.util.Vector;
import java.util.logging.Logger;

import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import sim.engine.SimState;
import sim.engine.Steppable;
import simulation.Simulation;
import simulation.model.User;

public class NetworkGrowth implements Steppable{
	
	private static final long serialVersionUID = -404896649928390724L;
	private int population;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private int initialPopulation;
	private int newPopulation;
	private int lastPopulation;
	private GraphManager graphManager;

	
	public NetworkGrowth(Simulation sim, GraphManager graphManager){
		this.graphManager = graphManager;
		this.population = sim.getEventManager().getNumberOfAgents();

	}
	
	@Override
	public void step(SimState sim) {
		Simulation simulation = (Simulation) sim;
		this.initialPopulation = simulation.getEventManager().getNumberOfAgents();
		this.lastPopulation = population;
		long t = simulation.schedule.getSteps();
		double exponent = (1190.0/5000.0);
		double r = Math.pow(t, exponent);
		double integralResult = (1565*r)/937;
		double n = initialPopulation*Math.exp(integralResult);
		int nt = (int) Math.round(n);
		
		this.setPopulation(nt);
		this.newPopulation = population;
		
		createNewUsers(simulation);
		//connectNewUsers(simulation);
	}
	
	public void createNewUsers(Simulation sim){
		Graph graph = graphManager.getGraph();
		int diference = newPopulation - lastPopulation;
		for(int i = 0; i < diference; i++){
			graph.addNode(Integer.toString(i+lastPopulation));	
			logger.info("Node " + (i+lastPopulation) + " added");
			User u = new User((i+lastPopulation), "id " + (i+lastPopulation),
					"User " + (i+lastPopulation));
			sim.addUser(u);
			
			
//			for(int j = 0; j<graph.getNodeCount(); j++){
//				Node node = graph.getNode(j+lastPopulation);
//				double edgeNumber = graph.getEdgeCount();
//				System.out.println("EDGE COUNT " + edgeNumber);
//				Node target = graph.getNode(j);
//				System.out.println("TARGET " + target.getId());
//				double edges = sim.getUsers().get(j).getFollowers().size();
//				System.out.println("EDGES " + edges);
//				double prob = (edges/edgeNumber) + 0.1;
//				System.out.println("NODE " + node.getId() + " prob= " + prob);
//				probs.add(prob);
////					graph.addEdge("Edge" + node.getId() + "-->" + target.getId(), 
////							target, node, true);	
//			}
//			System.out.println("VECTOR SIZE " + probs.size());
		}
		
	}
	
	public void connectNewUsers(Simulation sim){
		Vector<Node> oldNodes = new Vector<Node>(); 
		Vector<Node> newNodes = new Vector<Node>();
		Graph graph = graphManager.getGraph();
		for(int i = 0; i < graph.getNodeCount(); i++){
			Node n = graph.getNode(i);
			if(n.getDegree() == 0){
				newNodes.add(n);
			}else{
				oldNodes.add(n);
			}
		}
		System.out.println("OLD NODES SIZE " + oldNodes.size());
		System.out.println("NEW NODES SIZE " + newNodes.size());
	}

	
	public void setPopulation(int pop){
		this.population = pop;
	}
	
	public int getPopulation(){
		return this.population;
	}
	
}

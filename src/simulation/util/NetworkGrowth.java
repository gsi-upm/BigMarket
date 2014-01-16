package simulation.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import sim.engine.SimState;
import sim.engine.Steppable;
import simulation.Simulation;
import simulation.model.User;
import simulation.model.event.Follow;

public class NetworkGrowth implements Steppable{
	
	private static final long serialVersionUID = -404896649928390724L;
	private int population;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private int initialPopulation;
	private int newPopulation;
	private int lastPopulation;
	private GraphManager graphManager;
	private List<Integer> popularity;

	
	public NetworkGrowth(Simulation sim, GraphManager graphManager){
		this.graphManager = graphManager;
		this.population = sim.getEventManager().getNumberOfAgents();
		this.popularity = new ArrayList<Integer>();

	}
	
	@Override
	public void step(SimState sim) {
		Simulation simulation = (Simulation) sim;
		fillPopularityList(simulation);
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
		lookForNewUsers(simulation);
	}
	
	private void fillPopularityList(Simulation sim){
		popularity.clear();
		for(User u : sim.getUsers()){
			for(int i = 0; i < u.getFollowers().size(); i++){
				popularity.add(u.getId());
			}
		}
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

		}
		
	}
	
	public void connectNewUsers(Simulation sim, Node n1, Node n2){
		Graph graph = sim.getGraphManager().getGraph();
		graph.addEdge(Integer.toString(graph.getEdgeCount()+1), n2, n1, true);
		Follow f = new Follow("Follow " + graph.getEdgeCount()+1, "TS " + graph.getEdgeCount()+1
				, sim.getUsers().get(n1.getIndex()), sim.getUsers().get(n2.getIndex()));
	}
    
	public void lookForNewUsers(Simulation sim){
		Graph graph = sim.getGraphManager().getGraph();
		for(User u: sim.getUsers()){
			if(u.getFollowed().size() == 0 && u.getFollowers().size() == 0){
				System.out.println("AAASASS " + u.getId());
				Node n2 = graph.getNode(u.getId());
				int random = (int) (Math.random()*popularity.size());
				Node n1 = graph.getNode(popularity.get(random));
				connectNewUsers(sim, n1, n2);
			}
		}
	}
	
	public void setPopulation(int pop){
		this.population = pop;
	}
	
	public int getPopulation(){
		return this.population;
	}
	
}

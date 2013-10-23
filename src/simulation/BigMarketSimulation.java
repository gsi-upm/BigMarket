package simulation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.network.Network;
import sim.portrayal.network.SpatialNetwork2D;
import sim.util.Double2D;
import simulation.model.link.Connection;
import simulation.model.node.User;
import simulation.util.KKVisualizator;
import simulation.util.NodeManager;

public class BigMarketSimulation extends SimState{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private int selectNetworkTopology = 1;
	private int numberOfNodes = 100;
	private int networkDimension = 2000;
	private int numberOfSuperNodes = 0;
	private List<User> nodes;
	private Network network;
	private Continuous2D nodeFields2D;
	private BigMarketSimulationGUI gui;
	private List<Connection> connect;
	private NodeManager nodeManager;

	private double[] degrees;


	private static final long serialVersionUID = -2827383081610092503L;

	public BigMarketSimulation(long seed) {
		super(seed);
	}
	
	
	
	public void start(){
		logger.info("Starting sim...");
		
		this.nodeManager = new NodeManager();
		
		logger.info("Building the network...");
		if(selectNetworkTopology == 1){
			this.setNetwork(buildScaleFreeNetwork());
			KKVisualizator kkv = new KKVisualizator();
			this.schedule.scheduleRepeating(kkv, 0, 1);
			
			for (User user : this.nodes) {
				this.schedule.scheduleRepeating(user, 1, 1);
			}
			
			this.schedule.scheduleRepeating(this.nodeManager, 0, 1);
		}else if(selectNetworkTopology == 2){
			this.setNetwork(buildRegularNetwork());
		}else if(selectNetworkTopology == 3){
			logger.info("Building Small World...");
			this.setNetwork(buildSmallWorldNetwork());
			for (User user : this.nodes) {
				this.schedule.scheduleRepeating(user, 1, 1);
			}
			logger.info("SmallWorld network done");
		}else{
			this.setNetwork(buildEmptyNetwork());
		}
		
		logger.info("Done");
		
		logger.info("Building 2D space...");
		this.build2DSpace();
		logger.info("Done");
		
		
		
	}

	



	private void build2DSpace() {
		this.nodeFields2D = new Continuous2D(0.1, 1050, 1050);
		this.setNodeFields2D(nodeFields2D);
		
		for(User n : this.getNodes()){
			this.nodeFields2D.setObjectLocation(n, 
					new Double2D(n.getPosition()[0], n.getPosition()[1]));
		}
	}

	
	private Network buildScaleFreeNetwork() {
		Factory<Graph<User, Connection>> graphFactory = new Factory<Graph<User,Connection>>() {

			
			public Graph<User, Connection> create() {
				return new SparseMultigraph<User, Connection>();
			}
		};
		
		Factory<User> vertexFactory = new Factory<User>() {
			private int id = 0;
			
			public User create(){
				return new User(id++);
			}
			
		};
		
		
		Factory<Connection> edgeFactory = new Factory<Connection>(){
			private int id = 0;
			
			public Connection create(){
				return new Connection(id++);
			}
		};
		
		Set<User> seeds = new HashSet<User>();
		User n1 = new User(1);
		seeds.add(n1);
		User n2 = new User(2);
		seeds.add(n2);
		BarabasiAlbertGenerator<User, Connection> barabasi = new BarabasiAlbertGenerator<User, Connection>
			(graphFactory, vertexFactory, edgeFactory, 1, 1, seeds);
		
		Graph<User, Connection> graph = barabasi.create();
		barabasi.evolveGraph(this.numberOfNodes - 1);
		
		KKLayout<User, Connection> layout = new KKLayout<User, Connection>(graph);
		BasicVisualizationServer<User, Connection> vv = new BasicVisualizationServer<User, Connection>(layout);
		vv.setPreferredSize(new Dimension(this.networkDimension, this.networkDimension));
		
		Network net = new Network(true);
		this.nodes = new ArrayList<User>(graph.getVertices());
		for(User n : nodes){
			double[] position = new double[2];
			position[0] = layout.getX(n);
			position[1] = layout.getY(n);
			n.setPosition(position);
			net.addNode(n);
		}
		
		this.connect = new ArrayList<Connection>(graph.getEdges());
		for(Connection c : connect){
			Pair<User> nodes = graph.getEndpoints(c);
			c.setNodeA(nodes.getFirst());
			c.setNodeB(nodes.getSecond());
			net.addEdge(nodes.getFirst(), nodes.getSecond(), c);
		}
		
		return net;
	}
	
	/**
	 * Build a network without connections. Only users randomly placed.
	 * 
	 * @return 
	 */
	private Network buildEmptyNetwork() {
		Network net = new Network(true);	
		
		this.nodes = new ArrayList<User>();
		
		for(int i = 0; i < numberOfNodes; i++){
			User n = new User(i);
			double[] pos = new double[2];
			pos[0] = (int) (Math.random()*1000);
			pos[1] = (int) (Math.random()*1000);
			
			n.setPosition(pos);
			this.nodes.add(n);
			net.addNode(n);
			
		}
		
		this.connect = new ArrayList<Connection>();
		
		return net;
	}

	private Network buildSmallWorldNetwork() {
		Network net = new Network(true);
		int radio = 475;
		this.selectDegrees(numberOfNodes);
		int center = 500;
		
		this.nodes = new ArrayList<User>();
		for(int i = 0; i < numberOfNodes; i++){
			User n = new User(i);
			double[] pos = new double[2];
			pos[0] = center + this.xPosition(i, radio);
			pos[1] = center + this.yPosition(i, radio);
			
			n.setPosition(pos);
			this.nodes.add(n);
			net.addNode(n);	
		}
		
		this.connect = new ArrayList<Connection>();
		int extraConnections = (int) numberOfNodes/5;
		for(int j = 0; j < (numberOfNodes + extraConnections); j++){
			if(j == numberOfNodes - 1){
				Connection con = new Connection(j, nodes.get(j), nodes.get(0));
				this.connect.add(con);
				net.addEdge(nodes.get(j), nodes.get(0), con);
				nodes.get(j).getConnections().add(con);
				nodes.get(0).getConnections().add(con);
			}else if(j > numberOfNodes - 1){
				int ram1 = (int) (Math.random()*numberOfNodes+1);
				int ram2 = (int) (Math.random()*numberOfNodes+1);
				if(ram1 != ram2 && ram1 < numberOfNodes && ram2 < numberOfNodes){
					Connection con = new Connection(j, nodes.get(ram1), nodes.get(ram2));
					this.connect.add(con);
					net.addEdge(nodes.get(ram1), nodes.get(ram2), con);
					nodes.get(ram1).getConnections().add(con);
					nodes.get(ram2).getConnections().add(con);
				}
			}else{
				Connection con = new Connection(j, nodes.get(j), nodes.get(j+1));
				this.connect.add(con);
				net.addEdge(nodes.get(j), nodes.get(j+1), con);
				nodes.get(j).getConnections().add(con);
				nodes.get(j+1).getConnections().add(con);
			}
		}
		return net;
	}

	private Network buildRegularNetwork() {
		Network net = new Network(true);
		int radio = 475;
		this.selectDegrees(numberOfNodes);
		int center = 500;
		
		this.nodes = new ArrayList<User>();
		for(int i = 0; i < numberOfNodes; i++){
			User n = new User(i);
			double[] pos = new double[2];
			pos[0] = center + this.xPosition(i, radio);
			pos[1] = center + this.yPosition(i, radio);
			
			n.setPosition(pos);
			this.nodes.add(n);
			net.addNode(n);	
		}
		
		this.connect = new ArrayList<Connection>();
		for(int j = 0; j < (numberOfNodes); j++){
			if(j == numberOfNodes - 1){
				Connection con = new Connection(j, nodes.get(j), nodes.get(0));
				this.connect.add(con);
				net.addEdge(nodes.get(j), nodes.get(0), con);
			}else{
				Connection con = new Connection(j, nodes.get(j), nodes.get(j+1));
				this.connect.add(con);
				net.addEdge(nodes.get(j), nodes.get(j+1), con);
			}
		}
		return net;
	}


	
	public void finish() {
		super.finish();
		if (this.gui != null) {
			gui.finish();
		}
	}
	
	public void createNewLink(User node1, User node2) {
		Connection link = new Connection(this.connect.size(), node1, node2);
		this.connect.add(link);
		this.network.addEdge(node1, node2, link);

	}

	
	private void selectDegrees(int nodes){
		this.degrees = new double[nodes];
		double deg = (2*Math.PI)/nodes;
		for(int i = 0; i < nodes; i++){
			this.degrees[i] = deg*i;
		}
	}
	
	private double xPosition(int d, int radio){
		double x = (radio*Math.cos(degrees[d]));
		logger.info("El Nodo: " + d + " en la X tiene: " + x);
		return x;
	}
	
	private double yPosition(int d, int radio){
		double y = (radio*Math.sin(degrees[d]));
		logger.info("El Nodo: " + d + " en la Y tiene: " + y);
		return y;
	}



	/**
	 * @return the numberOfNodes
	 */
	public int getNumberOfNodes() {
		return numberOfNodes;
	}



	/**
	 * @param numberOfNodes the numberOfNodes to set
	 */
	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}



	/**
	 * @return the networkDimension
	 */
	public int getNetworkDimension() {
		return networkDimension;
	}



	/**
	 * @param networkDimension the networkDimension to set
	 */
	public void setNetworkDimension(int networkDimension) {
		this.networkDimension = networkDimension;
	}



	/**
	 * @return the numberOfSuperNodes
	 */
	public int getNumberOfSuperNodes() {
		return numberOfSuperNodes;
	}



	/**
	 * @param numberOfSuperNodes the numberOfSuperNodes to set
	 */
	public void setNumberOfSuperNodes(int numberOfSuperNodes) {
		this.numberOfSuperNodes = numberOfSuperNodes;
	}



	/**
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}



	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}



	/**
	 * @return the nodeFields2D
	 */
	public Continuous2D getNodeFields2D() {
		return nodeFields2D;
	}



	/**
	 * @param nodeFields2D the nodeFields2D to set
	 */
	public void setNodeFields2D(Continuous2D nodeFields2D) {
		this.nodeFields2D = nodeFields2D;
		this.gui.getCon2D().setField(nodeFields2D);
		this.gui.getNet2D().setField(new SpatialNetwork2D(nodeFields2D, this.network));
	}



	/**
	 * @return the gui
	 */
	public BigMarketSimulationGUI getGui() {
		return gui;
	}



	/**
	 * @param gui the gui to set
	 */
	public void setGui(BigMarketSimulationGUI gui) {
		this.gui = gui;
	}



	/**
	 * @return the nodes
	 */
	public List<User> getNodes() {
		return nodes;
	}



	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<User> nodes) {
		this.nodes = nodes;
	}

	public List<Connection> getConnections(){
		return connect;
	}
	
	public void setConnects(List<Connection> connect){
		this.connect = connect;
	}
	
	public NodeManager getNodeManager(){
		return this.nodeManager;
	}
	
}

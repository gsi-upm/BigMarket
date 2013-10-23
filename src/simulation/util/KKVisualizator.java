package simulation.util;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;
import simulation.BigMarketSimulation;
import simulation.model.link.Connection;
import simulation.model.node.User;

public class KKVisualizator implements Steppable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5670879472931245412L;
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.engine.Steppable#step(sim.engine.SimState)
	 */
	@Override
	public void step(SimState simstate) {
		BigMarketSimulation simulation = (BigMarketSimulation) simstate;

		if (simulation.getGui() != null) {
			if(simulation.getNodeManager() != null && 
					simulation.getNodeManager().isPendingUsers()){
				this.updateUsersPosition(simulation);

				Continuous2D usersField2D = new Continuous2D(0.1, simulation
						.getGui().getDisplay().getSize().getHeight(),
						simulation.getGui().getDisplay().getSize().getWidth());
				simulation.setNodeFields2D(usersField2D);


				for (User user : simulation.getNodes()) {
					simulation.getNodeFields2D().setObjectLocation(
							user,
							new Double2D(user.getPosition()[0], user
									.getPosition()[1]));
				}
				simulation.setNodeFields2D(usersField2D);
				simulation.getNodeManager().setPendingUsers(false);
		}
		}
	}

	/**
	 * Build a scale free network using simulation parameters.
	 * 
	 * @return
	 */
	private void updateUsersPosition(BigMarketSimulation simulation) {
		if (simulation.getNodeManager().isPendingUsers()){
			Network network = simulation.getNetwork();
			Graph<User, Connection> graph = new SparseMultigraph<User, Connection>();
			// Create graph
			Bag usersBag = network.getAllNodes();
			for (Object o : usersBag) {
				User user = (User) o;
				graph.addVertex(user);
			}
			for (Object o : usersBag) {
				User user = (User) o;
				Bag linksFromUserBag = network.getEdgesOut(user);
				for (Object l : linksFromUserBag) {
					Edge edge = (Edge) l;
					Connection link = (Connection) edge.getInfo();
					if (link.getA().equals(user)) {
						graph.addEdge(link, link.getA(),
								link.getB());
					} else {
						logger.severe("Not consistent link.");
					}
				}
			}
			
			KKLayout<User, Connection> layout = new KKLayout<User, Connection>(graph);
			BasicVisualizationServer<User, Connection> vv = new BasicVisualizationServer<User, Connection>(
					layout);
			vv.setPreferredSize(new Dimension(simulation.getNetworkDimension(),
					simulation.getNetworkDimension())); // Sets the viewing

			// Updates positions
			List<User> users = new ArrayList<User>(graph.getVertices());
			for (User user : users) {
				double[] position = new double[2];
				position[0] = layout.getX(user);
				position[1] = layout.getY(user);
				user.setPosition(position);

			}
			
						
		}else{
			for (User user : simulation.getNodes()) {
			double[] oldPosition = user.getPosition();
			double[] position = new double[2];
			position[0] = oldPosition[0];
			position[1] = oldPosition[1];
			user.setPosition(position);
			}
			
		}
		
			
	}
}


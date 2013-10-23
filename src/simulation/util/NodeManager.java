package simulation.util;

import java.util.logging.Logger;

import sim.engine.SimState;
import sim.engine.Steppable;
import simulation.BigMarketSimulation;
import simulation.model.link.Connection;
import simulation.model.node.User;

/**
 * This class manage the creation of new nodes
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */

public class NodeManager implements Steppable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3441370965978536003L;
	private boolean pendingUsers;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public NodeManager(){
		this.pendingUsers = false;
	}


	@Override
	public void step(SimState simstate) {
		
		BigMarketSimulation sim = (BigMarketSimulation) simstate;
		double random = sim.random.nextDouble();
		if(random < 0.2){
			logger.info("Nodo random creado!!!");
			User node = new User(sim.getNodes().size());
			int index = (int) (Math.random()*sim.getNumberOfNodes()+1);
			if(index == node.getId()){
				index = (int) (Math.random()*sim.getNumberOfNodes()+1);
			}
			sim.getNodes().add(node);
			sim.getNetwork().addNode(node);
			sim.createNewLink(node, sim.getNodes().get(index-1));
			sim.setNumberOfNodes(sim.getNodes().size() + 1);
			this.setPendingUsers(true);
		}
		
		
	}


	/**
	 * @return the pendingUsers
	 */
	public boolean isPendingUsers() {
		return pendingUsers;
	}


	/**
	 * @param pendingUsers the pendingUsers to set
	 */
	public void setPendingUsers(boolean pendingUsers) {
		this.pendingUsers = pendingUsers;
	}

}

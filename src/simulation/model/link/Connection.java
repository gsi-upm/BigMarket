package simulation.model.link;

import java.util.List;
import java.util.logging.Logger;

import simulation.model.node.User;

/**
 * This class represent the connections between each node
 * 
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */
public class Connection {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private User[] nodesConnected;
    private boolean isBidirectional;
    private int id;
	
    
    /**
     * Constructor that only need the id of the connection
     * 
     * @param id
     */
    public Connection(int id){
    	this.nodesConnected = new User[2];
    	this.id = id;
    }
    
    
    /**
     * Constructor that need the id and the two nodes connected
     * @param id
     * @param node1
     * @param node2
     */
    public Connection(int id, User node1, User node2){
    	this.nodesConnected = new User[2];
    	this.id = id;
    	this.nodesConnected[0] = node1;
    	this.nodesConnected[1] = node2;  	
    }
    
    
    /**
     * This method returns the first node of a connection
     * 
     * @return
     */
    public User getA(){
    	return this.nodesConnected[0];
    }
    
    /**
     * This method returns the second node of a connection
     * 
     * @return
     */
    public User getB(){
    	return this.nodesConnected[1];
    }

	/**
	 * This method return the two nodes connected
	 * 
	 * @return the nodesConnected
	 */
	public User[] getNodesConnected() {
		return nodesConnected;
	}



	/**
	 * This method set the nodes connected
	 * 
	 * @param nodesConnected the nodesConnected to set
	 */
	public void setNodesConnected(User[] nodesConnected) {
		this.nodesConnected = nodesConnected;
	}
	
	
	/**
	 * This method set the first node connected
	 * @param n
	 */
	public void setNodeA(User n){
		this.nodesConnected[0] = n;
	}
	
	
	/**
	 * This method set the second node connected
	 * 
	 * @param n
	 */
	public void setNodeB(User n){
		this.nodesConnected[1] = n;
		
	}



	/**
	 * This method represent if node A can send message to node B and viceversa or only A to B
	 * 
	 * @return the isBidirectional
	 */
	public boolean isBidirectional() {
		return isBidirectional;
	}

	/**
	 * Set if a connection is Bidirectional or not
	 * 
	 * @param isBidirectional the isBidirectional to set
	 */
	public void setBidirectional(boolean isBidirectional) {
		this.isBidirectional = isBidirectional;
	}
	
	/**
	 * Return the connection id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the connection id
	 * 
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


}

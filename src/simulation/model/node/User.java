package simulation.model.node;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sim.engine.SimState;
import sim.engine.Steppable;
import simulation.BigMarketSimulation;
import simulation.model.Rol;
import simulation.model.link.Connection;

/**
 * 
 * This class represents the diferent nodes 
 * 
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */

public class User implements Steppable{

	private static final long serialVersionUID = 4928926457530568268L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private double[] position;
	private int id;
	private String name;
	private Rol rol;
	
	private List<Connection> connections;
	
	
	/**
	 * Constructor that need the id of the user
	 * 
	 * @param id
	 */
	public User(int id){
		this.setId(id);
		this.connections = new ArrayList<Connection>();
		logger.info("User " + id + " sucessfully created");
	}
	
	/**
	 * Constructor that need the id and the rol/roles of the user
	 * 
	 * 
	 */
	
	public User(int id, Rol rol){
		this.setId(id);
		this.connections = new ArrayList<Connection>();
		this.setRol(rol);
	}
	
	/**
	 * Return the user position in the grid
	 * 
	 * @return the position
	 */
	public double[] getPosition() {
		return position;
	}
	
	
	/**
	 * Set the user position in the grid
	 * 
	 * @param position the position to set
	 */
	public void setPosition(double[] position) {
		this.position = position;
	}
	
	/**
	 * Return the user name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the user name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the user id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the user id
	 * 
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * This method specifies the behaviour of each user
	 * 
	 */
	public void step(SimState simState) {
		
		
	}

	/**
	 * Return the connection of the user
	 * 
	 * @return the connections
	 */
	public List<Connection> getConnections() {
		return connections;
	}

	/**
	 * Set the user connections
	 * 
	 * @param connections the connections to set
	 */
	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	/**
	 * @return the rol
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * @param rol the rol to set
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

}

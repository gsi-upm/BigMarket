package simulation;

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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sim.engine.SimState;
import simulation.model.User;
import simulation.util.EventManager;
import simulation.util.GraphManager;
import simulation.util.NetworkGrowth;
import simulation.util.Statistics;

/**
 * This class represents the simulation an manage it
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 * 
 */
public class Simulation extends SimState{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2439742009658189211L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private EventManager eventManager;
	private List<User> users;
	private SimulationGUI gui;
	private GraphManager graphManager;
	private NetworkGrowth networkGrowth;
	private Statistics statistics;
	private int numberOfNodes;
	private int FLAG = 2;
	private int GUIFLAG = 1;

	/**
	 * Main constructor of the class
	 * 
	 * @param seed
	 */
	public Simulation(long seed) {
		super(seed);
		this.users = new ArrayList<>();
	}
	
	/**
	 * This method configures the initial simulation
	 * 
	 */
	public void start(){
		logger.info("Configuring simulation...");
		super.start();
		
		this.graphManager = new GraphManager(this);
		this.statistics = new Statistics(this);
		this.eventManager = new EventManager(this, statistics);
		
		logger.info("Building network...");
		if(FLAG == 0){
			try{
				eventManager.loadEvents();
			}catch(Exception e){
					
			}
			graphManager.runGraphManager(statistics);
		}else if(FLAG == 1){
			try{
				eventManager.loadRandomEvents(numberOfNodes, null);
			}catch(Exception e){
					
			}
			graphManager.runGraphManagerWithoutDataSets(statistics);
		}else{
			eventManager.loadRandomEvents(20, null);
			graphManager.runGraphManagerWithoutDataSets(statistics);
		}
		
		
		this.networkGrowth = new NetworkGrowth(this, this.graphManager);
		this.schedule.scheduleRepeating(networkGrowth, 1, 1);
		
		for(User u : this.getUsers()){
			this.schedule.scheduleRepeating(u, 0, 1);
		}

		this.schedule.scheduleRepeating(eventManager, 1, 1);
		
		
	}

	
	/**
	 * Get the eventManager
	 * 
	 * @return eventManager
	 */
	public EventManager getEventManager() {
		return eventManager;
	}

	/**
	 * This method add an user to the sim
	 * 
	 * @param user
	 */
	public void addUser(User user){
		this.users.add(user);
	}
	
	/**
	 * This method set the eventManager
	 * 
	 * @param eventManager
	 */
	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	/**
	 * Get the users of the sim
	 * 
	 * @return users
	 */
	public List<User> getUsers() {
		return users;
	}

	
	/**
	 * Set the users of the sim
	 * 
	 * @param users
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Get the gui of the sim
	 * 
	 * @return gui
	 */
	public SimulationGUI getGui() {
		return gui;
	}

	/**
	 * Set the gui of the sim
	 * 
	 * @param gui
	 */
	public void setGui(SimulationGUI gui) {
		this.gui = gui;
	}
	
	/**
	 * Get the graphManager of the sim
	 * 
	 * @return graphManager
	 */
	public GraphManager getGraphManager(){
		return this.graphManager;
	}
	
	public void setNumberOfNodes(int numberOfNodes){
		this.numberOfNodes = numberOfNodes;
	}
	
	public void setFlag(int flag){
		this.FLAG = flag;
		
	}
	
	public void setGUIFlag(int flag){
		this.GUIFLAG = flag;
	}
	
	public int getGUIFLag(){
		return this.GUIFLAG;
	}
	
	public static void main(String[]args){
		Simulation simulation = new Simulation(System.currentTimeMillis());
		simulation.setGUIFlag(0);
		simulation.start();
		long steps = 0;
	    while(steps < 1000){
	    	if (!simulation.schedule.step(simulation))
	    		break;
	    	steps = simulation.schedule.getSteps();
	    	if (steps % 50 == 0)
	    		System.out.println("Steps: " + steps + " Time: " + simulation.schedule.getTime());
	    		
	     	}
	    simulation.finish();
	}
	
	}


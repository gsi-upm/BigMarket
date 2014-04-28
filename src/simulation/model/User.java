package simulation.model;

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

import org.graphstream.graph.Node;
import org.ibex.nestedvm.UsermodeConstants;

import sim.engine.SimState;
import sim.engine.Steppable;
import simulation.Simulation;
import simulation.model.behaviours.AcquaintanceBehaviour;
import simulation.model.behaviours.BroadcasterBehaviour;
import simulation.model.behaviours.OddUserBehaviour;
import simulation.model.behaviours.RandomBehaviour;
import simulation.model.event.Follow;
import simulation.model.event.Tweet;
import simulation.util.Constants;


/**
 * Class that represents the users (agents) in the simulation
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */

public class User implements Steppable{

	private static final long serialVersionUID = 8516058208724183319L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private int id;
	private String userID;
	private String userName;
	private List<User> followed;
	private List<User> followers;
	private List<User> inList;
	private List<Tweet> timeline;
	private String type;
	private UserModel random;
	private UserModel acq;
	private UserModel odd;
	private UserModel broad;

	 
	
	
	/**
	 * Main constructor of the class
	 * 
	 * @param id main identifier (it will be used in the noSQL database)
	 * @param userID identifier used in the sim
	 * @param userName name of the user
	 */
	public User(int id, String userID, String userName){
		this.setId(id);
		this.setUserID(userID);
		this.setUserName(userName);
		this.followed = new ArrayList<User>();
		this.followers = new ArrayList<User>();
		this.random = new RandomBehaviour("RandomBehaviour");
		this.acq = new AcquaintanceBehaviour("AcquaintanceBehaviour");
		this.odd = new OddUserBehaviour("OddBehaviour");
		this.broad = new BroadcasterBehaviour("BroadCasterBehaviour");

	}
	
	/**
	 * This method add followers
	 * 
	 * @param user
	 */
	public void addFollower(User user){
			followers.add(user);	
	}
	
	/**
	 * This method add followeds
	 * 
	 * @param user
	 */
	public void addFollowed(User user){
			followed.add(user);
	}
	
	
	/**
	 * This method remove followers
	 * 
	 * @param user
	 */
	public void removeFollower(User user){
		if(followers.contains(user)){
			followers.remove(user);
		}
	}
	
	/**
	 * This method remove followeds
	 * @param user
	 */
	public void removeFollowed(User user){
		if(followed.contains(user)){
			followed.remove(user);
		}
	}
	
	/**
	 * This method is the engine of the agent bevahiour
	 * 
	 */
	@Override
	public void step(SimState sim) {
		Simulation simulation = (Simulation) sim;
		if(this.getType() == Constants.USER_TYPE_ACQUAINTANCES){
			acq.userBehaviour(simulation, this);
		}if(this.getType() == Constants.USER_TYPE_BROADCASTER){
			broad.userBehaviour(simulation, this);
		}if(this.getType() == Constants.USER_TYPE_ODDUSERS){
			odd.userBehaviour(simulation, this);
		}else{
			random.userBehaviour(simulation, this);
		}
	}
	
	/**
	 * Get the user id (noSQL id)
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set the user id (noSQL id)
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get the user id (sim id)
	 * 
	 * @return id
	 */
	public String getUserID() {
		return userID;
	}
	
	/**
	 * Set the user id (sim id)
	 * 
	 * @param userID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	/**
	 * Get the user name
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Set the user name
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Get all the followeds of the user 
	 * 
	 * @return followed
	 */
	public List<User> getFollowed() {
		return followed;
	}
	
	/**
	 * Set all the followeds of the user
	 * 
	 * @param followed
	 */
	public void setFollowed(List<User> followed) {
		this.followed = followed;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<User> getInList() {
		return inList;
	}
	public void setInList(List<User> inList) {
		this.inList = inList;
	}
	
	/**
	 * Get the time line of the user
	 * 
	 * @return timeline
	 */
	public List<Tweet> getTimeline() {
		return timeline;
	}
	
	/**
	 * Set the timeLine of the user
	 * 
	 * @param timeline
	 */
	public void setTimeline(List<Tweet> timeline) {
		this.timeline = timeline;
	}
	
	/**
	 * Get all the followers of the user
	 * 
	 * @return followers
	 */
	public List<User> getFollowers() {
		return followers;
	}
	
	/**
	 * Set all the followers of the user
	 * 
	 * @param followers
	 */
	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}

}

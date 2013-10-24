package simulation.model;

import java.util.List;

import simulation.model.node.User;


/**
 * This class represent the rol that a user can assume
 * 
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */
public class Rol {

	private List<User> friends;
	private boolean informationSeeker;
	private boolean informationProducer;

	
	private String name;
	
	public Rol(String name, List<User> friend, boolean seeker, boolean producer){
		this.setName(name);
		this.setFriends(friend);
		this.setInformationProducer(producer);
		this.setInformationSeeker(seeker);
	}

	/**
	 * @return the friends
	 */
	public List<User> getFriends() {
		return friends;
	}

	/**
	 * @param friend the list of friends to set
	 */
	public void setFriends(List<User> friend) {
		this.friends = friend;
	}
	
	/**
	 * @param user a user to add as friend
	 */
	public void setFriend(User user){
		this.friends.add(user);
	}
	
	/**
	 * @return friend a friend of the user
	 * 
	 */
	public User getFriend(int index){
		return this.friends.get(index);
	}
	
	
	/**
	 * 
	 * 
	 * @param id of the user to get
	 * @return the user
	 */
	public User getFriendByID(int id){
		for(int i = 0; i < this.getFriends().size(); i++){
			if(this.getFriend(i).getId() == id){
				return this.getFriend(i);
			}
		}
		
		return null;
		
	}

	/**
	 * @return the informationSeeker
	 */
	public boolean isInformationSeeker() {
		return informationSeeker;
	}

	/**
	 * @param informationSeeker the informationSeeker to set
	 */
	public void setInformationSeeker(boolean informationSeeker) {
		this.informationSeeker = informationSeeker;
	}

	/**
	 * @return the informationProducer
	 */
	public boolean isInformationProducer() {
		return informationProducer;
	}

	/**
	 * @param informationProducer the informationProducer to set
	 */
	public void setInformationProducer(boolean informationProducer) {
		this.informationProducer = informationProducer;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}

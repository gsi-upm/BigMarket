package simulation.model;


/**
 * This class represent the rol that a user can assume
 * 
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */
public class Rol {

	private boolean friend;
	private boolean informationSeeker;
	private boolean informationProducer;
	
	private String name;
	
	public Rol(String name, boolean friend, boolean seeker, boolean producer){
		this.setName(name);
		this.setFriend(friend);
		this.setInformationProducer(producer);
		this.setInformationSeeker(seeker);
	}

	/**
	 * @return the friend
	 */
	public boolean isFriend() {
		return friend;
	}

	/**
	 * @param friend the friend to set
	 */
	public void setFriend(boolean friend) {
		this.friend = friend;
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


/**
*
*
* This file is part of BigMarket.
*
* BigMarket has been developed by members of the research Group on
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes),
* acknowledged group by the Technical University of Madrid [UPM]
* (Universidad Politécnica de Madrid)
*
* Authors:
* Daniel Lara
* Carlos A. Iglesias
* Emilio Serrano
*
* Contact:
* http://www.gsi.dit.upm.es/;
*
*
*
* BigMarket is free software:
* you can redistribute it and/or modify it under the terms of the GNU
* General Public License as published by the Free Software Foundation,
* either version 3 of the License, or (at your option) any later version.
*
*
* BigMarket is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with VoteSim. If not, see <http://www.gnu.org/licenses/>
*/

package simulation.model.event;

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

import simulation.model.Event;
import simulation.model.User;

/**
 * Subclass of Event that represents the action of Follow between Users
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */

public class Follow extends Event{
	
	
	/**
	 * The followed
	 */
	private User target;
	
	/**
	 * The follower
	 */
	private User origin;

	/**
	 * Constructor of the class
	 * 
	 * @param name and identifier of the relationship
	 * @param timeStamp
	 * @param target user followed
	 * @param origin follower
	 */
	public Follow(String name, String timeStamp, User target, User origin) {
		super(name, timeStamp);
		this.setTarget(target);
		this.setOrigin(origin);
		this.origin.addFollowed(target);
		this.target.addFollower(origin);
	}

	/**
	 * Get the followed
	 * 
	 * @return target the users that is being followed
	 */
	public User getTarget() {
		return target;
	}

	/**
	 * Set the followed
	 * 
	 * @param target the user that is going to be followed
	 */
	public void setTarget(User target) {
		this.target = target;
	}

	
	/**
	 * Get the follower
	 * 
	 * @return the follower
	 */
	public User getOrigin() {
		return origin;
	}

	/**
	 * Set the follower
	 * 
	 * @param origin the follower
	 */
	public void setOrigin(User origin) {
		this.origin = origin;
	}

}

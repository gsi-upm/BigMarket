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

import simulation.model.User;

/**
 * Subclass of Follow that represents the action of unfollow
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */
public class Unfollow extends Follow{

	private User target;
	private User origin;
	
	public Unfollow(String name, String timeStamp, User target, User origin) {
		super(name, timeStamp, target, origin);
		target.removeFollower(origin);
		origin.removeFollowed(target);
	}
	
	/**
	 * Set the person to unfollow
	 */
	public void setTarget(User target){
		this.target = target;
	}
	
	
	/**
	 * Get the person to unfollow
	 */
	public User getTarget(){
		return this.target;
	}
	
	/**
	 * Set who will do the unfollow
	 */
	public void setOrigin(User origin){
		this.origin = origin;
	}
	
	/**
	 * Get who will do the unfollow
	 */
	public User getOrigin(){
		return this.origin;
	}

}

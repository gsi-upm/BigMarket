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

/**
 * Represents the basic options of an event
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */
public abstract class Event {
	
	private String id;
	private String timeStamp;
	
	public Event(String name, String timeStamp){
		this.setTimeStamp(timeStamp);
		this.setId(name+timeStamp);
	}

	
	/**
	 * This method returns the id of the event
	 * 
	 * @return id of the event
	 */
	public String getId() {
		return id;
	}
	

	/**
	 * This method sets the id of the event
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	/**
	 * This method returns the timeStamp of the event
	 * 
	 * @return timeStamp
	 */
	public String getTimeStamp(){
		return timeStamp;
	}
	
	
	/**
	 * This method sets the timeStamp of the event
	 * @param timeStamp
	 */
	public void setTimeStamp(String timeStamp){
		this.timeStamp = timeStamp;
	}

}

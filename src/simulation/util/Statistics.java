package simulation.util;

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

import simulation.Simulation;

/**
 * This class represents the statistics of the sim
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 */

public class Statistics{
	
	private Simulation sim;
	private int totalNumberOfTweets;
	private int oddTweets;
	private int acqTweets;
	private int broadTweets;
	
	
	/**
	 * Main constructor of the sim
	 * 
	 * @param sim
	 */
	public Statistics(Simulation sim){
		this.sim = sim;
		this.totalNumberOfTweets = 0;
	}
	
	/**
	 * This class represents the number of agents of the sim
	 * 
	 * @return number of the agents
	 */
	public int numberOfAgents(){
		System.out.println("Numero agentes " + sim.getEventManager().getNumberOfAgents());
		return sim.getEventManager().getNumberOfAgents();
		
	}

	/**
	 * Get the total number of tweets in the sim
	 * 
	 * @return totalNumberOfTweets
	 */
	public int getTotalNumberOfTweets(){
		return totalNumberOfTweets;
	}
	
	/**
	 * This method increase the number of tweets each time one tweet is generated
	 */
	public void increaseTotalNumberOfTweets(){
		this.totalNumberOfTweets++;
		System.out.println("NUMERO TOTAL DE TWEETS " + this.totalNumberOfTweets);
	}
	
	public void increaseOddTweets(){
		this.oddTweets++;
	}
	
	public void increaseBroadTweets(){
		this.broadTweets++;
	}
	
	public void increaseAcqTweets(){
		this.acqTweets++;
	}
	
	public int getOddTweets(){
		return this.oddTweets;
	}
	
	public int getAcqTweets(){
		return this.acqTweets;
	}
	
	public int getBroadTweets(){
		return this.broadTweets;
	}
	
}


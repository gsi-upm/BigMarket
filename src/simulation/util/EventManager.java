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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.graphstream.graph.Node;

import sim.engine.SimState;
import sim.engine.Steppable;
import simulation.Simulation;
import simulation.model.Event;
import simulation.model.User;
import simulation.model.event.Follow;
import simulation.model.event.Tweet;

/**
 * This class manage all the events that happen in the sim
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 * 
 */
public class EventManager implements Steppable{
	
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 3035231174520889738L;
	private List<Event> events;
	private Simulation sim;
	private int simStatus;
	private int numberOfAgents;
	private Statistics statistics;
	
	
	/**
	 * Main constructor of the class
	 * 
	 * @param sim
	 * @param statistics
	 */
	public EventManager(Simulation sim, Statistics statistics){
		this.setSim(sim);
		this.statistics = statistics;
		this.events = new ArrayList<Event>();
		this.simStatus = 0;
	}
	
	/**
	 * This method loads all the events that happens in the sim
	 * 
	 * @return
	 */
	public List<Event> loadEvents(){
		int numberOfAgents = 0;
		//File fileNodes = new File("/home/dlara/PFC/BigMarketv2/BigMarketv2/src/simulation/util/nodes.txt");
		File fileNodes = new File("/home/dlara/Twitter Dataset/nodes.csv");
		try (BufferedReader reader = new BufferedReader(new FileReader(fileNodes))){
			while(true){
				String line = reader.readLine();	
				if (line == null || line.equals("")) {
                    break;
                }
				numberOfAgents++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.numberOfAgents = numberOfAgents;
		return events;
		
	}
	
	/**
	 * This method loads random events and a specific number of agents
	 * 
	 * @param numberOfAgents
	 * @param event
	 * @return
	 */
	public List<Event> loadRandomEvents(int numberOfAgents, List<Event> event){
		this.numberOfAgents = numberOfAgents;
		this.events = event;
		return events;
	}
	
	public List<Event> loadFromDataBase(Neo4JManageTool neo, List<Event> event){
		this.numberOfAgents = neo.getNodes().size();
		this.events = event;
		return events;
	}
	
	
//	/**
//	 * This method generate random tweets 
//	 * 
//	 * @param sim
//	 */
//	private void generateRandomTweet(Simulation sim){
//		for(int n = 0; n < sim.getGraphManager().getGraph().getNodeCount(); n++){
//			Node node = sim.getGraphManager().getGraph().getNode(n);
//			node.removeAttribute("ui.class");
//		}
//		double prob = Math.random();
//		if(prob < 0.8){
//			int user = (int) (Math.random()*numberOfAgents)+1;
//			User source = sim.getUsers().get(user);
//			Node n = sim.getGraphManager().getGraph().getNode(source.getId());
//			n.addAttribute("ui.class", "sender");
//			Calendar cal = Calendar.getInstance();
//			Tweet tweet = new Tweet("Tweet " + source.getId(), " " + cal.getTime() , source, null, -1);
//			logger.info("El usuario " + source.getUserName() + " ha creado el " + tweet.getId());
//			n.addAttribute("ui.class", "sender");
//			statistics.increaseTotalNumberOfTweets();
//			List<User> users = tweet.getTargets(tweet);
//			for(User u:users){
//				logger.info("El usuario " + u.getUserName() + " ha leido el " + tweet.getId());
//				Node n1 = sim.getGraphManager().getGraph().getNode(u.getId());
//				n1.addAttribute("ui.class", "reciever");
//			}
//			if(users.size() == 0){
//				logger.info("El usuario " + source.getUserName() + " no tiene followers");
//			}
//		}
//		
//	}
//	private void generateRandomFollows(){
//		double prob = Math.random();
//		if(prob < 0.1){
//			int user1 = (int) (Math.random()*numberOfAgents)+1;
//			int user2 = (int) (Math.random()*numberOfAgents)+1;
//			while(user1 == user2){
//				user2 = (int) (Math.random()*numberOfAgents)+1;
//			}
//			User u1 = sim.getUsers().get(user1);
//			User u2 = sim.getUsers().get(user2);
//			Follow f = new Follow("Follow " + u1.getUserName() + "-->" + u2.getUserName() , 
//					"A", u2, u1);
//			Node n1 = sim.getGraphManager().getGraph().getNode(u1.getId());
//			Node n2 = sim.getGraphManager().getGraph().getNode(u2.getId());
//			sim.getGraphManager().getGraph().addEdge("Edge " + u1.getUserName() + "-->" + u2.getUserName()
//					, n1, n2, true);
//		}
//	}


	/**
	 * This method manage the behaviour of EventManager
	 */
	@Override
	public void step(SimState sim) {
		Simulation simulation = (Simulation) sim;
		setSimStatus(simulation.getGui().getConsole().getPlayState());

	}

	/**
	 * Get the sim
	 * 
	 * @return sim
	 */
	public Simulation getSim() {
		return sim;
	}

	/**
	 * Set the sim
	 * 
	 * @param sim
	 */
	public void setSim(Simulation sim) {
		this.sim = sim;
	}

	/**
	 * Get the number of agents
	 * 
	 * @return numberOfAgents
	 */
	public int getNumberOfAgents(){
		return this.numberOfAgents;
	}
	
	public void setStatistics(Statistics stat){
		this.statistics = stat;
	}
	
	public Statistics getStatistics(){
		return this.statistics;
	}
	
	public void setSimStatus(int a){
		this.simStatus = a;
	}
	
	public int getSimStatus(){
		return this.simStatus;
	}
	
	//TODO
	//Los tweets son generados por los users y añadidos a la lista de eventos pendientes, 
	//cuando se vuelva a ejecutar el siguiente step del eventManager se recorre la lista
	//y se van generando los eventos contenidos en ella.
	
}
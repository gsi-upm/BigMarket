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


package simulation.model.behaviours;

import java.util.Calendar;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import simulation.Simulation;
import simulation.model.User;
import simulation.model.UserModel;
import simulation.model.event.Follow;
import simulation.model.event.Tweet;
import simulation.util.Constants;
import simulation.util.Statistics;

public class BroadcasterBehaviour extends UserModel{

	public BroadcasterBehaviour(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	private void generateTweet(Simulation sim){
		int numberOfAgents = sim.getEventManager().getNumberOfAgents();
		Statistics statistics = sim.getEventManager().getStatistics();
		for(int n = 0; n < sim.getGraphManager().getGraph().getNodeCount(); n++){
			Node node = sim.getGraphManager().getGraph().getNode(n);
			node.removeAttribute("ui.class");
		}
		double prob = Math.random();
		if(prob < 0.5){
			int user = (int) (Math.random()*numberOfAgents);
			User source = sim.getUsers().get(user);
			Node n = sim.getGraphManager().getGraph().getNode(source.getId());
			n.addAttribute("ui.class", "sender");
			Calendar cal = Calendar.getInstance();
			Tweet tweet = new Tweet("Tweet " + source.getId(), " " + cal.getTime() , source, null, -1);
			n.addAttribute("ui.class", "sender");
			statistics.increaseBroadTweets();
			List<User> users = tweet.getTargets(tweet);
			for(User u:users){
				Node n1 = sim.getGraphManager().getGraph().getNode(u.getId());
				n1.addAttribute("ui.class", "reciever");
			}
			if(users.size() == 0){
			}
		}
		
	}
	
	private void generateFollows(Simulation sim){
		int numberOfAgents = sim.getEventManager().getNumberOfAgents();
//		for(User u:sim.getUsers()){
//		}
		double prob = Math.random();
		if(prob < 0.01){
			int user1 = (int) (Math.random()*numberOfAgents);
			int user2 = (int) (Math.random()*numberOfAgents);
			while(user1 == user2){
				user2 = (int) (Math.random()*numberOfAgents);
			}
			User u1 = sim.getUsers().get(user1);
			User u2 = sim.getUsers().get(user2);
			if(!u2.getFollowers().contains(u1)){
				Follow f = new Follow("Follow " + u1.getUserName() + "-->" + u2.getUserName() , 
						"A", u2, u1);
				Node n1 = sim.getGraphManager().getGraph().getNode(u1.getId());
				Node n2 = sim.getGraphManager().getGraph().getNode(u2.getId());
				sim.getGraphManager().getGraph().addEdge("Edge " + u1.getUserName() + "-->" + u2.getUserName()
						, n1, n2, true);
			}
		}
	}
	
	private void setUserType(Simulation sim, User user){
		Graph graph = sim.getGraphManager().getGraph();
		double enteredEdges = 0.0;
		double totalEdges = graph.getEdgeCount();
		double percentage = 0.0;
		enteredEdges = user.getFollowers().size();
		percentage = enteredEdges/totalEdges;
			if(percentage >= 0.3){ //0.00275
				user.setType(Constants.USER_TYPE_BROADCASTER);
				//System.out.println("EL usuario " + user.getUserName() + " es un " + Constants.USER_TYPE_BROADCASTER);
			}else if(0.3 > percentage &&  percentage >= 0.1){ //0.00275 && 0.000121
				user.setType(Constants.USER_TYPE_ACQUAINTANCES);
				//System.out.println("EL usuario " + user.getUserName() + " es un " + Constants.USER_TYPE_ACQUAINTANCES);
			}else{
				user.setType(Constants.USER_TYPE_ODDUSERS);
				//System.out.println("EL usuario " + user.getUserName() + " es un " + Constants.USER_TYPE_ODDUSERS);
			}
	}
	@Override
	public void userBehaviour(Simulation sim, User user) {
		generateFollows(sim);
		generateTweet(sim);
		setUserType(sim, user);
	}

}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import simulation.model.Event;
import simulation.model.User;
import simulation.util.Polarity;
import simulation.util.Topic;

/**
 * Subclass of event that represents when an user create a tweet (it can be a RT)
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 *
 */
public class Tweet extends Event{
	
	/**
	 * The author of the tweet
	 */
	private User author;
	
	/**
	 * The source of the tweet, in a RT the original author of the tweet
	 */
	private User source;
	
	/**
	 * Indicates if the tweet is a RT
	 */
	private int rt;
	
	/**
	 * Indicates the polarity of the user towards determinate topics
	 */
	private HashMap<Topic, Polarity> sentimentsForTopics;
	
	/**
	 * Indicates the polarity of the user towards determinate topics
	 */
	private HashMap<Topic, Polarity> hashTags;
	
	/**
	 * Indicates the replies to the tweet
	 */
	private List<User> replies;
	
	/**
	 * Indicates which users are mentioned in the tweet
	 */
	private List<User> mentions;
	

	/**
	 * Constructor of the class
	 * 
	 * @param name identifier of the tweet
	 * @param timeStamp
	 * @param source the sourcer of the tweet
	 * @param tweet used if is a RT, in that case the tweet copy the fields of the original tweet
	 * @param rt indicates if the tweet is a RT
	 */
	public Tweet(String name, String timeStamp, User source, Tweet tweet, int rt) {
		super(name, timeStamp);
		this.setSource(source);
		this.setRT(rt);
		if(!isRT()){
			setAuthor(source);
		}else{
			setPropertiesFromTweet(tweet);
		}
	}

	/**
	 * Indicates if a tweet is a RT
	 * 
	 * @return true if is a RT, false in the other case.
	 */
	public boolean isRT(){
		if(rt == -1){
			return false;
		}
		return true;
	}
	
	
	/**
	 * If the tweet is a RT, copy all the fields of the original tweet
	 */
	private void setPropertiesFromTweet(Tweet tweet){
		this.setAuthor(tweet.getAuthor());
		this.setSource(tweet.getSource());
		this.setRT(tweet.getRT());
		this.setSentimentsForTopics(tweet.getSentimentsForTopics());
		this.setHashTags(tweet.getHashTags());
		this.setReplies(tweet.getReplies());
		this.setMentions(tweet.getMentions());
	}
	
	
	/**
	 * Set the sentiments for topics
	 * 
	 * @param topic
	 * @param pol
	 */
	public void setSentimentsForTopics(Topic topic, Polarity pol){
		sentimentsForTopics.put(topic, pol);	
	}
	
	/**
	 * Set the polarity to hashtags
	 * 
	 * @param topic
	 * @param pol
	 */
	public void setHashTags(Topic topic, Polarity pol){
		hashTags.put(topic, pol);
	}
	
	/**
	 * Get all the users that receive directly the tweet
	 * 
	 * @param tweet
	 * @return targets with all the users that receives the tweet
	 */
	public List<User> getTargets(Tweet tweet){
		List<User> targets = new ArrayList<>();
		//TODO Implement mentions
//		for(User user : mentions){
//			targets.add(user);
//		}
		if(isRT()){
			targets.add(tweet.getSource());
		}
		User user = tweet.getAuthor();
		for(User u : user.getFollowers()){
			targets.add(u);
		}
		
		return targets;
		
	}
	

	/**
	 * Get the author of the tweet
	 * 
	 * @return
	 */
	public User getAuthor() {
		return author;
	}


	/**
	 * Set the author of the tweet
	 * @param author
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

	/**
	 * Get the source of the tweet
	 * @return
	 */
	public User getSource() {
		return source;
	}

	/**
	 * Set the source of the tweet
	 * @param source
	 */
	public void setSource(User source) {
		this.source = source;
	}

	/**
	 * Get the RT param in order to know if it is a RT
	 * @return
	 */
	public int getRT() {
		return rt;
	}

	/**
	 * Set the RT param
	 * @param rT
	 */
	public void setRT(int rT) {
		rt = rT;
	}

	/**
	 * Get the sentiments for topics
	 * @return
	 */
	public HashMap<Topic, Polarity> getSentimentsForTopics() {
		return sentimentsForTopics;
	}

	/**
	 * Set the whole list of sentiments for topics
	 * @param sentimentsForTopics
	 */
	public void setSentimentsForTopics(HashMap<Topic, Polarity> sentimentsForTopics) {
		this.sentimentsForTopics = sentimentsForTopics;
	}

	/**
	 * Get the polarity of hashtags
	 * @return
	 */
	public HashMap<Topic, Polarity> getHashTags() {
		return hashTags;
	}

	/**
	 * Set the whole list of hashtags
	 * @param hashTags
	 */
	public void setHashTags(HashMap<Topic, Polarity> hashTags) {
		this.hashTags = hashTags;
	}


	/**
	 * Get the replies of the tweet
	 * @return
	 */
	public List<User> getReplies() {
		return replies;
	}

	/**
	 * Set the whole list of replies to the tweet
	 * @param replies
	 */
	public void setReplies(List<User> replies) {
		this.replies = replies;
	}

	/**
	 * Get the mentions in the tweet
	 * @return
	 */
	public List<User> getMentions() {
		return mentions;
	}

	/**
	 * Set the whole list of mentions in the tweet
	 * @param mentions
	 */
	public void setMentions(List<User> mentions) {
		this.mentions = mentions;
	}

}
package simulation.model;

import simulation.Simulation;

public abstract class UserModel {
	
	private String name;
	
	public UserModel(String name){
		this.name = name;
	}
	
	public abstract void userBehaviour(Simulation sim);
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

}

package simulation;

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

import javax.swing.JFrame;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import simulation.util.Neo4JManageTool;

/**
 * This class is used to create the gui of the simulation
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 * 
 */
public class SimulationGUI extends GUIState{

	private Simulation simulation;
	private Display2D display2D;
	private JFrame networkFrame2D;
	private Console console;
	
	
	/**
	 * Main constructor of the class
	 * 
	 * @param simulation
	 */
	public SimulationGUI(SimState simulation) {
		super(simulation);
		this.simulation = (Simulation) simulation;
		this.console = new Console(this);
		this.simulation.setGui(this);
		this.createController();
	}
	
	/**
	 * Get the gui controller
	 * 
	 * @return controller
	 */
	public Controller getController(){
		return this.controller;
	}
	
	/**
	 * This method initiates the gui
	 */
	public void start(){
		super.start();
	}
	
	/**
	 * This method finish the gui
	 */
	public void finish(){
		try{
			if(this.display2D != null){
				this.display2D.reset();
			}
		}catch(Exception e){
			
		}
	}
	
	/**
	 * This method load a simulation
	 */
	public void load(SimState simulation){
		super.load(simulation);
	}
	
	
	public Object getSimulationInspectedObject() {
		return this.simulation;
	}
	
	/**
	 * This method configures the intial values of the gui
	 */
	public void init(Controller c){
		super.init(c);
		this.networkFrame2D = new JFrame("Twitter Network Graph");
		this.networkFrame2D.setSize(1000, 1000);
		c.registerFrame(this.networkFrame2D);
		this.networkFrame2D.setVisible(true);
		

	}
	
	/**
	 * This method closes the gui
	 */
	public void quit(){
		super.quit();
		
		if (this.networkFrame2D != null) {
			this.networkFrame2D.dispose();
		}
		super.quit();
		this.networkFrame2D = null;
		this.display2D = null;
	}
	
	/**
	 * @return the simulation
	 */
	public Simulation getSimulation() {
		return simulation;
	}

	/**
	 * @param simulation
	 *            the simulation to set
	 */
	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

	/**
	 * @return the display2D
	 */
	public Display2D getDisplay2D() {
		return display2D;
	}

	/**
	 * @param display2d
	 *            the display2D to set
	 */
	public void setDisplay2D(Display2D display2d) {
		display2D = display2d;
	}
	
	
	/**
	 * @return the networkFrame2D
	 */
	public JFrame getNetworkFrame2D() {
		return networkFrame2D;
	}

	/**
	 * @param networkFrame2D
	 *            the networkFrame2D to set
	 */
	public void setNetworkFrame2D(JFrame networkFrame2D) {
		this.networkFrame2D = networkFrame2D;
	}
	
	public void controlSimulation(String control){
		if(control.equals("pause")){
			console.pressPause();
		}else if(control.equals("play")){
			console.pressPlay();
		}else if(control.equals("stop")){
			console.pressStop();
		}
	}
	
	public static void main(String[]args){
		Simulation simulation = new Simulation(System.currentTimeMillis());
		new SimulationGUI(simulation);

	}

}

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

/**
 * Main launcher of the simulation
 * 
 * @author Daniel Lara Diezma
 * @email daniel.lara.diezma@gmail.com
 * 
 */
public class Launcher {
	
	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[]args){
		Simulation simulation = new Simulation(System.currentTimeMillis());
		new SimulationGUI(simulation);
	}
	
	public void launchWithGUI(Simulation sim){
		new SimulationGUI(sim);
		long steps = 0;
	    while(steps < 500){
	    	steps = sim.schedule.getSteps();
	    }
	    sim.finish();
	}
	
	public void launchWithoutGUI(Simulation sim){
		sim.start();
		long steps = 0;
	    while(steps < 10000){
	    	if (!sim.schedule.step(sim))
	    		break;
	    	steps = sim.schedule.getSteps();
	    	if (steps % 500 == 0)
	    		System.out.println("Steps: " + steps + " Time: " + sim.schedule.getTime());
	    		
	     	}
	    sim.finish();
		
	}
	
	
	
	
	
	
	
}
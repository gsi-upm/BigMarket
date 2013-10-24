package simulation;

import org.rosuda.JRI.Rengine;


public class GUILauncher {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the simulation.
		BigMarketSimulation simulation = new BigMarketSimulation(System.currentTimeMillis());
		// Create the GUI.
		new BigMarketSimulationGUI(simulation);		
	}
	
	
	

}

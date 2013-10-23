package simulation;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import simulation.model.node.User;
import simulation.portrayal.Link2DPortrayal;
import simulation.portrayal.Node2DPortrayal;

public class BigMarketSimulationGUI extends GUIState{
	
	private BigMarketSimulation sim;
	private Display2D display;
	private JFrame frame;
	private NetworkPortrayal2D net2D;
	private ContinuousPortrayal2D con2D;

	public BigMarketSimulationGUI(SimState simulation) {
		super(simulation);
		this.sim = (BigMarketSimulation) simulation;
		this.net2D = new NetworkPortrayal2D();
		this.con2D = new ContinuousPortrayal2D();
		this.sim.setGui(this);
		this.createController();
	}
	
	/**
	 * The controller of the gui is useful to handle charts
	 * (reset charts) and their data. 
	 * 
	 * @return
	 */
	public Controller getController() {
		return this.controller;
	}
	
	/**
     * Creates and returns a controller ready for the user to manipulate. By
     * default this method creates a Console, sets it visible, and returns it.
     * You can override this to provide some other kind of controller.
     */
    public Controller createController() {
        Console console = new Console(this);
        console.setVisible(true);
        return console;
    }


	
	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#start()
	 */
	public void start() {
		super.start();
		this.setupPortrayals();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#finish()
	 */
	public void finish() {
		try {
			if (this.display != null) {
				this.display.reset();
			}
		} catch (Exception e) {
			// Ignore exception
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#load(sim.engine.SimState)
	 */
	public void load(SimState simulation) {
		super.load(simulation);
		this.setupPortrayals();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#getSimulationInspectedObject()
	 */
	public Object getSimulationInspectedObject() {
		return this.sim;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#init(sim.display.Controller)
	 */
	public void init(Controller c) {

		super.init(c);

		// Configure display 2D
		this.display = new Display2D(250,
				250, this);;
		this.display.setScale(3);
		this.display.setClipping(false);
		this.frame = this.display.createFrame();
		this.frame.setTitle("BigMarket 2D");
		c.registerFrame(this.frame);
		this.frame.setVisible(true);

		this.display.attach(this.net2D, "Connections");
		this.display.attach(this.con2D, "Users");


		// Prepare displays
		this.display.reset();
		this.display.setBackdrop(Color.WHITE);
		this.display.repaint();
		this.frame.setLocation(100, 100);


	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see sim.display.GUIState#quit()
	 */
	public void quit() {
		super.quit();
		if (this.frame != null) {
			this.frame.dispose();
		}
		super.quit();
		this.frame = null;
		this.display = null;

	}
	
	
	/**
	 * Setup all portrayals for the GUI.
	 */
	private void setupPortrayals() {

		// 2D Portrayals configuration

		// Portrayals to represent network (links and users).
		this.net2D.setField(new SpatialNetwork2D(this.sim
				.getNodeFields2D(), this.sim.getNetwork()));
		Link2DPortrayal connectionPortrayal2D = new Link2DPortrayal();
		this.net2D.setPortrayalForAll(connectionPortrayal2D);

		this.con2D.setField(this.sim.getNodeFields2D());
		Node2DPortrayal userPortrayal2D = new Node2DPortrayal();
		this.con2D.setPortrayalForClass(User.class,
				userPortrayal2D);


	}

	
	

	/**
	 * @return the sim
	 */
	public BigMarketSimulation getSim() {
		return sim;
	}

	/**
	 * @param sim the sim to set
	 */
	public void setSim(BigMarketSimulation sim) {
		this.sim = sim;
	}

	/**
	 * @return the display
	 */
	public Display2D getDisplay() {
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(Display2D display) {
		this.display = display;
	}

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * @return the net2D
	 */
	public NetworkPortrayal2D getNet2D() {
		return net2D;
	}

	/**
	 * @param net2d the net2D to set
	 */
	public void setNet2D(NetworkPortrayal2D net2d) {
		net2D = net2d;
	}

	/**
	 * @return the con2D
	 */
	public ContinuousPortrayal2D getCon2D() {
		return con2D;
	}

	/**
	 * @param con2d the con2D to set
	 */
	public void setCon2D(ContinuousPortrayal2D con2d) {
		con2D = con2d;
	}

}

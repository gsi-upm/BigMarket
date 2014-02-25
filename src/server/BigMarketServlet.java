package server;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.graphstream.stream.file.FileSinkGEXF;
import org.graphstream.stream.file.FileSinkGEXF2;

import simulation.Launcher;
import simulation.Simulation;
import simulation.model.User;
import simulation.util.Constants;
import simulation.util.Neo4JManageTool;

/**
 * Servlet implementation class BigMarketServlet
 */
public class BigMarketServlet extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = -7634769531465961909L;
	private Simulation sim;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public BigMarketServlet() {
        super();
        System.out.println("CREADO SERVLET");
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("bPress") != null){
			if(request.getParameter("bPress").equals("ros")){	
				clickRunOneStep(request, response);
			}else if(request.getParameter("bPress").equals("run")){
				clickRun(request, response);
			}else if(request.getParameter("bPress").equals("pause")){
				clickPause(request, response);
			}else{
				clickStop(request, response);
			}
		}else{
			launchSimulation(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);	
	}
	
	private void launchSimulation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		sim = new Simulation(System.currentTimeMillis());
		
		if(Integer.parseInt(request.getParameter("nodes")) != 0){
			int numberOfNodes = Integer.parseInt(request.getParameter("nodes"));		
			sim.setNumberOfNodes(numberOfNodes);
			sim.setFlag(1);
		}else{
			
		}
		
		Launcher launcher = new Launcher(sim);
		launcher.start();
		
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute("steps", sim.schedule.getSteps());
		request.setAttribute("sim", sim);
		request.getRequestDispatcher("parameters.jsp").forward(request, response);
		
		
	
//		Neo4JManageTool neoDB = new Neo4JManageTool(sim);
//		neoDB.launchDatabaseTool();
	}
	
	private void clickRunOneStep(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(sim.getGui().getConsole().getPlayState() == 2){
			sim.getGui().getConsole().pressPlay();
		}else{
			sim.getGui().getConsole().pressPause();
			sim.getGui().getConsole().pressPlay();
		}
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute("steps", sim.schedule.getSteps());
		request.setAttribute("sim", sim);
		request.getRequestDispatcher("parameters.jsp").forward(request, response);
	}
	
	private void clickRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(sim.getGui().getConsole().getPlayState() == 0){
			sim.getGui().getConsole().pressPlay();
		}else if(sim.getGui().getConsole().getPlayState() == 2){
			sim.getGui().getConsole().pressPause();
		}
		
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute("steps", sim.schedule.getSteps());
		request.setAttribute("sim", sim);
		request.getRequestDispatcher("parameters.jsp").forward(request, response);
		
		
	}
	
	private void clickPause(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(sim.getGui().getConsole().getPlayState() != 2){
			sim.getGui().getConsole().pressPause();
		}
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute("steps", sim.schedule.getSteps());
		request.setAttribute("sim", sim);
		request.getRequestDispatcher("parameters.jsp").forward(request, response);
	}
	
	private void clickStop(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		sim.getGui().getConsole().pressStop();
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute("steps", sim.schedule.getSteps());
		request.setAttribute("sim", sim);
		
		
		FileSinkGEXF out = new FileSinkGEXF();
		out.writeAll(sim.getGraphManager().getGraph(), "/home/dlara/pruebaDEGEFX.gexf");
		FileSinkGEXF2 out2 = new FileSinkGEXF2();
		out2.writeAll(sim.getGraphManager().getGraph(), "/home/dlara/pruebaDEGEFX2.gexf");
		
		//En realidad me da la pantalla de resultados
		//request.getRequestDispatcher("parameters.jsp").forward(request, response);
	}
	
	
	public int getBroadUsers(Simulation sim){
		int result = 0;
		for(User u : sim.getUsers()){
			if(u.getType() != null){
				if(u.getType().equals(Constants.USER_TYPE_BROADCASTER)){
					result++;
			
				}
			}
		}
		return result;
	}
	
	public int getAqUsers(Simulation sim){
		int result = 0;
		for(User u : sim.getUsers()){
			if(u.getType() != null){
				if(u.getType().equals(Constants.USER_TYPE_ACQUAINTANCES)){
					result++;
			
				}
			}
		}
		return result;
	}
	
	public int getOddUsers(Simulation sim){
		int result = 0;
		for(User u : sim.getUsers()){
			if(u.getType() != null){
				if(u.getType().equals(Constants.USER_TYPE_ODDUSERS)){
					result++;
			
				}
			}
		}
		return result;
	}
	
	
	
	

}

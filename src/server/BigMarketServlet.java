package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import simulation.Launcher;
import simulation.Simulation;
import simulation.util.Neo4JManageTool;

/**
 * Servlet implementation class BigMarketServlet
 */
public class BigMarketServlet extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = -7634769531465961909L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public BigMarketServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		launchSimulation(request, response);

//        request.getRequestDispatcher("parameters.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);	
	}
	
	private void launchSimulation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Simulation sim = new Simulation(System.currentTimeMillis());
		
		if(Integer.parseInt(request.getParameter("nodes")) != 0){
			int numberOfNodes = Integer.parseInt(request.getParameter("nodes"));		
			sim.setNumberOfNodes(numberOfNodes);
		}else{
			
		}
		
		Launcher launcher = new Launcher(sim);
		launcher.start();
		
		request.getRequestDispatcher("parameters.jsp").forward(request, response);
	
//		numberOfTweets = sim.getEventManager().getStatistics().getTotalNumberOfTweets();
//		System.out.println("NUMERO FINAL DE USERS " + sim.getUsers().size());
//		Neo4JManageTool neoDB = new Neo4JManageTool(sim);
//		neoDB.launchDatabaseTool();
//		System.out.println("Program finished");
	}
	

}

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
	private static final long serialVersionUID = 1L;
	private int numberOfTweets;
	private boolean running = true;
       
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
		launchSimulation(request);

        request.getRequestDispatcher("pauseScreen.html").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);	
	}
	
	private void launchSimulation(HttpServletRequest request){
		int numberOfNodes = Integer.parseInt(request.getParameter("initialNodes"));
		String datasets = request.getParameter("datasetUsed");
		Simulation sim = new Simulation(System.currentTimeMillis());
		sim.setNumberOfNodes(numberOfNodes);
		
		if(datasets.equals("Yes")){
			sim.setFlag(0);
		}else{
			sim.setFlag(1);
		}
		
		Launcher launcher = new Launcher(sim);
		launcher.start();
	
//		numberOfTweets = sim.getEventManager().getStatistics().getTotalNumberOfTweets();
//		System.out.println("NUMERO FINAL DE USERS " + sim.getUsers().size());
//		Neo4JManageTool neoDB = new Neo4JManageTool(sim);
//		neoDB.launchDatabaseTool();
//		System.out.println("Program finished");
	}
	

}

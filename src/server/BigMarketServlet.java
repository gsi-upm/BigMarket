package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import simulation.Launcher;
import simulation.Simulation;

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
		writeHTML(response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);	
	}
	
	private void launchSimulation(HttpServletRequest request){
		int numberOfNodes = Integer.parseInt(request.getParameter("initialNodes"));
		String wannaGUI = request.getParameter("wannaGUI");
		String datasets = request.getParameter("datasetUsed");
		Simulation sim = new Simulation(System.currentTimeMillis());
		sim.setNumberOfNodes(numberOfNodes);
		
		if(datasets.equals("Yes")){
			sim.setFlag(0);
		}else{
			sim.setFlag(1);
		}
		Launcher launcher = new Launcher();
		if(wannaGUI.equals("Yes")){
			launcher.launchWithGUI(sim);
		}else{
			launcher.launchWithoutGUI(sim);
		}
		numberOfTweets = sim.getEventManager().getStatistics().getTotalNumberOfTweets();
	}
	
	public void writeHTML(HttpServletResponse response) throws IOException{
		PrintWriter pw = response.getWriter();
		pw.println("<HTML><HEAD><TITLE>Simulation Results</TITLE></HEAD>");
		pw.println("<H2>Simulation Results</H2><P>");
		pw.println("Number of total tweets: " + numberOfTweets);
		pw.println("</BODY></HTML>");
		pw.close();
	}

}

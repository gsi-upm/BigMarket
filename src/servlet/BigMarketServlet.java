package servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Servlet implementation class BigMarketServlet
 */
public class BigMarketServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private int alive = 0;
	private int death = 0;
	private long numberOfSteps = 0;
	public static final String KEYSPACE = "BigMarket";  
	public static final String ALIVE = "Alives";
	public static final String DEATH = "Deaths";
	public static final String STEPS = "Steps";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BigMarketServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		launchSimulation(request);
		launchCassandra();
	    writeHTML(response);
		writeFile("/home/dlara/PFC/BigMarket/BigMarketServlet/src/servlet/prueba.txt");
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * This method write a HTML page with the results of the simulations
	 * 
	 * @param response
	 * @throws IOException
	 */
	public void writeHTML(HttpServletResponse response) throws IOException{
		PrintWriter pw = response.getWriter();
		pw.println("<HTML><HEAD><TITLE>Simulation Results</TITLE></HEAD>");
		pw.println("<H2>Simulation Results</H2><P>");
	    pw.println("Number of alive: " + alive + "<BR>");
	    pw.println("Number of deaths: " + death + "<BR>");
	    pw.println("Number of steps: " + numberOfSteps + "<BR>");
	    pw.println("<A HREF=http://localhost:8080/BigMarketServlet/index.html> New simulation </A>");
		pw.println("</BODY></HTML>");
		pw.close();
	}
	
	/**
	 * This method is used to write a file and save it in local storage
	 * @param name
	 */
	public void writeFile(String name){
		File f = new File(name);
		try{
			FileWriter w = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw);  
			wr.write("Resultados de la simulación:" + "\n");
			wr.write("Number of alive: " + alive + "\n");
			wr.write("Number of deaths: " + death + "\n");
			wr.write("Number of steps: " + numberOfSteps + "\n");
			wr.close();
			bw.close();
		}catch(IOException e){};
	}	
	
	/**
	 * This is the method which launch the MASON simulation, it needs the param request
	 * in order to extract the data from the form
	 * 
	 * @param request
	 */
	
	private void launchSimulation(HttpServletRequest request){
		Tutorial1 tutorial1 = new Tutorial1(System.currentTimeMillis());
		int gW = Integer.parseInt(request.getParameter("gridW"));
		int gH = Integer.parseInt(request.getParameter("gridH"));
		numberOfSteps = Long.parseLong(request.getParameter("steps"));
		tutorial1.setGridDimension(gW, gH);
	    tutorial1.start();
	    long steps = 0;
	    while(steps < numberOfSteps){
	    	if (!tutorial1.schedule.step(tutorial1))
	    		break;
	    	steps = tutorial1.schedule.getSteps();
	    	if (steps % 500 == 0)
	    		System.out.println("Steps: " + steps + " Time: " + tutorial1.schedule.getTime());
	     	}
	    alive = tutorial1.numberOfAlive();
	    death = tutorial1.numberOfDead();
	    tutorial1.finish();
	}
		
	/**
	 * This method save the results of the simulation in Cassandra DB
	 */
	private void launchCassandra(){
		Cluster cluster1 = Cluster.builder().addContactPoints("localhost").build();
		Session session = cluster1.connect(KEYSPACE);
		double sim = Math.random();
		session.execute("USE " + KEYSPACE);
			
		session.execute("INSERT INTO BigM (sim_id, alives, deaths, steps)"
					+ " VALUES (" + sim + ", " + alive + ", " + death + ", "
					+ numberOfSteps + ");");
	}

	}


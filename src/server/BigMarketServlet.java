/**
*
*
* This file is part of BigMarket.
*
* BigMarket has been developed by members of the research Group on
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes),
* acknowledged group by the Technical University of Madrid [UPM]
* (Universidad Politécnica de Madrid)
*
* Authors:
* Daniel Lara
* Carlos A. Iglesias
* Emilio Serrano
*
* Contact:
* http://www.gsi.dit.upm.es/;
*
*
*
* BigMarket is free software:
* you can redistribute it and/or modify it under the terms of the GNU
* General Public License as published by the Free Software Foundation,
* either version 3 of the License, or (at your option) any later version.
*
*
* BigMarket is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with VoteSim. If not, see <http://www.gnu.org/licenses/>
*/
package server;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlas;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.graphstream.stream.file.FileSinkGEXF;
import org.graphstream.stream.file.FileSinkGEXF2;
import org.openide.util.Lookup;

import simulation.Launcher;
import simulation.Simulation;
import simulation.model.User;
import simulation.util.Constants;
import simulation.util.GraphJSONParser;
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
	private Neo4JManageTool neoDB;
	private String datasetName;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public BigMarketServlet() {
        super();
        this.neoDB = new Neo4JManageTool();
        System.out.println("SERVLET CREATED");
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter(Constants.BUTTON_PRESSED) != null){
			if(request.getParameter(Constants.BUTTON_PRESSED).equals(Constants.RUN_ONE_STEP)){	
				clickRunOneStep(request, response);
			}else if(request.getParameter(Constants.BUTTON_PRESSED).equals(Constants.RUN)){
				clickRun(request, response);
			}else if(request.getParameter(Constants.BUTTON_PRESSED).equals(Constants.PAUSE)){
				clickPause(request, response);
			}else{
				clickStop(request, response);
			}
		}else if(request.getParameter(Constants.ACTION_SELECTED) != null){
			if(request.getParameter(Constants.ACTION_SELECTED).equals(Constants.SEE_NETWORK)){
				clickSeeNetwork(request, response);
			}else if(request.getParameter(Constants.ACTION_SELECTED).equals(Constants.SEE_RESULTS)){
				String[] r = request.getParameterValues("resu");
				List<String> results = Arrays.asList(r); 
				if(results.contains(Constants.BETWEENNESS) && results.contains(Constants.CLOSENESS)){
					calculateBetweenness(request, response);
					calculateCloseness(request, response);
					request.getRequestDispatcher(Constants.SHOW_PAGE).forward(request, response);
				}else if(results.contains(Constants.BETWEENNESS) && !results.contains(Constants.CLOSENESS)){
					calculateBetweenness(request, response);
					request.getRequestDispatcher(Constants.SHOW_PAGE).forward(request, response);
				}else if(!results.contains(Constants.BETWEENNESS) && results.contains(Constants.CLOSENESS)){
					calculateCloseness(request, response);
					request.getRequestDispatcher(Constants.SHOW_PAGE).forward(request, response);
				}
			}else if(request.getParameter(Constants.ACTION_SELECTED).equals(Constants.SAVE)){
				clickSave(request, response);
			}else{
				clickExport(request, response);
			}
		
		}else if(request.getParameter("loadPushed").equals("ok")){
			System.out.println("HE PULSADO CARGAR");
			Neo4JManageTool n = new Neo4JManageTool();
			n.launchLoad(request.getParameter("bbddId"));
			sim = new Simulation(System.currentTimeMillis());
			sim.setDataBase(n);
			sim.setFlag(2);
			String data = request.getParameter("DataId");
			sim.setSimDataSet(data);
			Launcher launcher = new Launcher(sim);
			launcher.start();
			
			request.setAttribute("broadUsers",getBroadUsers(sim));
			request.setAttribute("aqUsers", getAqUsers(sim));
			request.setAttribute("oddUsers", getOddUsers(sim));
			request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
			request.setAttribute(Constants.SIM, sim);
			request.getRequestDispatcher(Constants.PARAMETERS_PAGE).forward(request, response);
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
			sim.setFlag(1);
			if(Integer.parseInt(request.getParameter("nodes")) != 0){
				int numberOfNodes = Integer.parseInt(request.getParameter("nodes"));		
				sim.setNumberOfNodes(numberOfNodes);
				String data = request.getParameter("DataId");
				sim.setSimDataSet(data);
			}else{
			}
				
		Launcher launcher = new Launcher(sim);
		launcher.start();
		
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.getRequestDispatcher(Constants.PARAMETERS_PAGE).forward(request, response);
		
		
	
		
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
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.setAttribute("oddTweets", sim.getEventManager().getStatistics().getOddTweets());
		request.setAttribute("broadTweets", sim.getEventManager().getStatistics().getBroadTweets());
		request.setAttribute("acqTweets", sim.getEventManager().getStatistics().getAcqTweets());
		request.getRequestDispatcher(Constants.PARAMETERS_PAGE).forward(request, response);
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
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.setAttribute("oddTweets", sim.getEventManager().getStatistics().getOddTweets());
		request.setAttribute("broadTweets", sim.getEventManager().getStatistics().getBroadTweets());
		request.setAttribute("acqTweets", sim.getEventManager().getStatistics().getAcqTweets());
		request.getRequestDispatcher(Constants.PARAMETERS_PAGE).forward(request, response);
		
		
	}
	
	private void clickPause(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if(sim.getGui().getConsole().getPlayState() != 2){
			sim.getGui().getConsole().pressPause();
		}
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		request.setAttribute("oddTweets", sim.getEventManager().getStatistics().getOddTweets());
		request.setAttribute("broadTweets", sim.getEventManager().getStatistics().getBroadTweets());
		request.setAttribute("acqTweets", sim.getEventManager().getStatistics().getAcqTweets());
		request.getRequestDispatcher(Constants.PARAMETERS_PAGE).forward(request, response);
	}
	
	private void clickStop(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		sim.getGui().getConsole().pressStop();
		request.setAttribute("broadUsers",getBroadUsers(sim));
		request.setAttribute("aqUsers", getAqUsers(sim));
		request.setAttribute("oddUsers", getOddUsers(sim));
		request.setAttribute(Constants.STEPS, sim.schedule.getSteps());
		request.setAttribute(Constants.SIM, sim);
		
		sim.finish();
		
		request.getRequestDispatcher(Constants.RESULTS_PAGE).forward(request, response);
	}
	
	private void clickSave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	
		neoDB.setSim(sim);
		neoDB.launchDatabaseTool();
		
		request.getRequestDispatcher(Constants.RESULTS_PAGE).forward(request, response);
	}
	
	private void calculateBetweenness(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		String path = Constants.GRAPH_PATH;
		FileSinkGEXF2 out = new FileSinkGEXF2();
		out.writeAll(sim.getGraphManager().getGraph(), path);
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;
		try{
			File file = new File(path);
			container = importController.importFile(file);
			container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
			container.setAllowAutoNode(false);
		}catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		importController.process(container,new DefaultProcessor(), workspace);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
	
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);
		
		
		AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
		AttributeModel model = ac.getModel();
		AttributeColumn col = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		System.out.println(col);
		
		HashMap<Double, Integer> bet = new HashMap<Double, Integer>();
		List<Double> finalBet = new ArrayList<Double>();
		List<Integer> finalNodes = new ArrayList<Integer>();
		for(Node n : graphModel.getGraph().getNodes()) {
			 Double centrality = (Double)n.getNodeData().getAttributes().getValue(col.getTitle());
			 System.out.println("Node " + n.getId() + " betweeness " + centrality);
			 bet.put(centrality, n.getId());
		}
		List<Double> betPerNode = new ArrayList<Double>(bet.keySet());

	    Collections.sort(betPerNode, new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				// TODO Auto-generated method stub
				return (int) (o1-o2);
			}

			
	    });
	    
	    java.util.Collections.reverse(betPerNode);
    
	    for(double p : betPerNode){
	    	finalBet.add(p);
	    	finalNodes.add(bet.get(p));  	
	    }
	    Double[] arrayBet = new Double[finalBet.size()];
	    int[] arrayNodes = new int[finalNodes.size()];
	    
	    for(int i = 0; i < arrayBet.length; i++){
	    	arrayBet[i] = finalBet.get(i);
	    }
	    
	    for(int j = 0; j < arrayNodes.length; j++){
	    	arrayNodes[j] = finalNodes.get(j);
	    }
	    
		request.setAttribute("bet", arrayBet);
		request.setAttribute("betnodes", arrayNodes);
		request.getRequestDispatcher(Constants.SHOW_PAGE).forward(request, response);

	}
	
	private void calculateCloseness(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String path = Constants.GRAPH_PATH;
		FileSinkGEXF2 out = new FileSinkGEXF2();
		out.writeAll(sim.getGraphManager().getGraph(), path);
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;
		try{
			File file = new File(path);
			container = importController.importFile(file);
			container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
			container.setAllowAutoNode(false);
		}catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		importController.process(container,new DefaultProcessor(), workspace);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
	
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);
		
		
		AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
		AttributeModel model = ac.getModel();
		AttributeColumn col = attributeModel.getNodeTable().getColumn(GraphDistance.CLOSENESS);
		System.out.println(col);
		SortedMap<Integer, Double> close = new TreeMap<Integer, Double>(java.util.Collections.reverseOrder());
		for(Node n : graphModel.getGraph().getNodes()) {
			 Double centrality = (Double)n.getNodeData().getAttributes().getValue(col.getTitle());
			 System.out.println("Node " + n.getId() + " closeness " + centrality);
			 close.put(n.getId(), centrality);
		}
		
		request.setAttribute("close", close);
	}
	
	private void clickSeeNetwork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		GraphJSONParser g = new GraphJSONParser(sim.getGraphManager().getGraph());
		g.launchParser();
		request.getRequestDispatcher(Constants.SEE_NET_PAGE).forward(request, response);
	}
	
	
	private void clickExport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String path = Constants.GRAPH_PATH;
		FileSinkGEXF2 out = new FileSinkGEXF2();
		out.writeAll(sim.getGraphManager().getGraph(), path);
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;
		try{javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
		String ruta = "";
		try{
		if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION){
		ruta = jF1.getSelectedFile().getAbsolutePath();
		//Aqui ya tiens la ruta,,,ahora puedes crear un fichero n esa ruta y escribir lo k kieras...
		}
		}catch (Exception ex){
		ex.printStackTrace();
		} 
			File file = new File(Constants.GRAPH_PATH);
			container = importController.importFile(file);
			container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
			container.setAllowAutoNode(false);
		}catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		
		
		importController.process(container,new DefaultProcessor(), workspace);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		
		
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");     //Get GEXF exporter
        exporter.setExportVisible(true);  //Only exports the visible (filtered) graph
        exporter.setWorkspace(workspace);
        String path2 = Constants.GRAPH1_PATH;
        try {
            ec.exportFile(new File(Constants.GRAPH1_PATH), exporter);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        
        try {
			System.out.println("PUNTO 1");
            // Url con la foto
            URL url = new URL(
                    "http://simplez.gsi.dit.upm.es:8080/BigMarket/g.gexf");
            
			System.out.println("PUNTO 2");

            // establecemos conexion
            URLConnection urlCon = url.openConnection();
            
			System.out.println("PUNTO 3");
 
            // Sacamos por pantalla el tipo de fichero
            System.out.println(urlCon.getContentType());
			System.out.println("PUNTO 4");
 
            // Se obtiene el inputStream de la foto web y se abre el fichero
            // local.
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream(Constants.GRAPH_PRUEBA_PATH);
 
            // Lectura de la foto de la web y escritura en fichero local
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            int leido = is.read(array);
            while (leido > 0) {
                fos.write(array, 0, leido);
                leido = is.read(array);
            }
 
            // cierre de conexion y fichero.
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        request.getRequestDispatcher(Constants.RESULTS_PAGE).forward(request, response);
	}
	
	
//	private void clickExport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//		String path = "/home/dlara/Gitted/WebContent/g.gexf";
//		FileSinkGEXF2 out = new FileSinkGEXF2();
//		out.writeAll(sim.getGraphManager().getGraph(), path);
//		
//		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
//		pc.newProject();
//		Workspace workspace = pc.getCurrentWorkspace();
//		
//		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
//		Container container;
//		try{
//			File file = new File("/home/dlara/Gitted/WebContent/g.gexf");
//			container = importController.importFile(file);
//			container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
//			container.setAllowAutoNode(false);
//		}catch(Exception ex) {
//			ex.printStackTrace();
//			return;
//		}
//		
//		importController.process(container,new DefaultProcessor(), workspace);
//		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
//		
//		
//		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
//		GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");     //Get GEXF exporter
//        exporter.setExportVisible(true);  //Only exports the visible (filtered) graph
//        exporter.setWorkspace(workspace);
//        String path2 = "/home/dlara/Gitted/WebContent/g1.gexf";
//        try {
//            ec.exportFile(new File("/home/dlara/Gitted/WebContent/g1.gexf"), exporter);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return;
//        }
//		request.setAttribute("file", path2);
//		request.getRequestDispatcher("network.html").forward(request, response);
//	}
	
	
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

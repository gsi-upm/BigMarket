package server;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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
		}else if(request.getParameter("action") != null){
			if(request.getParameter("action").equals("see")){
				clickSeeNetwork(request, response);
			}else if(request.getParameter("action").equals("results")){
				String[] r = request.getParameterValues("resu");
				List<String> results = Arrays.asList(r); 
				if(results.contains("betweenness") && results.contains("closeness")){
					calculateBetweenness(request, response);
					calculateCloseness(request, response);
					request.getRequestDispatcher("show.jsp").forward(request, response);
				}else if(results.contains("betweenness") && !results.contains("closeness")){
					calculateBetweenness(request, response);
					request.getRequestDispatcher("show.jsp").forward(request, response);
				}else if(!results.contains("betweenness") && results.contains("closeness")){
					calculateCloseness(request, response);
					request.getRequestDispatcher("show.jsp").forward(request, response);
				}
			}else{
				clickExport(request, response);
			}
		
		}else if(request.getParameter("loadPushed").equals("ok")){
			System.out.println("HE PULSADO CARGAR");
			Neo4JManageTool n = new Neo4JManageTool();
//			n.retrieveGraph(request.getParameter("bbddId"));
			
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
			String data = request.getParameter("DataId");
			sim.setSimDataSet(data);
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
		
		neoDB.setSim(sim);
		neoDB.launchDatabaseTool();
		neoDB.saveDataSetName(sim.getSimDataset());
		
		request.getRequestDispatcher("results.jsp").forward(request, response);
	}
	
	private void calculateBetweenness(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String path = "/home/dlara/Gitted/WebContent/g.gexf";
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
		SortedMap bet = new TreeMap(java.util.Collections.reverseOrder());
		for(Node n : graphModel.getGraph().getNodes()) {
			 Double centrality = (Double)n.getNodeData().getAttributes().getValue(col.getTitle());
			 System.out.println("Node " + n.getId() + " betweeness " + centrality);
			 bet.put(n.getId(), centrality);
		}
		Iterator iterator = bet.keySet().iterator();
		while (iterator.hasNext()) {
		Object key = iterator.next();
		System.out.println("Clave : " + key + " Valor :" + bet.get(key));
		}
		request.setAttribute("betweenness", bet);
	}
	
	private void calculateCloseness(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String path = "/home/dlara/Gitted/WebContent/g.gexf";
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
		String path = "/home/dlara/Gitted/WebContent/g.gexf";
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
		
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance(200f);
		
		layout.initAlgo();
		
		for(int i = 0; i < 100 && layout.canAlgo(); i++){
			layout.goAlgo();
		}
		
		 
		
		String path2 = "/home/dlara/Gitted/WebContent/g1.gexf";

		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		try{
			ec.exportFile(new File(path2));
		}catch(IOException ex) {
			ex.printStackTrace();
			return;
		}

		request.setAttribute("file", path2);
		request.getRequestDispatcher("network.html").forward(request, response);
	}
	
	private void clickExport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String path = "/home/dlara/Gitted/WebContent/g.gexf";
		FileSinkGEXF2 out = new FileSinkGEXF2();
		out.writeAll(sim.getGraphManager().getGraph(), path);
		
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;
		try{
			File file = new File("/home/dlara/Gitted/WebContent/g.gexf");
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
        String path2 = "/home/dlara/Gitted/WebContent/g1.gexf";
        try {
            ec.exportFile(new File("/home/dlara/Gitted/WebContent/g1.gexf"), exporter);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
		request.setAttribute("file", path2);
		request.getRequestDispatcher("network.html").forward(request, response);
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

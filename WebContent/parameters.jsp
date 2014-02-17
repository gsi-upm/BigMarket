<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "simulation.Simulation" %>
<%@ page import= "simulation.Launcher" %> 
<%@ page import= "simulation.util.Neo4JManageTool" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Simulation parameters</title>
</head>
<body>
	<%
	int numberOfNodes = Integer.parseInt(request.getParameter("initialNodes"));
	String wannaGUI = request.getParameter("wannaGUI");
	String datasets = request.getParameter("datasetUsed");
	Simulation sim = new Simulation(System.currentTimeMillis());
	sim.setNumberOfNodes(numberOfNodes);
	Thread t = Thread.currentThread();
	t.setName("Main Thread");
	if(datasets.equals("Yes")){
		sim.setFlag(0);
	}else{
		sim.setFlag(1);
	}
	
	Launcher launcher = new Launcher(sim);
	launcher.start();
	t.sleep(2500);
	int control = 1;
	int control2 = 5;
	while(control2 != 1){
		control2 = sim.getGui().getConsole().getPlayState();
	}
	while(control != 0){
		
		if(sim.getGui().getConsole().getPlayState() == 2){
			
		}
		control = sim.getGui().getConsole().getPlayState();
	}
	//int numberOfTweets = sim.getEventManager().getStatistics().getTotalNumberOfTweets();
	//System.out.println("NUMERO FINAL DE USERS " + sim.getUsers().size());
	//Neo4JManageTool neoDB = new Neo4JManageTool(sim);
	//neoDB.launchDatabaseTool();
	//System.out.println("Program finished");
	%>
	<%@ include file="pauseScreen.html"%>
</body>
</html>
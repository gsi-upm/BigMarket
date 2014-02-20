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
	if(datasets.equals("Yes")){
		sim.setFlag(0);
	}else{
		sim.setFlag(1);
	}
	
	Launcher launcher = new Launcher(sim);
	launcher.start();
	t.sleep(3000);
	System.out.println("SALGO DEL WHILE");
	sim.getGui().getConsole().pressPause();
	launcher.sleep(10000);
	System.out.println("ESTADO: " + sim.getGui().getConsole().getPlayState());
	while(sim.getGui().getConsole().getPlayState() == 2){
		
	}
	
	//sim.getGui().getConsole().pressPlay();
	//t.sleep(2000);
	//int numberOfTweets = sim.getEventManager().getStatistics().getTotalNumberOfTweets();
	//System.out.println("NUMERO FINAL DE USERS " + sim.getUsers().size());
	//Neo4JManageTool neoDB = new Neo4JManageTool(sim);
	//neoDB.launchDatabaseTool();
	//System.out.println("Program finished");
	%>
	<%@ include file="pauseScreen.html"%>
</body>
</html>